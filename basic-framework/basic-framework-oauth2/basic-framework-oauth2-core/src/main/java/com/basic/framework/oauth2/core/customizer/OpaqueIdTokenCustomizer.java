package com.basic.framework.oauth2.core.customizer;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Map;

import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.OAUTH2_ACCOUNT_PLATFORM;
import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.TOKEN_UNIQUE_ID;

/**
 * An {@link OAuth2TokenCustomizer} to map claims from a federated identity to
 * the {@code id_token} produced by this authorization server.
 * opaque token.
 *
 * @author vains
 */
@RequiredArgsConstructor
public final class OpaqueIdTokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

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
            // 存储用户所属平台与用户唯一id
            claims.claim(OAUTH2_ACCOUNT_PLATFORM, user.getAccountPlatform());
            claims.claim(TOKEN_UNIQUE_ID, user.getUsername());
            // 资源服务自省时需要该属性
            claims.claim(OAuth2TokenIntrospectionClaimNames.USERNAME, user.getUsername());
        }

        if (context.getPrincipal() instanceof OAuth2ClientAuthenticationToken) {
            // 在access token中标明是客户端模式
            claims.claim(AuthorizeConstants.IS_CLIENT_CREDENTIALS, Boolean.TRUE);
        }
    }

}
