package com.basic.framework.oauth2.storage.core.converter;

import com.basic.framework.oauth2.authorization.server.core.BasicCoreServiceConverter;
import com.basic.framework.oauth2.authorization.server.util.OAuth2JsonUtils;
import com.basic.framework.oauth2.storage.core.domain.BasicAuthorization;
import jakarta.annotation.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.util.StringUtils;

/**
 * 框架认证信息转实体
 *
 * @author vains
 */
public class OAuth2Authorization2AuthorizationConverter implements BasicCoreServiceConverter<OAuth2Authorization, BasicAuthorization> {

    @Override
    public BasicAuthorization convert(@Nullable OAuth2Authorization source) {
        if (source == null) {
            return null;
        }
        BasicAuthorization authorization = new BasicAuthorization();
        authorization.setId(source.getId());
        authorization.setRegisteredClientId(source.getRegisteredClientId());
        authorization.setPrincipalName(source.getPrincipalName());
        authorization.setAuthorizationGrantType(source.getAuthorizationGrantType().getValue());
        authorization.setAuthorizedScopes(source.getAuthorizedScopes());
        authorization.setAttributes(OAuth2JsonUtils.toJson(source.getAttributes()));
        authorization.setState(source.getAttribute(OAuth2ParameterNames.STATE));

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                source.getToken(OAuth2AuthorizationCode.class);
        setTokenValues(authorizationCode,
                authorization::setAuthorizationCodeValue,
                authorization::setAuthorizationCodeIssuedAt,
                authorization::setAuthorizationCodeExpiresAt,
                authorization::setAuthorizationCodeMetadata);

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                source.getToken(OAuth2AccessToken.class);
        setTokenValues(
                accessToken,
                authorization::setAccessTokenValue,
                authorization::setAccessTokenIssuedAt,
                authorization::setAccessTokenExpiresAt,
                authorization::setAccessTokenMetadata
        );

        if (accessToken != null) {
            if (accessToken.getToken().getScopes() != null) {
                authorization.setAccessTokenScopes(StringUtils.collectionToDelimitedString(accessToken.getToken().getScopes(), ","));
            }

            if (accessToken.getToken().getTokenType() != null) {
                authorization.setAccessTokenType(accessToken.getToken().getTokenType().getValue());
            }
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                source.getToken(OAuth2RefreshToken.class);
        setTokenValues(
                refreshToken,
                authorization::setRefreshTokenValue,
                authorization::setRefreshTokenIssuedAt,
                authorization::setRefreshTokenExpiresAt,
                authorization::setRefreshTokenMetadata
        );

        OAuth2Authorization.Token<OidcIdToken> oidcIdToken =
                source.getToken(OidcIdToken.class);
        setTokenValues(
                oidcIdToken,
                authorization::setOidcIdTokenValue,
                authorization::setOidcIdTokenIssuedAt,
                authorization::setOidcIdTokenExpiresAt,
                authorization::setOidcIdTokenMetadata
        );
        if (oidcIdToken != null) {
            authorization.setOidcIdTokenClaims(OAuth2JsonUtils.toJson(oidcIdToken.getClaims()));
        }

        OAuth2Authorization.Token<OAuth2UserCode> userCode =
                source.getToken(OAuth2UserCode.class);
        setTokenValues(
                userCode,
                authorization::setUserCodeValue,
                authorization::setUserCodeIssuedAt,
                authorization::setUserCodeExpiresAt,
                authorization::setUserCodeMetadata
        );

        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
                source.getToken(OAuth2DeviceCode.class);
        setTokenValues(
                deviceCode,
                authorization::setDeviceCodeValue,
                authorization::setDeviceCodeIssuedAt,
                authorization::setDeviceCodeExpiresAt,
                authorization::setDeviceCodeMetadata
        );
        return authorization;
    }
}
