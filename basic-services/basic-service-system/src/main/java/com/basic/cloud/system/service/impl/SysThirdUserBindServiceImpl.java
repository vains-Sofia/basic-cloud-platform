package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.MailSenderRequest;
import com.basic.cloud.system.api.enums.CheckBindingStatusEnum;
import com.basic.cloud.system.domain.SysBasicUser;
import com.basic.cloud.system.domain.SysThirdUserBind;
import com.basic.cloud.system.domain.model.BindingConfirmationTemplate;
import com.basic.cloud.system.domain.model.ConfirmSuccessTemplate;
import com.basic.cloud.system.enums.BindStatusEnum;
import com.basic.cloud.system.repository.SysBasicUserRepository;
import com.basic.cloud.system.repository.SysThirdUserBindRepository;
import com.basic.cloud.system.service.CommonService;
import com.basic.cloud.system.service.SysThirdUserBindService;
import com.basic.framework.core.constants.DateFormatConstants;
import com.basic.framework.core.constants.PlatformConstants;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.core.util.Sequence;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import com.basic.framework.oauth2.core.domain.thired.ThirdAuthenticatedUser;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import com.basic.framework.oauth2.core.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * 三方登录用户绑定服务实现类
 *
 * @author vains
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysThirdUserBindServiceImpl implements SysThirdUserBindService {

    private final CommonService commonService;

    private final TemplateEngine templateEngine;

    private final ServerProperties serverProperties;

    private final Sequence sequence = new Sequence((null));

    private final SysBasicUserRepository basicUserRepository;

    private final SysThirdUserBindRepository thirdUserBindRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckBindingStatusEnum checkBinding() {
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        if (!(authenticatedUser instanceof ThirdAuthenticatedUser thirdUser)) {
            throw new CloudServiceException("当前用户不是三方登录用户，无法进行绑定检查");
        }

        // 检查绑定记录
        String userId = thirdUser.getId() == null ? thirdUser.getSub() : thirdUser.getId() + "";
        Optional<SysThirdUserBind> userBindOpt = thirdUserBindRepository.findByProviderAndProviderUserId(thirdUser.getAccountPlatform(), userId);
        if (userBindOpt.isPresent()) {
            if (log.isDebugEnabled()) {
                log.debug("用户 {} 已经绑定，绑定信息: {}", thirdUser.getName(), userBindOpt.get());
            }
            SysThirdUserBind thirdUserBind = userBindOpt.get();
            // 如果已存在绑定记录且状态为已绑定，则直接返回
            if (BindStatusEnum.BOUND.equals(thirdUserBind.getBindStatus())) {
                return CheckBindingStatusEnum.BOUND;
            }
            // 检查确认token是否过期
            if (thirdUserBind.getTokenExpiresAt().isAfter(LocalDateTime.now())) {
                // 如果状态为待确认且待确认token未过期，则返回重复绑定提示
                return CheckBindingStatusEnum.CONFLICT;
            } else {
                // 如果状态为待确认且待确认token已过期，则删除该记录，让后边重新生成并发送确认邮件
                if (log.isDebugEnabled()) {
                    log.debug("检测到绑定记录已过期，删除绑定记录: {}", thirdUserBind);
                }
                thirdUserBindRepository.delete(thirdUserBind);
            }
        }

        if (ObjectUtils.isEmpty(thirdUser.getEmail())) {
            if (log.isDebugEnabled()) {
                log.debug("检测到未绑定邮箱，需要主动绑定");
            }
            return CheckBindingStatusEnum.NON_EMAIL;
        }

        // 检查是否有相同邮箱的本地账号
        Optional<SysBasicUser> existingUser = basicUserRepository.findByEmail(thirdUser.getEmail());
        if (existingUser.isPresent()) {
            // 创建待绑定记录 + 发送确认邮件
            SysThirdUserBind thirdUserBind = new SysThirdUserBind();
            thirdUserBind.setId(sequence.nextId());
            thirdUserBind.setUserId(existingUser.get().getId());
            thirdUserBind.setProvider(thirdUser.getAccountPlatform());
            thirdUserBind.setProviderUserId(userId);
            thirdUserBind.setEmail(thirdUser.getEmail());
            thirdUserBind.setAccessToken(thirdUser.getAccessToken());
            thirdUserBind.setBindStatus(BindStatusEnum.PENDING_CONFIRMATION);
            thirdUserBind.setConfirmToken(UUID.randomUUID().toString());
            thirdUserBind.setTokenExpiresAt(LocalDateTime.now().plusMinutes(30));
            thirdUserBind.setCreateTime(LocalDateTime.now());
            thirdUserBind.setUpdateTime(LocalDateTime.now());
            thirdUserBindRepository.save(thirdUserBind);

            // 发送邮件
            this.sendBindConfirmation(thirdUser, thirdUserBind.getConfirmToken());

            // 响应状态
            return CheckBindingStatusEnum.PENDING_CONFIRM;
        }

        // 创建新用户并绑定
        this.registerBasicUser(thirdUser);

        return CheckBindingStatusEnum.NEW_CREATED;
    }

    @Override
    public void registerBasicUser(ThirdAuthenticatedUser thirdUser) {
        log.info("三方登录获取到的邮箱为：{}", thirdUser.getEmail());
        SysBasicUser sysBasicUser = new SysBasicUser();
        sysBasicUser.setId(sequence.nextId());
        sysBasicUser.setUsername(thirdUser.getSub());
        sysBasicUser.setNickname(thirdUser.getNickname());
        sysBasicUser.setEmail(thirdUser.getEmail());
        if (ObjectUtils.isEmpty(sysBasicUser.getEmail())) {
            throw new CloudServiceException("三方用户没有绑定邮箱，无法注册");
        }
        sysBasicUser.setEmailVerified(true);
        sysBasicUser.setPhoneNumber(thirdUser.getPhoneNumber());
        sysBasicUser.setPhoneNumberVerified(!ObjectUtils.isEmpty(thirdUser.getPhoneNumber()));
        sysBasicUser.setPassword(thirdUser.getAccessToken()); // 使用access token作为密码
        sysBasicUser.setProfile(thirdUser.getProfile());
        sysBasicUser.setPicture(thirdUser.getPicture());
        sysBasicUser.setDeleted(Boolean.FALSE);
        sysBasicUser.setAccountPlatform(thirdUser.getAccountPlatform());
        if (!ObjectUtils.isEmpty(thirdUser.getBirthdate())) {
            sysBasicUser.setBirthdate(LocalDate.parse(thirdUser.getBirthdate()));
        }

        // 构建默认认证用户为当前注册用户
        DefaultAuthenticatedUser authenticatedUser = new DefaultAuthenticatedUser(
                sysBasicUser.getNickname(), sysBasicUser.getAccountPlatform(), null);
        authenticatedUser.setId(sysBasicUser.getId());
        // 填充SecurityContextHolder，让审计信息自动填充可以获取到需要的数据
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(authenticatedUser, sysBasicUser.getPassword(), null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        log.info("基础用户信息中的邮箱为：{}", sysBasicUser.getEmail());
        log.info("注册三方用户信息：{}", sysBasicUser);
        basicUserRepository.save(sysBasicUser);
    }

    @Override
    public void sendBindConfirmation(ThirdAuthenticatedUser thirdUser, String confirmToken) {
        // 构建绑定确认api
        HttpServletRequest request = ServletUtils.getRequest();
        if (ObjectUtils.isEmpty(request)) {
            if (log.isDebugEnabled()) {
                log.debug("无法获取HttpServletRequest，无法发送绑定确认邮件");
            }
            return;
        }

        // 根据当前请求组装确认绑定链接
        String host = request.getRequestURL().substring(0, request.getRequestURL().indexOf(serverProperties.getServlet().getContextPath()));
        String confirmUrl = host + serverProperties.getServlet().getContextPath() + "/third/user/confirm?confirmToken=" + confirmToken;

        // 构建绑定确认模板数据
        BindingConfirmationTemplate template = BindingConfirmationTemplate.builder()
                .nickname(thirdUser.getNickname())
                .accountPlatform(thirdUser.getAccountPlatform())
                .email(thirdUser.getEmail())
                .requestTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_TIME_FORMAT)))
                .ipAddress(getClientIpAddress(request))
                .expiresIn(30 + "分钟")
                .confirmUrl(confirmUrl)
                .companyName(PlatformConstants.PLATFORM_NAME)
                .build();
        // 创建 Thymeleaf 上下文
        Context context = new Context();
        context.setVariables(JsonUtils.objectToObject(template, Map.class, String.class, Object.class));
        // 渲染模板
        String content = templateEngine.process("binding-confirmation", context);

        // 构建邮件发送请求
        MailSenderRequest senderRequest = new MailSenderRequest();
        senderRequest.setFrom(PlatformConstants.PLATFORM_NAME);
        senderRequest.setMailTo(Set.of(thirdUser.getEmail()));
        senderRequest.setSubject("Basic Cloud 绑定确认");
        senderRequest.setContent(content);
        senderRequest.setContentIsHtml(Boolean.TRUE);
        // 发送邮件
        commonService.mailSender(senderRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConfirmSuccessTemplate confirm(String confirmToken) {
        try {
            SysThirdUserBind thirdUserBind = thirdUserBindRepository.findByConfirmToken(confirmToken)
                    .orElseThrow(() -> new CloudServiceException("无效或已失效的绑定请求"));

            if (BindStatusEnum.BOUND.equals(thirdUserBind.getBindStatus())) {
                throw new CloudServiceException("该绑定请求已被确认，请勿重复操作");
            }

            if (thirdUserBind.getTokenExpiresAt() == null || thirdUserBind.getTokenExpiresAt().isBefore(LocalDateTime.now())) {
                throw new CloudIllegalArgumentException("绑定请求已过期");
            }

            // 检查该 provider_user_id 是否已绑定其他用户（防止多绑定）
            boolean exists = thirdUserBindRepository.existsByProviderAndProviderUserIdAndBindStatus(
                    thirdUserBind.getProvider(), thirdUserBind.getProviderUserId(), BindStatusEnum.BOUND
            );
            if (exists) {
                throw new CloudServiceException("该三方账号已被其他用户绑定，请使用其他账号进行绑定");
            }

            // 更新状态为已绑定
            thirdUserBind.setBindStatus(BindStatusEnum.BOUND);
            thirdUserBind.setConfirmToken(null);
            thirdUserBind.setTokenExpiresAt(null);
            thirdUserBind.setBindTime(LocalDateTime.now());
            thirdUserBind.setUpdateTime(LocalDateTime.now());
            thirdUserBindRepository.save(thirdUserBind);

            SysBasicUser basicUserOpt = basicUserRepository.findById(thirdUserBind.getUserId())
                    .orElseThrow(() -> new CloudServiceException("绑定用户不存在"));

            // 返回绑定成功模板数据
            return ConfirmSuccessTemplate.builder()
                    .success(true)
                    .nickname(basicUserOpt.getNickname())
                    .bindTime(thirdUserBind.getBindTime().format(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_TIME_FORMAT)))
                    .build();
        } catch (Exception e) {
            log.error("确认绑定失败，原因：{}", e.getMessage(), e);
            return ConfirmSuccessTemplate.builder()
                    .success(false)
                    .cause(e.getMessage())
                    .build();
        }
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HttpServletRequest
     * @return 客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // 多个IP时取第一个
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}
