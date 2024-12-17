package com.basic.framework.oauth2.authorization.server.introspector;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import com.basic.framework.core.enums.OAuth2AccountPlatformEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 认证服务令牌自省
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class BasicOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OAuth2AuthorizationService authorizationService;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        // 本地令牌自省
        OAuth2Authorization authorization = this.authorizationService.findByToken(token, (null));
        if (authorization == null) {
            if (log.isTraceEnabled()) {
                log.trace("No authorization found for token {}", token);
            }
            // Return the authentication request when token not found
            throw new InvalidBearerTokenException("No authorization found for token");
        }

        if (log.isTraceEnabled()) {
            log.trace("Retrieved authorization with token");
        }

        OAuth2Authorization.Token<OAuth2Token> authorizedToken = authorization.getToken(token);
        if (authorizedToken == null) {
            if (log.isTraceEnabled()) {
                log.trace("Did not authenticate token introspection request since token was not found");
            }
            // Return the authentication request when token not found
            throw new InvalidBearerTokenException("No authorization found for token");
        }

        if (!authorizedToken.isActive()) {
            if (log.isTraceEnabled()) {
                log.trace("Did not introspect token since not active");
            }
            throw new InvalidBearerTokenException("Did not introspect token since not active.");
        }

        Collection<String> stringAuthorities = Collections.emptyList();
        Map<String, Object> claims = authorizedToken.getClaims();
        if (!ObjectUtils.isEmpty(claims) && claims.containsKey(AuthorizeConstants.AUTHORITIES)) {
            Object authorities = claims.get(AuthorizeConstants.AUTHORITIES);
            if (authorities instanceof String) {
                if (StringUtils.hasText((String) authorities)) {
                    stringAuthorities = Arrays.asList(((String) authorities).split(" "));
                }
            }
            if (authorities instanceof Collection<?> objs) {
                stringAuthorities = objs.stream().map(String::valueOf).collect(Collectors.toSet());
            }
        }

        Set<SimpleGrantedAuthority> authorities = stringAuthorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        Object attribute = authorization.getAttribute(Principal.class.getName());
        if (attribute instanceof AbstractAuthenticationToken authenticationToken) {
            if (authenticationToken.getPrincipal() instanceof User user) {
                return new DefaultAuthenticatedUser(user.getUsername(), OAuth2AccountPlatformEnum.SYSTEM, authorities);
            }
            if (authenticationToken.getPrincipal() instanceof AuthenticatedUser user) {
                user.setAuthorities(authorities);
                return user;
            }
        }

        return null;
    }
}
