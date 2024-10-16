package com.basic.framework.oauth2.storage.mybatis.converter;

import com.basic.framework.oauth2.authorization.server.core.BasicCoreServiceConverter;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2Authorization;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.StringUtils;

/**
 * 框架认证信息转实体
 *
 * @author vains
 */
@RequiredArgsConstructor
public class Authorization2OAuth2AuthorizationConverter implements BasicCoreServiceConverter<MybatisOAuth2Authorization, OAuth2Authorization> {

    private final RegisteredClientRepository registeredClientRepository;

    @Override
    public OAuth2Authorization convert(@Nullable MybatisOAuth2Authorization source) {
        if (source == null) {
            return null;
        }
        RegisteredClient registeredClient = this.registeredClientRepository.findById(source.getRegisteredClientId());
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '" + source.getRegisteredClientId() + "' was not found in the RegisteredClientRepository.");
        }
        OAuth2Authorization.Builder authorization = OAuth2Authorization.withRegisteredClient(registeredClient);
        authorization.id(source.getId());
        authorization.principalName(source.getPrincipalName());
        authorization.authorizationGrantType(source.getAuthorizationGrantType());
        authorization.authorizedScopes(source.getAuthorizedScopes());
        authorization.attributes(attributes -> attributes.putAll(source.getAttributes()));
        if (source.getState() != null) {
            authorization.attribute(OAuth2ParameterNames.STATE, source.getState());
        }

        if (source.getAuthorizationCodeValue() != null) {
            OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
                    source.getAuthorizationCodeValue(),
                    timeToInstant(source.getAuthorizationCodeIssuedAt()),
                    timeToInstant(source.getAuthorizationCodeExpiresAt()));
            authorization.token(authorizationCode, metadata -> metadata.putAll(source.getAuthorizationCodeMetadata()));
        }

        if (source.getAccessTokenValue() != null) {
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    source.getAccessTokenValue(),
                    timeToInstant(source.getAccessTokenIssuedAt()),
                    timeToInstant(source.getAccessTokenExpiresAt()),
                    StringUtils.commaDelimitedListToSet(source.getAccessTokenScopes()));
            authorization.token(accessToken, metadata -> metadata.putAll(source.getAccessTokenMetadata()));
        }

        if (source.getRefreshTokenValue() != null) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    source.getRefreshTokenValue(),
                    timeToInstant(source.getRefreshTokenIssuedAt()),
                    timeToInstant(source.getRefreshTokenExpiresAt()));
            authorization.token(refreshToken, metadata -> metadata.putAll(source.getRefreshTokenMetadata()));
        }

        if (source.getOidcIdTokenValue() != null) {
            OidcIdToken idToken = new OidcIdToken(
                    source.getOidcIdTokenValue(),
                    timeToInstant(source.getOidcIdTokenIssuedAt()),
                    timeToInstant(source.getOidcIdTokenExpiresAt()),
                    source.getOidcIdTokenClaims());
            authorization.token(idToken, metadata -> metadata.putAll(source.getOidcIdTokenMetadata()));
        }

        if (source.getUserCodeValue() != null) {
            OAuth2UserCode userCode = new OAuth2UserCode(
                    source.getUserCodeValue(),
                    timeToInstant(source.getUserCodeIssuedAt()),
                    timeToInstant(source.getUserCodeExpiresAt()));
            authorization.token(userCode, metadata -> metadata.putAll(source.getUserCodeMetadata()));
        }

        if (source.getDeviceCodeValue() != null) {
            OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(
                    source.getDeviceCodeValue(),
                    timeToInstant(source.getDeviceCodeIssuedAt()),
                    timeToInstant(source.getDeviceCodeExpiresAt()));
            authorization.token(deviceCode, metadata -> metadata.putAll(source.getDeviceCodeMetadata()));
        }

        return authorization.build();
    }
}
