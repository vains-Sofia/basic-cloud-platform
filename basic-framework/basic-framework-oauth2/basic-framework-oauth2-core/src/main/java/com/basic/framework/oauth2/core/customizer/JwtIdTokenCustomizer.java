package com.basic.framework.oauth2.core.customizer;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

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

    private final BasicIdTokenCustomizer idTokenCustomizer;

    private final RedisOperator<AuthenticatedUser> redisOperator;

    @Override
    public void customize(JwtEncodingContext context) {
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            Map<String, Object> payloadClaims = idTokenCustomizer.extractClaims(context.getPrincipal());
            context.getClaims().claims(existingClaims -> {
                // Remove conflicting claims set by this authorization server
                existingClaims.keySet().forEach(payloadClaims::remove);

                // Remove standard id_token claims that could cause problems with clients
                BasicIdTokenCustomizer.ID_TOKEN_CLAIMS.forEach(payloadClaims::remove);

                // Add all other claims directly to id_token
                existingClaims.putAll(payloadClaims);
            });
        }

        // 检查登录用户信息是不是OAuth2User，在token中添加loginType属性
        if (context.getPrincipal().getPrincipal() instanceof AuthenticatedUser user) {
            // 只在生成access token时操作
            if (OAuth2ParameterNames.ACCESS_TOKEN.equals(context.getTokenType().getValue())) {
                // id token不缓存用户信息
                JwtClaimsSet.Builder claims = context.getClaims();
                JwtClaimsSet claimsSet = claims.build();
                // 获取jwt的id(jti)
                String jti = claimsSet.getId();
                // 计算token有效时长
                long expire = ChronoUnit.SECONDS.between(claimsSet.getIssuedAt(), claimsSet.getExpiresAt());
                log.debug("Jwt的id为：{}", jti);
                // 授权确认的scope
                Set<String> authorizedScopes = context.getAuthorizedScopes();
                idTokenCustomizer.transferScopesAuthorities(user, authorizedScopes);
                redisOperator.set((AuthorizeConstants.USERINFO_PREFIX + jti), user, expire);
            }
        }
    }

}
