package com.basic.framework.oauth2.resource.server.autoconfigure;

import com.basic.framework.oauth2.core.converter.BasicJwtRedisAuthenticationConverter;
import com.basic.framework.oauth2.core.customizer.BasicIdTokenCustomizer;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.security.BasicGrantedAuthority;
import com.basic.framework.oauth2.core.domain.security.ScopePermissionModel;
import com.basic.framework.oauth2.core.manager.ReactiveContextAuthorizationManager;
import com.basic.framework.oauth2.core.manager.RequestContextAuthorizationManager;
import com.basic.framework.oauth2.core.property.ResourceServerProperties;
import com.basic.framework.oauth2.resource.server.configure.ReactiveResourceServerConfiguration;
import com.basic.framework.oauth2.resource.server.configure.ResourceServerConfiguration;
import com.basic.framework.oauth2.core.introspection.ResourceOpaqueTokenIntrospector;
import com.basic.framework.redis.support.RedisOperator;
import com.basic.framework.redis.util.RedisConfigUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.jackson2.CoreJackson2Module;

import java.util.List;
import java.util.Map;

/**
 * 资源服务自动配置类
 *
 * @author vains
 */
@Slf4j

@Import({
        ResourceServerConfiguration.class,
        ReactiveResourceServerConfiguration.class
})
@RequiredArgsConstructor
@EnableConfigurationProperties({ResourceServerProperties.class})
public class ResourceServerAutoConfiguration {

    private final ResourceServerProperties resourceServerProperties;

    @Bean
    @ConditionalOnMissingBean
    public RequestContextAuthorizationManager requestContextAuthorizationManager(
            RedisOperator<Map<String, List<BasicGrantedAuthority>>> permissionRedisOperator
    ) {
        if (log.isDebugEnabled()) {
            log.debug("注入 基于Webmvc的自定义鉴权配置类 RequestContextAuthorizationManager.");
        }
        return new RequestContextAuthorizationManager(resourceServerProperties, permissionRedisOperator);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReactiveContextAuthorizationManager reactiveContextAuthorizationManager(
            RedisOperator<Map<String, List<BasicGrantedAuthority>>> permissionRedisOperator
    ) {
        if (log.isDebugEnabled()) {
            log.debug("注入 基于Webflux的自定义鉴权配置类 ReactiveContextAuthorizationManager.");
        }
        return new ReactiveContextAuthorizationManager(resourceServerProperties, permissionRedisOperator);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicJwtRedisAuthenticationConverter authenticationConverter(BasicIdTokenCustomizer idTokenCustomizer,
                                                                        RedisOperator<AuthenticatedUser> redisOperator) {
        if (log.isDebugEnabled()) {
            log.debug("注入 从redis中根据jwt获取认证信息的Jwt token解析器.");
        }
        return new BasicJwtRedisAuthenticationConverter(idTokenCustomizer, redisOperator);
    }

    /**
     * 资源服务器的RedisTemplate，与认证服务使用的配置基本一致
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

        // 存入redis时序列化值的序列化器
        Jackson2JsonRedisSerializer<Object> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        return RedisConfigUtils.buildRedisTemplate(connectionFactory, valueSerializer);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicIdTokenCustomizer basicIdTokenCustomizer(
            RedisOperator<List<ScopePermissionModel>> scopePermissionOperator,
            RedisOperator<Map<String, List<BasicGrantedAuthority>>> permissionRedisOperator) {
        if (log.isDebugEnabled()) {
            log.debug("注入 自定义token内容帮助类 BasicIdTokenCustomizer.");
        }
        return new BasicIdTokenCustomizer(scopePermissionOperator, permissionRedisOperator);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceOpaqueTokenIntrospector resourceOpaqueTokenIntrospector(BasicIdTokenCustomizer idTokenCustomizer,
                                                                           RedisOperator<AuthenticatedUser> redisOperator,
                                                                           OAuth2ResourceServerProperties resourceServerProperties) {
        if (resourceServerProperties.getOpaquetoken() == null) {
            if (log.isDebugEnabled()) {
                log.debug("未配置Opaque Token相关属性，无法注入 ResourceOpaqueTokenIntrospector.");
            }
            return null;
        }
        // 资源服务器的OpaqueTokenIntrospector实现
        if (log.isDebugEnabled()) {
            log.debug("注入 自定义的Opaque Token自省实现 ResourceOpaqueTokenIntrospector.");
        }
        return new ResourceOpaqueTokenIntrospector(idTokenCustomizer, redisOperator, resourceServerProperties);
    }

    @PostConstruct
    public void postConstruct() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing Resource Server Auto Configuration.");
        }
    }

}
