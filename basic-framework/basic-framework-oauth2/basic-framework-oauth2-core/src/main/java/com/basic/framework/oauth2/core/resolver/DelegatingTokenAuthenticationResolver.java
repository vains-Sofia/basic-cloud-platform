package com.basic.framework.oauth2.core.resolver;

import com.basic.framework.oauth2.core.converter.BasicJwtRedisAuthenticationConverter;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;

/**
 * 同时支持匿名token与jwt token解析配置
 *
 * @author vains
 */
public class DelegatingTokenAuthenticationResolver implements AuthenticationManagerResolver<HttpServletRequest>, BasicTokenAuthenticationResolver {

    private final OpaqueTokenIntrospector opaqueTokenIntrospector;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * 从请求中获取bearer token的处理器
     */
    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();

    /**
     * 实例化时自动从ioc中获取需要的bean并初始化
     *
     * @param applicationContext 容器对象
     */
    public DelegatingTokenAuthenticationResolver(ApplicationContext applicationContext) {
        // 获取spring提供的匿名token自省实现
        this.opaqueTokenIntrospector = this.getOptionalBean(applicationContext, OpaqueTokenIntrospector.class);
        // 从ioc中获取jwt token解析manager
        JwtAuthenticationProvider authenticationProvider = this.getOptionalBean(applicationContext, JwtAuthenticationProvider.class);
        if (ObjectUtils.isEmpty(authenticationProvider)) {
            // 从ioc中获取jwt解析器(需配置spring.security.oauth2.resourceserver.jwt.issuer-uri配置项)
            JwtDecoder jwtDecoder = this.getOptionalBean(applicationContext, JwtDecoder.class);
            Assert.notNull(jwtDecoder, "jwtDecoder cannot be null");
            // 获取不到默认初始化一个
            this.jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        } else {
            // 获取到直接初始化
            this.jwtAuthenticationProvider = authenticationProvider;
        }

        // 获取jwt转换器，获取到直接给转换器使用
        BasicJwtRedisAuthenticationConverter authenticationConverter = this.getOptionalBean(applicationContext, BasicJwtRedisAuthenticationConverter.class);
        if (authenticationConverter != null) {
            this.jwtAuthenticationProvider.setJwtAuthenticationConverter(authenticationConverter);
        }
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest context) {
        // 如果没有匿名token自省类默认使用jwt解析器
        AuthenticationManager jwt = new ProviderManager(jwtAuthenticationProvider);
        if (this.opaqueTokenIntrospector == null) {
            return jwt;
        }
        // 根据access token类型决定使用哪一种解析器
        AuthenticationManager opaqueToken = new ProviderManager(
                new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector));

        // 到这里肯定会有access token
        String token = bearerTokenResolver.resolve(context);
        String[] split = token.split("\\.");
        if (split.length == 3) {
            try {
                // 可以正确解析jwt则使用jwt解析器
                JWTParser.parse(token);
                return jwt;
            } catch (ParseException e) {
                // 解析失败尝试自省 access token
                return opaqueToken;
            }
        } else {
            // 如果不符合jwt格式使用匿名token自省
            return opaqueToken;
        }
    }

}
