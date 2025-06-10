package com.basic.framework.oauth2.core.customizer;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * An {@link OAuth2TokenCustomizer} to map claims from a federated identity to
 * the {@code id_token} produced by this authorization server.
 * Jwt token.
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public final class JwtIdTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final RedisOperator<AuthenticatedUser> redisOperator;

    @Override
    public void customize(JwtEncodingContext context) {
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            Map<String, Object> payloadClaims = BasicIdTokenCustomizer.extractClaims(context.getPrincipal());
            context.getClaims().claims(existingClaims -> {
                // Remove conflicting claims set by this authorization server
                existingClaims.keySet().forEach(payloadClaims::remove);

                // Remove standard id_token claims that could cause problems with clients
                BasicIdTokenCustomizer.ID_TOKEN_CLAIMS.forEach(payloadClaims::remove);

                // Add all other claims directly to id_token
                existingClaims.putAll(payloadClaims);
            });
        }

        // 检查登录用户信息是不是统一用户信息，缓存用户信息至redis
        if (context.getPrincipal().getPrincipal() instanceof AuthenticatedUser user) {
            // 只在生成access token时操作
            if (OAuth2ParameterNames.ACCESS_TOKEN.equals(context.getTokenType().getValue())) {
                // id token不缓存用户信息
                JwtClaimsSet.Builder claims = context.getClaims();
                // 存储用户唯一id
                claims.claim(AuthorizeConstants.USER_ID_KEY, user.getId());
                JwtClaimsSet claimsSet = claims.build();
                // 计算token有效时长
                long expire = ChronoUnit.SECONDS.between(claimsSet.getIssuedAt(), claimsSet.getExpiresAt());
                log.debug("认证用户的id为：{}", user.getId());
                redisOperator.set((AuthorizeConstants.USERINFO_PREFIX + user.getId()), user, expire);
            }
        }

        if (context.getPrincipal() instanceof OAuth2ClientAuthenticationToken) {
            // 在access token中标明是客户端模式
            JwtClaimsSet.Builder claims = context.getClaims();
            claims.claim(AuthorizeConstants.IS_CLIENT_CREDENTIALS, Boolean.TRUE);
        }
    }

}
