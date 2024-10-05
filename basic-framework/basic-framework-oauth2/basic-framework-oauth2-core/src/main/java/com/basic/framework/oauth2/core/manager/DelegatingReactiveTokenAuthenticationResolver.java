package com.basic.framework.oauth2.core.manager;

import com.basic.framework.oauth2.core.converter.BasicReactiveOpaqueTokenConverter;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.oauth2.server.resource.introspection.SpringReactiveOpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 同时支持匿名token与jwt token解析配置(webflux)
 *
 * @author vains
 */
public class DelegatingReactiveTokenAuthenticationResolver implements ReactiveAuthenticationManagerResolver<ServerWebExchange> {

    private final JwtReactiveAuthenticationManager jwtAuthenticationManager;

    private final SpringReactiveOpaqueTokenIntrospector opaqueTokenIntrospector;

    public DelegatingReactiveTokenAuthenticationResolver(ApplicationContext applicationContext) {
        this.opaqueTokenIntrospector = this.getOptionalBean(applicationContext, SpringReactiveOpaqueTokenIntrospector.class);
        if (!ObjectUtils.isEmpty(this.opaqueTokenIntrospector)) {
            this.opaqueTokenIntrospector.setAuthenticationConverter(new BasicReactiveOpaqueTokenConverter());
        }
        JwtReactiveAuthenticationManager jwtAuthenticationManager = this.getOptionalBean(applicationContext, JwtReactiveAuthenticationManager.class);
        if (ObjectUtils.isEmpty(jwtAuthenticationManager)) {
            ReactiveJwtDecoder jwtDecoder = this.getOptionalBean(applicationContext, ReactiveJwtDecoder.class);
            Assert.notNull(jwtDecoder, "jwtDecoder cannot be null");
            this.jwtAuthenticationManager = new JwtReactiveAuthenticationManager(jwtDecoder);
        } else {
            this.jwtAuthenticationManager = jwtAuthenticationManager;
        }

        JwtAuthenticationConverter authenticationConverter = this.getOptionalBean(applicationContext, JwtAuthenticationConverter.class);
        if (authenticationConverter != null) {
            this.jwtAuthenticationManager.setJwtAuthenticationConverter(new ReactiveJwtAuthenticationConverterAdapter(authenticationConverter));
        }
    }

    @Override
    public Mono<ReactiveAuthenticationManager> resolve(ServerWebExchange context) {
        Mono<ReactiveAuthenticationManager> jwtMono = Mono.just(jwtAuthenticationManager);
        if (this.opaqueTokenIntrospector == null) {
            return jwtMono;
        }
        OpaqueTokenReactiveAuthenticationManager opaqueTokenManager = new OpaqueTokenReactiveAuthenticationManager(opaqueTokenIntrospector);
        return useJwt(context) ? jwtMono : Mono.just(opaqueTokenManager);
    }

    /**
     * 判断请求头是否有key ： token-type，有值不是jwt
     * 这里根据自己业务实现，可以获取token后再判断token是jwt还是匿名token
     * TODO 待完善
     *
     * @param exchange 请求对象
     * @return 是否使用jwt token
     */
    private boolean useJwt(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        return ObjectUtils.isEmpty(headers.get("token-type"));
    }

    private <T> T getOptionalBean(ApplicationContext applicationContext, Class<T> type) {
        Map<String, T> beansMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, type);
        if (beansMap.size() > 1) {
            throw new NoUniqueBeanDefinitionException(type, beansMap.size(),
                    "Expected single matching bean of type '" + type.getName() + "' but found " +
                            beansMap.size() + ": " + StringUtils.collectionToCommaDelimitedString(beansMap.keySet()));
        }
        return (!beansMap.isEmpty() ? beansMap.values().iterator().next() : null);
    }

}
