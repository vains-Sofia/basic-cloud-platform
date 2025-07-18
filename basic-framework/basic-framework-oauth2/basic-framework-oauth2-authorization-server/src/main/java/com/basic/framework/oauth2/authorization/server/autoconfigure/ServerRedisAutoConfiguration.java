package com.basic.framework.oauth2.authorization.server.autoconfigure;

import com.basic.framework.redis.util.RedisConfigUtils;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import java.util.List;

/**
 * redis配置
 *
 * @author vains
 */
public class ServerRedisAutoConfiguration {

    /**
     * 默认情况下使用
     *
     * @param connectionFactory redis链接工厂
     * @return RedisTemplate
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<Object, Object> redisTemplate(Jackson2ObjectMapperBuilder builder,
                                                       RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = RedisConfigUtils.buildRedisObjectMapper(builder);

        // 添加Security提供的Jackson Mixin
        objectMapper.registerModule(new CoreJackson2Module());
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        // 初始化序列化配置
        ClassLoader classLoader = ServerRedisAutoConfiguration.class.getClassLoader();
        // 加载security提供的Modules
        List<Module> modules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(modules);

        // 存入redis时序列化值的序列化器
        Jackson2JsonRedisSerializer<Object> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        return RedisConfigUtils.buildRedisTemplate(connectionFactory, valueSerializer);
    }

}
