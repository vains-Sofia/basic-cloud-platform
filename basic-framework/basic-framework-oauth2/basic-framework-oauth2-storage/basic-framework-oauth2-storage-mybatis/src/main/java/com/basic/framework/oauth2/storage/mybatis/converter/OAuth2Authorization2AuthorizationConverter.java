package com.basic.framework.oauth2.storage.mybatis.converter;

import com.basic.framework.oauth2.authorization.server.core.BasicCoreServiceConverter;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2Authorization;
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
public class OAuth2Authorization2AuthorizationConverter implements BasicCoreServiceConverter<OAuth2Authorization, MybatisOAuth2Authorization> {

    @Override
    public MybatisOAuth2Authorization convert(@Nullable OAuth2Authorization source) {
        if (source == null) {
            return null;
        }
        MybatisOAuth2Authorization MybatisOAuth2Authorization = new MybatisOAuth2Authorization();
        MybatisOAuth2Authorization.setId(source.getId());
        MybatisOAuth2Authorization.setRegisteredClientId(source.getRegisteredClientId());
        MybatisOAuth2Authorization.setPrincipalName(source.getPrincipalName());
        MybatisOAuth2Authorization.setAuthorizationGrantType(source.getAuthorizationGrantType());
        MybatisOAuth2Authorization.setAuthorizedScopes(source.getAuthorizedScopes());
        MybatisOAuth2Authorization.setAttributes(source.getAttributes());
        MybatisOAuth2Authorization.setState(source.getAttribute(OAuth2ParameterNames.STATE));

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                source.getToken(OAuth2AuthorizationCode.class);
        setTokenValues(authorizationCode,
                MybatisOAuth2Authorization::setAuthorizationCodeValue,
                MybatisOAuth2Authorization::setAuthorizationCodeIssuedAt,
                MybatisOAuth2Authorization::setAuthorizationCodeExpiresAt,
                MybatisOAuth2Authorization::setAuthorizationCodeMetadata);

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                source.getToken(OAuth2AccessToken.class);
        setTokenValues(
                accessToken,
                MybatisOAuth2Authorization::setAccessTokenValue,
                MybatisOAuth2Authorization::setAccessTokenIssuedAt,
                MybatisOAuth2Authorization::setAccessTokenExpiresAt,
                MybatisOAuth2Authorization::setAccessTokenMetadata
        );

        if (accessToken != null) {
            if (accessToken.getToken().getScopes() != null) {
                MybatisOAuth2Authorization.setAccessTokenScopes(StringUtils.collectionToDelimitedString(accessToken.getToken().getScopes(), ","));
            }

            if (accessToken.getToken().getTokenType() != null) {
                MybatisOAuth2Authorization.setAccessTokenType(accessToken.getToken().getTokenType().getValue());
            }
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                source.getToken(OAuth2RefreshToken.class);
        setTokenValues(
                refreshToken,
                MybatisOAuth2Authorization::setRefreshTokenValue,
                MybatisOAuth2Authorization::setRefreshTokenIssuedAt,
                MybatisOAuth2Authorization::setRefreshTokenExpiresAt,
                MybatisOAuth2Authorization::setRefreshTokenMetadata
        );

        OAuth2Authorization.Token<OidcIdToken> oidcIdToken =
                source.getToken(OidcIdToken.class);
        setTokenValues(
                oidcIdToken,
                MybatisOAuth2Authorization::setOidcIdTokenValue,
                MybatisOAuth2Authorization::setOidcIdTokenIssuedAt,
                MybatisOAuth2Authorization::setOidcIdTokenExpiresAt,
                MybatisOAuth2Authorization::setOidcIdTokenMetadata
        );
        if (oidcIdToken != null) {
            MybatisOAuth2Authorization.setOidcIdTokenClaims(oidcIdToken.getClaims());
        }

        OAuth2Authorization.Token<OAuth2UserCode> userCode =
                source.getToken(OAuth2UserCode.class);
        setTokenValues(
                userCode,
                MybatisOAuth2Authorization::setUserCodeValue,
                MybatisOAuth2Authorization::setUserCodeIssuedAt,
                MybatisOAuth2Authorization::setUserCodeExpiresAt,
                MybatisOAuth2Authorization::setUserCodeMetadata
        );

        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
                source.getToken(OAuth2DeviceCode.class);
        setTokenValues(
                deviceCode,
                MybatisOAuth2Authorization::setDeviceCodeValue,
                MybatisOAuth2Authorization::setDeviceCodeIssuedAt,
                MybatisOAuth2Authorization::setDeviceCodeExpiresAt,
                MybatisOAuth2Authorization::setDeviceCodeMetadata
        );
        return MybatisOAuth2Authorization;
    }
}
