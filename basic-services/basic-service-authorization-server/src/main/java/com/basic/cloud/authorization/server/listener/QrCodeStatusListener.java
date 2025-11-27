package com.basic.cloud.authorization.server.listener;

import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.core.domain.security.QrCodeStatus;
import com.basic.framework.oauth2.core.enums.QrCodeStatusEnum;
import com.basic.framework.oauth2.storage.sse.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import static com.basic.framework.oauth2.core.constant.AuthorizeConstants.QR_STATUS_CACHE;

/**
 * 移动端扫描监听器
 * 监听Redis消息，处理二维码扫描事件
 *
 * @author vains
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QrCodeStatusListener {

    private final SseEmitterManager emitterManager;

    @EventListener
    public void handleScanEvent(QrCodeStatus statusCache) {
        boolean sendMessage = emitterManager.sendMessage(statusCache.getToken(), statusCache.getStatus().getValue(), statusCache);
        if (log.isDebugEnabled()) {
            log.debug("处理设备扫描事件: 二维码={}, 发送消息={}，成功状态={}", statusCache.getToken(), statusCache, sendMessage);
        }
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        container.addMessageListener((message, pattern) ->
                        handleScanEvent(JsonUtils.toObject(message.toString(), QrCodeStatus.class)),
                new PatternTopic("qr-code-app-channel")
        );

        // 监听 String 类型 key 的过期事件
        Topic expireTopic = new PatternTopic("__keyevent@0__:expired");

        container.addMessageListener((message, pattern) -> {
            String expiredKey = message.toString();
            // 如果是二维码的key
            if (expiredKey.startsWith(QR_STATUS_CACHE)) {
                // 提取二维码id
                String qrCodeId = extractQrCodeId(expiredKey);
                if (!ObjectUtils.isEmpty(qrCodeId)) {
                    // 推送删除事件
                    QrCodeStatus qrCodeStatus = new QrCodeStatus();
                    qrCodeStatus.setToken(qrCodeId);
                    qrCodeStatus.setStatus(QrCodeStatusEnum.EXPIRED);
                    handleScanEvent(qrCodeStatus);
                }
            }

        }, expireTopic);

        return container;
    }

    public String extractQrCodeId(String key) {
        if (!key.startsWith(QR_STATUS_CACHE)) {
            return null;
        }

        String rest = key.substring(QR_STATUS_CACHE.length());
        // rest 可能是：qrCodeId 或 qrCodeId:ticket
        int index = rest.indexOf(":");
        if (index >= 0) {
            return rest.substring(0, index);
        }
        return rest; // 无 ticket 的情况
    }

}
