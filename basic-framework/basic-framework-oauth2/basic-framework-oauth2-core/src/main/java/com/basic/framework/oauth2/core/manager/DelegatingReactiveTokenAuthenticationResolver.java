package com.basic.framework.oauth2.core.manager;

import com.basic.framework.oauth2.core.converter.BasicJwtRedisAuthenticationConverter;
import com.basic.framework.oauth2.core.converter.BasicReactiveOpaqueTokenConverter;
import com.nimbusds.jwt.JWTParser;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.oauth2.server.resource.introspection.SpringReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.server.authentication.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;

/**
 * 同时支持匿名token与jwt token解析配置(webflux)
 *
 * @author vains
 */
public class DelegatingReactiveTokenAuthenticationResolver implements ReactiveAuthenticationManagerResolver<ServerWebExchange>, BasicTokenAuthenticationResolver {

    private final JwtReactiveAuthenticationManager jwtAuthenticationManager;

    private final SpringReactiveOpaqueTokenIntrospector opaqueTokenIntrospector;

    /**
     * 从请求中获取bearer token并转为Authentication
     */
    private final ServerAuthenticationConverter authenticationConverter = new ServerBearerTokenAuthenticationConverter();

    /**
     * 实例化时自动从ioc中获取需要的bean并初始化
     *
     * @param applicationContext 容器对象
     */
    public DelegatingReactiveTokenAuthenticationResolver(ApplicationContext applicationContext) {
        // 获取spring提供的匿名token自省实现
        this.opaqueTokenIntrospector = this.getOptionalBean(applicationContext, SpringReactiveOpaqueTokenIntrospector.class);
        if (!ObjectUtils.isEmpty(this.opaqueTokenIntrospector)) {
            // 设置自定义Authority解析配置
            this.opaqueTokenIntrospector.setAuthenticationConverter(new BasicReactiveOpaqueTokenConverter());
        }
        // 从ioc中获取jwt token解析manager
        JwtReactiveAuthenticationManager jwtAuthenticationManager = this.getOptionalBean(applicationContext, JwtReactiveAuthenticationManager.class);
        if (ObjectUtils.isEmpty(jwtAuthenticationManager)) {
            // 从ioc中获取jwt解析器(需配置spring.security.oauth2.resourceserver.jwt.issuer-uri配置项)
            ReactiveJwtDecoder jwtDecoder = this.getOptionalBean(applicationContext, ReactiveJwtDecoder.class);
            Assert.notNull(jwtDecoder, "jwtDecoder cannot be null");
            // 获取不到默认初始化一个
            this.jwtAuthenticationManager = new JwtReactiveAuthenticationManager(jwtDecoder);
        } else {
            // 获取到直接初始化
            this.jwtAuthenticationManager = jwtAuthenticationManager;
        }

        // 获取jwt转换器，获取到通过适配器给webflux的jwt解析manager使用
        BasicJwtRedisAuthenticationConverter authenticationConverter = this.getOptionalBean(applicationContext, BasicJwtRedisAuthenticationConverter.class);
        if (authenticationConverter != null) {
            this.jwtAuthenticationManager.setJwtAuthenticationConverter(new ReactiveJwtAuthenticationConverterAdapter(authenticationConverter));
        }
    }

    @Override
    public Mono<ReactiveAuthenticationManager> resolve(ServerWebExchange context) {
        // 如果没有匿名token自省类默认使用jwt解析器
        Mono<ReactiveAuthenticationManager> jwtMono = Mono.just(jwtAuthenticationManager);
        if (this.opaqueTokenIntrospector == null) {
            return jwtMono;
        }
        OpaqueTokenReactiveAuthenticationManager opaqueTokenManager = new OpaqueTokenReactiveAuthenticationManager(opaqueTokenIntrospector);
        // 根据access token类型决定使用哪一种解析器
        return authenticationConverter.convert(context)
                // 只处理BearerTokenAuthenticationToken
                .filter(BearerTokenAuthenticationToken.class::isInstance)
                // 转换
                .cast(BearerTokenAuthenticationToken.class)
                .map(authenticationToken -> {
                    String token = authenticationToken.getToken();
                    String[] split = token.split("\\.");
                    // 如果不符合jwt格式使用匿名token自省
                    if (split.length != 3) {
                        return opaqueTokenManager;
                    }
                    try {
                        // 可以正确解析jwt则使用jwt解析器
                        JWTParser.parse(token);
                        return jwtAuthenticationManager;
                    } catch (ParseException e) {
                        // 解析失败尝试自省 access token
                        return opaqueTokenManager;
                    }
                });
    }

}
