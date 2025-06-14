package com.basic.framework.oauth2.core.introspection;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.customizer.BasicIdTokenCustomizer;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.redis.support.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.ACTIVE;
import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.JTI;

/**
 * 自定义的不透明令牌内省器
 * 内部使用 SpringOpaqueTokenIntrospector 并将结果转换为 AuthenticatedUser
 *
 * @author vains
 */
@Slf4j
public class ResourceOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final RedisOperator<Long> redisHashOperator;

    private final BasicIdTokenCustomizer idTokenCustomizer;

    private final RedisOperator<AuthenticatedUser> redisOperator;

    private final SpringOpaqueTokenIntrospector springOpaqueTokenIntrospector;

    public ResourceOpaqueTokenIntrospector(RedisOperator<Long> redisHashOperator,
                                           BasicIdTokenCustomizer idTokenCustomizer,
                                           RedisOperator<AuthenticatedUser> redisOperator,
                                           OAuth2ResourceServerProperties resourceServerProperties) {
        this.idTokenCustomizer = idTokenCustomizer;
        this.redisOperator = redisOperator;
        this.redisHashOperator = redisHashOperator;
        OAuth2ResourceServerProperties.Opaquetoken opaquetoken = resourceServerProperties.getOpaquetoken();
        this.springOpaqueTokenIntrospector = new SpringOpaqueTokenIntrospector(
                opaquetoken.getIntrospectionUri(),
                opaquetoken.getClientId(),
                opaquetoken.getClientSecret());
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        // 使用 SpringOpaqueTokenIntrospector 进行令牌内省
        OAuth2AuthenticatedPrincipal principal = springOpaqueTokenIntrospector.introspect(token);

        // 将结果转换为项目中的 AuthenticatedUser
        if (principal == null || principal.getAttribute(ACTIVE) == null || !Boolean.TRUE.equals(principal.getAttribute(ACTIVE))) {
            return principal;
        }

        // 获取基本信息
        Map<String, Object> attributes = principal.getAttributes();
        Collection<? extends GrantedAuthority> grantedAuthorities;

        // 从 attributes 中提取用户信息
        if (attributes != null) {

            // 获取用户的id
            Long userId = redisHashOperator.getHash(AuthorizeConstants.JTI_USER_HASH, principal.getAttribute(JTI), Long.class);
            AuthenticatedUser authenticatedUser = redisOperator.get(AuthorizeConstants.USERINFO_PREFIX + userId);
            if (authenticatedUser == null) {
                // 客户端模式
                Boolean isClientCredentials = (Boolean) attributes.getOrDefault(AuthorizeConstants.IS_CLIENT_CREDENTIALS, Boolean.FALSE);
                if (isClientCredentials != null && isClientCredentials) {
                    if (log.isDebugEnabled()) {
                        log.debug("Client credentials mode detected, returning original principal: {}", principal);
                    }
                    return principal;
                }

                BearerTokenError bearerTokenError = BearerTokenErrors.invalidToken("Access token is invalid or has been logged out.");
                throw new OAuth2AuthenticationException(bearerTokenError);
            }

            // 解析scope
            Object o = attributes.get(OAuth2TokenIntrospectionClaimNames.SCOPE);
            if (o instanceof List<?> scopes) {
                grantedAuthorities = scopes.stream().filter(String.class::isInstance).map(String::valueOf).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            } else {
                grantedAuthorities = Collections.emptyList();
            }

            if (!ObjectUtils.isEmpty(grantedAuthorities)) {
                Set<String> authorizedScopes = grantedAuthorities
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                // 动态从redis获取SCOPE对应的权限给用户
                idTokenCustomizer.transferScopesAuthorities(authenticatedUser, authorizedScopes, grantedAuthorities);
            }

            if (log.isDebugEnabled()) {
                log.debug("转换令牌内省结果为 AuthenticatedUser: {}", authenticatedUser);
            }

            return authenticatedUser;
        } else {
            return principal;
        }
    }

}