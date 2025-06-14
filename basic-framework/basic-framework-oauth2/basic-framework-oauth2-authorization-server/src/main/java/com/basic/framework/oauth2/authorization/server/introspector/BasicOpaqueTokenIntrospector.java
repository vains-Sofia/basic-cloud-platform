package com.basic.framework.oauth2.authorization.server.introspector;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.customizer.BasicIdTokenCustomizer;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.JTI;

/**
 * 认证服务令牌自省
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class BasicOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final RedisOperator<Long> redisHashOperator;

    private final BasicIdTokenCustomizer idTokenCustomizer;

    private final RedisOperator<AuthenticatedUser> redisOperator;

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

        Collection<GrantedAuthority> grantedAuthorities;
        // 授权过的scope
        Set<String> authorizedScopes = authorization.getAuthorizedScopes();
        if (!ObjectUtils.isEmpty(authorizedScopes)) {
            grantedAuthorities = authorizedScopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        } else {
            // 如果没有scope，则使用默认的权限
            grantedAuthorities = Collections.emptySet();
        }

        // 尝试获取用户的信息
        Map<String, Object> claims = authorizedToken.getClaims();
        if (ObjectUtils.isEmpty(claims)) {
            if (log.isTraceEnabled()) {
                log.trace("Did not introspect token since no claims found");
            }
            throw new InvalidBearerTokenException("Did not introspect token since no claims found.");
        }
        String jti = claims.get(JTI) + "";
        Long userId = redisHashOperator.getHash(AuthorizeConstants.JTI_USER_HASH, jti, Long.class);
        // 从Redis中获取用户信息
        AuthenticatedUser authenticatedUser = redisOperator.get(AuthorizeConstants.USERINFO_PREFIX + userId);
        if (userId == null || authenticatedUser == null) {
            // 如果用户信息不存在，可能是客户端模式
            Boolean isClientCredentials = (Boolean) claims.getOrDefault(AuthorizeConstants.IS_CLIENT_CREDENTIALS, Boolean.FALSE);
            // 客户端模式
            if (isClientCredentials != null && isClientCredentials) {
                return new DefaultOAuth2AuthenticatedPrincipal(claims, grantedAuthorities);
            }

            // token 被正常解析但是无法获取到Redis的用户信息，这种情况一般是登出、管理平台下线后出现的问题
            throw new InvalidBearerTokenException("Access token is invalid or has been logged out.");
        }

        if (!ObjectUtils.isEmpty(grantedAuthorities)) {
            // 动态从redis获取SCOPE对应的权限给用户
            idTokenCustomizer.transferScopesAuthorities(authenticatedUser, authorizedScopes, grantedAuthorities);
        }
        return authenticatedUser;
    }
}
