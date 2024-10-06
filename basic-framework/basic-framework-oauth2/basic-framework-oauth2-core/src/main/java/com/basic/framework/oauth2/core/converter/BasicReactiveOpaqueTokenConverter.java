package com.basic.framework.oauth2.core.converter;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimAccessor;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义匿名token权限解析(webflux)
 *
 * @author vains
 */
public class BasicReactiveOpaqueTokenConverter implements Converter<OAuth2TokenIntrospectionClaimAccessor, Mono<? extends OAuth2AuthenticatedPrincipal>> {

    @Override
    @Nullable
    public Mono<? extends OAuth2AuthenticatedPrincipal> convert(OAuth2TokenIntrospectionClaimAccessor accessor) {
        Object claim = accessor.getClaims().get(AuthorizeConstants.AUTHORITIES);
        Set<GrantedAuthority> authorities;
        if (claim instanceof List<?> list) {
            authorities = list.stream().map(String::valueOf)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        } else {
            authorities = Collections.emptySet();
        }
        return Mono.just(new OAuth2IntrospectionAuthenticatedPrincipal(accessor.getClaims(), authorities));
    }
}
