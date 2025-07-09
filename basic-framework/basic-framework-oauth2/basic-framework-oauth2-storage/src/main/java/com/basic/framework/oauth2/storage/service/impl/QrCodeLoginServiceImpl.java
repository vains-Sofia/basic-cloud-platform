package com.basic.framework.oauth2.storage.service.impl;

import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oauth2.BasicAuthenticatedUser;
import com.basic.framework.oauth2.core.domain.security.QrCodeStatus;
import com.basic.framework.oauth2.core.domain.thired.ThirdAuthenticatedUser;
import com.basic.framework.oauth2.core.enums.QrCodeStatusEnum;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import com.basic.framework.oauth2.storage.domain.request.QrCodeConfirmRequest;
import com.basic.framework.oauth2.storage.domain.request.QrCodeScanRequest;
import com.basic.framework.oauth2.storage.domain.response.QrCodeScanResponse;
import com.basic.framework.oauth2.storage.domain.response.QrInitResponse;
import com.basic.framework.oauth2.storage.service.QrCodeLoginService;
import com.basic.framework.oauth2.storage.sse.SseEmitterManager;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Objects;
import java.util.UUID;

/**
 * 二维码登录 Service 实现
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class QrCodeLoginServiceImpl implements QrCodeLoginService {

    private final SseEmitterManager sseEmitterManager;

    private final RedisTemplate<String, ?> redisTemplate;

    private final RedisOperator<String> stringRedisOperator;

    private final RedisOperator<QrCodeStatus> redisOperator;

    @Override
    public QrInitResponse generateQrCode() {
        // 生成唯一的token
        String token = UUID.randomUUID().toString();
        QrCodeStatus status = new QrCodeStatus();
        status.setToken(token);
        status.setStatus(QrCodeStatusEnum.PENDING);

        // 设置二维码状态到Redis中
        String key = AuthorizeConstants.QR_STATUS_CACHE + token;
        redisOperator.set(key, status, AuthorizeConstants.EXPIRE_SECONDS);

        // 封装响应对象
        return new QrInitResponse(token, AuthorizeConstants.EXPIRE_SECONDS);
    }

    @Override
    public SseEmitter poll(String token) {
        QrCodeStatus statusCache = redisOperator.get(AuthorizeConstants.QR_STATUS_CACHE + token);
        if (statusCache == null) {
            // 设置失效状态
            statusCache = new QrCodeStatus();
            statusCache.setToken(token);
            statusCache.setStatus(QrCodeStatusEnum.EXPIRED);
            // Redis Pub/Sub 通知所有节点
            redisTemplate.convertAndSend("qr-code-scan-channel", JsonUtils.toJson(statusCache));
        }

        // 默认30秒
        long timeout = 30 * 1000L;
        // 存入集中管理器并返回
        return this.sseEmitterManager.addEmitter(token, timeout);
    }

    @Override
    public QrCodeScanResponse scan(QrCodeScanRequest request) {
        String token = request.getToken();
        QrCodeStatus statusCache = redisOperator.get(AuthorizeConstants.QR_STATUS_CACHE + token);
        if (statusCache == null) {
            throw new CloudServiceException("二维码已过期，请重新生成二维码.");
        }

        // 验证状态
        if (statusCache.getStatus() != QrCodeStatusEnum.PENDING) {
            throw new CloudServiceException("二维码不可用，请重新生成二维码.");
        }

        // 验证用户认证状态
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new CloudServiceException("未登录，无法进行二维码扫描.");
        }

        statusCache.setStatus(QrCodeStatusEnum.SCANNED);
        statusCache.setUserId(authenticatedUser.getId());
        if (authenticatedUser instanceof BasicAuthenticatedUser basicUser) {
            statusCache.setEmail(basicUser.getEmail());
            statusCache.setPicture(basicUser.getPicture());
            statusCache.setNickname(basicUser.getNickname());
        }

        if (authenticatedUser instanceof ThirdAuthenticatedUser thirdUser) {
            statusCache.setEmail(thirdUser.getEmail());
            statusCache.setPicture(thirdUser.getPicture());
            statusCache.setNickname(thirdUser.getNickname());
        }

        // 更新状态到Redis
        redisOperator.set(AuthorizeConstants.QR_STATUS_CACHE + token, statusCache, AuthorizeConstants.EXPIRE_SECONDS);

        // 生成临时票据
        String ticket = UUID.randomUUID().toString();
        // 将票据存入Redis，设置过期时间
        String redisQrCodeTicketKey = String.format("%s%s:%s", AuthorizeConstants.QR_STATUS_CACHE, token, ticket);
        stringRedisOperator.set(redisQrCodeTicketKey, ticket, AuthorizeConstants.EXPIRE_SECONDS);

        // Redis Pub/Sub 通知所有节点
        redisTemplate.convertAndSend("qr-code-app-channel", JsonUtils.toJson(statusCache));

        QrCodeScanResponse resp = new QrCodeScanResponse();
        resp.setTicket(ticket);
        resp.setStatus(statusCache.getStatus());
        return resp;
    }

    @Override
    public void confirm(QrCodeConfirmRequest request) {
        String token = request.getToken();
        String ticket = request.getTicket();

        QrCodeStatus statusCache = redisOperator.get(AuthorizeConstants.QR_STATUS_CACHE + token);
        if (statusCache == null) {
            throw new CloudServiceException("二维码已过期，请重新生成二维码.");
        }

        // 验证状态
        if (statusCache.getStatus() != QrCodeStatusEnum.SCANNED) {
            throw new CloudServiceException("二维码不可用，请重新生成二维码.");
        }

        // 验证票据
        String redisQrCodeTicketKey = String.format("%s%s:%s", AuthorizeConstants.QR_STATUS_CACHE, token, ticket);
        String cachedTicket = stringRedisOperator.get(redisQrCodeTicketKey);
        if (cachedTicket == null || !cachedTicket.equals(ticket)) {
            throw new CloudServiceException("无效或已过期，请重新扫描二维码.");
        }

        // 验证用户认证状态
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new CloudServiceException("未登录，无法进行二维码扫描.");
        }

        // 验证用户ID是否匹配
        if (!Objects.equals(statusCache.getUserId(), authenticatedUser.getId())) {
            throw new CloudServiceException("二维码已被其他用户扫描，请重新生成二维码.");
        }

        // 设置状态为已确认
        statusCache.setStatus(QrCodeStatusEnum.CONFIRMED);

        // 更新状态到Redis
        redisOperator.set(AuthorizeConstants.QR_STATUS_CACHE + token, statusCache, AuthorizeConstants.EXPIRE_SECONDS);
        // Redis Pub/Sub 通知所有节点
        redisTemplate.convertAndSend("qr-code-app-channel", JsonUtils.toJson(statusCache));
    }
}
