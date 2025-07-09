package com.basic.cloud.authorization.server.listener;

import com.basic.framework.oauth2.storage.sse.SseEmitterManager;
import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.core.domain.security.QrCodeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

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

        return container;
    }
}
