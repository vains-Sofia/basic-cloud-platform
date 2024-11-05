package com.basic.framework.oauth2.core.customizer;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.time.temporal.ChronoUnit;
import java.util.Set;

import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.OAUTH2_ACCESS_TOKEN;
import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.OAUTH2_ACCOUNT_PLATFORM;

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

    private static final Set<String> ID_TOKEN_CLAIMS = Set.of(
            IdTokenClaimNames.ISS,
            IdTokenClaimNames.SUB,
            IdTokenClaimNames.AUD,
            IdTokenClaimNames.EXP,
            IdTokenClaimNames.IAT,
            IdTokenClaimNames.AUTH_TIME,
            IdTokenClaimNames.NONCE,
            IdTokenClaimNames.ACR,
            IdTokenClaimNames.AMR,
            IdTokenClaimNames.AZP,
            IdTokenClaimNames.AT_HASH,
            IdTokenClaimNames.C_HASH,
            OAUTH2_ACCESS_TOKEN,
            OAUTH2_ACCOUNT_PLATFORM
    );

    @Override
    public void customize(JwtEncodingContext context) {

        // 检查登录用户信息是不是OAuth2User，在token中添加loginType属性
        if (context.getPrincipal().getPrincipal() instanceof AuthenticatedUser user) {
            if (!OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                // id token不缓存用户信息
                JwtClaimsSet.Builder claims = context.getClaims();
                JwtClaimsSet claimsSet = claims.build();
                // 获取jwt的id(jti)
                String jti = claimsSet.getId();
                // 计算token有效时长
                long expire = ChronoUnit.SECONDS.between(claimsSet.getIssuedAt(), claimsSet.getExpiresAt());
                log.debug("Jwt的id为：{}", jti);
                redisOperator.set((AuthorizeConstants.USERINFO_PREFIX + jti), user, expire);
            }
        }
    }

}
