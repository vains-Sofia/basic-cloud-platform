package com.basic.cloud.oauth2.authorization.manager;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 同时支持匿名token与jwt token解析配置
 * TODO 待完善
 *
 * @author vains
 */
public class DelegatingTokenAuthenticationResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private final OpaqueTokenIntrospector opaqueTokenIntrospector;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public DelegatingTokenAuthenticationResolver(OpaqueTokenIntrospector opaqueTokenIntrospector, ApplicationContext applicationContext) {
        this.opaqueTokenIntrospector = opaqueTokenIntrospector;
        JwtAuthenticationProvider authenticationProvider = this.getOptionalBean(applicationContext, JwtAuthenticationProvider.class);
        if (ObjectUtils.isEmpty(authenticationProvider)) {
            JwtDecoder jwtDecoder = this.getOptionalBean(applicationContext, JwtDecoder.class);
            Assert.notNull(jwtDecoder, "jwtDecoder cannot be null");
            this.jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        } else {
            this.jwtAuthenticationProvider = authenticationProvider;
        }

        JwtAuthenticationConverter authenticationConverter = this.getOptionalBean(applicationContext, JwtAuthenticationConverter.class);
        if (authenticationConverter != null) {
            this.jwtAuthenticationProvider.setJwtAuthenticationConverter(authenticationConverter);
        }
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest context) {
        AuthenticationManager jwt = new ProviderManager(jwtAuthenticationProvider);
        AuthenticationManager opaqueToken = new ProviderManager(
                new OpaqueTokenAuthenticationProvider(opaqueTokenIntrospector));
        return useJwt(context) ? jwt : opaqueToken;
    }

    /**
     * 判断请求头是否有key ： token-type，有值不是jwt
     * 这里根据自己业务实现，可以获取token后再判断token是jwt还是匿名token
     *
     * @param request 请求对象
     * @return 是否使用jwt token
     */
    private boolean useJwt(HttpServletRequest request) {
        return ObjectUtils.isEmpty(request.getHeader("token-type"));
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
