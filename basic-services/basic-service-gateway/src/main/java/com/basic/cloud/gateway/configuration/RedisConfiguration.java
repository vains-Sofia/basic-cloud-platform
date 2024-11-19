package com.basic.cloud.gateway.configuration;

import com.basic.framework.redis.util.RedisConfigUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.jackson2.CoreJackson2Module;

/**
 * redis配置类
 *
 * @author vains
 */
@Configuration(proxyBeanMethods = false)
public class RedisConfiguration {

    /**
     * 自己封装的resource server starter中虽然也有该配置，但是不知道为什么
     * 引入Spring Cloud Gateway以后会优先注入Redisson Spring Boot Starter
     * 中的RedisTemplate，导致资源服务注入的redisTemplate不生效以至于无法读取
     * 认证服务签发access token时存入Redis中的用户信息，所以手动在这里注入一下，
     * 以替换Redisson中默认的redisTemplate
     *
     * @param connectionFactory redis链接工厂
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(Jackson2ObjectMapperBuilder builder,
                                                       RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = RedisConfigUtils.buildRedisObjectMapper(builder);

        // 添加Security提供的Jackson Mixin
        objectMapper.registerModule(new CoreJackson2Module());

        // 存入redis时序列化值的序列化器
        Jackson2JsonRedisSerializer<Object> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        return RedisConfigUtils.buildRedisTemplate(connectionFactory, valueSerializer);
    }

}
