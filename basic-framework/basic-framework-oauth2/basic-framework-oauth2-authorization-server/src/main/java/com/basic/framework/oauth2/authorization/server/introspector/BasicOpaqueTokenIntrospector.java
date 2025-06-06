package com.basic.framework.oauth2.authorization.server.introspector;

import com.basic.framework.oauth2.core.customizer.BasicIdTokenCustomizer;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证服务令牌自省
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class BasicOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final BasicIdTokenCustomizer idTokenCustomizer;

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

        Collection<? extends GrantedAuthority> grantedAuthorities;
        // 授权过的scope
        Set<String> authorizedScopes = authorization.getAuthorizedScopes();
        if (!ObjectUtils.isEmpty(authorizedScopes)) {
            grantedAuthorities = authorizedScopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        } else {
            // 如果没有scope，则使用默认的权限
            grantedAuthorities = Collections.emptySet();
        }

        Object attribute = authorization.getAttribute(Principal.class.getName());
        if (attribute instanceof AbstractAuthenticationToken authenticationToken) {
            if (authenticationToken.getPrincipal() instanceof User user) {
                return new DefaultAuthenticatedUser(user.getUsername(), OAuth2AccountPlatformEnum.SYSTEM, grantedAuthorities);
            }
            if (authenticationToken.getPrincipal() instanceof AuthenticatedUser user) {
                if (!ObjectUtils.isEmpty(grantedAuthorities)) {
                    // 动态从redis获取SCOPE对应的权限给用户
                    idTokenCustomizer.transferScopesAuthorities(user, authorizedScopes, grantedAuthorities);
                }
                return user;
            }
        }

        return null;
    }
}
