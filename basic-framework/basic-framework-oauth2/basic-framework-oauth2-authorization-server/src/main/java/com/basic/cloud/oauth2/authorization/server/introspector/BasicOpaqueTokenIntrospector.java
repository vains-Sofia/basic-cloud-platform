package com.basic.cloud.oauth2.authorization.server.introspector;

import com.basic.cloud.oauth2.authorization.domain.AuthenticatedUser;
import com.basic.cloud.oauth2.authorization.domain.DefaultAuthenticatedUser;
import com.basic.cloud.oauth2.authorization.enums.OAuth2AccountPlatformEnum;
import com.basic.cloud.oauth2.authorization.server.core.AbstractLoginAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.ObjectUtils;

import java.security.Principal;

/**
 * 认证服务令牌自省
 *  TODO 待完善远程解析
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class BasicOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OAuth2ResourceServerProperties properties;

    private final OAuth2AuthorizationService authorizationService;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        if (properties != null && (!ObjectUtils.isEmpty(properties.getOpaquetoken().getIntrospectionUri()) || !ObjectUtils.isEmpty(properties.getJwt().getIssuerUri()))) {
            // 远程令牌自省
            return null;
        }
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

        Object attribute = authorization.getAttribute(Principal.class.getName());
        if (attribute instanceof UsernamePasswordAuthenticationToken authenticationToken) {
            if (authenticationToken.getPrincipal() instanceof User user) {
                return new DefaultAuthenticatedUser(user.getUsername(), OAuth2AccountPlatformEnum.SYSTEM, user.getAuthorities());
            }
            if (authenticationToken.getPrincipal() instanceof AuthenticatedUser user) {
                return user;
            }
        }

        if (attribute instanceof AbstractLoginAuthenticationToken authenticationToken) {
            if (authenticationToken.getPrincipal() instanceof AuthenticatedUser user) {
                return user;
            }
        }

        return null;
    }
}
