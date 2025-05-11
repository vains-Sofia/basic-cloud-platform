package com.basic.framework.redis.autoconfigure;

import com.basic.framework.redis.aop.RedisLockAspect;
import com.basic.framework.redis.util.RedisConfigUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Redis相关自动配置类
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP, keyspaceNotificationsConfigParameter = "")
public class DataRedisAutoConfiguration {

    private final Jackson2ObjectMapperBuilder builder;

    /**
     * 注入切面
     *
     * @param redissonClient 分布式锁的java实现
     * @return RedisLockAspect
     */
    @Bean
    public RedisLockAspect redisLockAspect(RedissonClient redissonClient) {
        return new RedisLockAspect(redissonClient);
    }

    /**
     * 默认情况下使用
     *
     * @param connectionFactory redis链接工厂
     * @return RedisTemplate
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = RedisConfigUtils.buildRedisObjectMapper(builder);

        // 存入redis时序列化值的序列化器
        Jackson2JsonRedisSerializer<Object> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        return RedisConfigUtils.buildRedisTemplate(connectionFactory, valueSerializer);
    }

    @PostConstruct
    public void postConstruct() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing Data Redis Auto Configuration.");
        }
    }

}
