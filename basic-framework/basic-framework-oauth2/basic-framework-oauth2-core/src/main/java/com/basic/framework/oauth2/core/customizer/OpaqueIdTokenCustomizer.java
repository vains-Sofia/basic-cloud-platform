package com.basic.framework.oauth2.core.customizer;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * An {@link OAuth2TokenCustomizer} to map claims from a federated identity to
 * the {@code id_token} produced by this authorization server.
 * opaque token.
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public final class OpaqueIdTokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    private final RedisOperator<Long> redisHashOperator;

    private final RedisOperator<AuthenticatedUser> redisOperator;

    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        OAuth2TokenClaimsSet.Builder claims = context.getClaims();
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            Map<String, Object> thirdPartyClaims = BasicIdTokenCustomizer.extractClaims(context.getPrincipal());
            claims.claims(existingClaims -> {
                // Remove conflicting claims set by this authorization server
                existingClaims.keySet().forEach(thirdPartyClaims::remove);

                // Remove standard id_token claims that could cause problems with clients
                BasicIdTokenCustomizer.ID_TOKEN_CLAIMS.forEach(thirdPartyClaims::remove);

                // Add all other claims directly to id_token
                existingClaims.putAll(thirdPartyClaims);
            });
        }

        // 检查登录用户信息是不是OAuth2User，在token中添加loginType属性
        if (context.getPrincipal().getPrincipal() instanceof AuthenticatedUser user) {
            // 只在生成access token时操作
            if (OAuth2ParameterNames.ACCESS_TOKEN.equals(context.getTokenType().getValue())) {
                // 存储用户唯一id
                claims.claim(AuthorizeConstants.USER_ID_KEY, user.getId());
                // 资源服务自省时需要该属性
                claims.claim(OAuth2TokenIntrospectionClaimNames.USERNAME, user.getUsername());
                OAuth2TokenClaimsSet build = claims.build();
                // 计算token有效时长
                long expire = ChronoUnit.SECONDS.between(build.getIssuedAt(), build.getExpiresAt());
                // 获取jti
                String jti = claims.build().getId();
                if (log.isDebugEnabled()) {
                    log.debug("当前用户id为：{}", user.getId());
                    log.debug("认证用户的jti为：{}", jti);
                }
                // token与用户id的映射关系存储到Redis中
                redisHashOperator.setHash(AuthorizeConstants.JTI_USER_HASH, jti, user.getId());
                // 将用户信息存储到Redis中，方便资源服务自省时获取
                redisOperator.set((AuthorizeConstants.USERINFO_PREFIX + user.getId()), user, expire);
            }
        }

        if (context.getPrincipal() instanceof OAuth2ClientAuthenticationToken) {
            // 在access token中标明是客户端模式
            claims.claim(AuthorizeConstants.IS_CLIENT_CREDENTIALS, Boolean.TRUE);
        }
    }

}
