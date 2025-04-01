package com.basic.framework.oauth2.storage.core.converter;

import com.basic.framework.oauth2.authorization.server.core.BasicCoreServiceConverter;
import com.basic.framework.oauth2.authorization.server.util.OAuth2JsonUtils;
import com.basic.framework.oauth2.storage.core.domain.BasicAuthorization;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 框架认证信息转实体
 *
 * @author vains
 */
@RequiredArgsConstructor
public class Authorization2OAuth2AuthorizationConverter implements BasicCoreServiceConverter<BasicAuthorization, OAuth2Authorization> {

    private final RegisteredClientRepository registeredClientRepository;

    private final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };

    @Override
    public OAuth2Authorization convert(@Nullable BasicAuthorization source) {
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
        authorization.authorizationGrantType(new AuthorizationGrantType(source.getAuthorizationGrantType()));
        authorization.authorizedScopes(source.getAuthorizedScopes());

        Map<String, Object> authorizationAttributes = OAuth2JsonUtils.toObject(source.getAttributes(), typeRef.getType());
        if (!ObjectUtils.isEmpty(authorizationAttributes)) {
            authorization.attributes(attributes -> attributes.putAll(authorizationAttributes));
        }
        if (source.getState() != null) {
            authorization.attribute(OAuth2ParameterNames.STATE, source.getState());
        }

        if (source.getAuthorizationCodeValue() != null) {
            OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
                    source.getAuthorizationCodeValue(),
                    timeToInstant(source.getAuthorizationCodeIssuedAt()),
                    timeToInstant(source.getAuthorizationCodeExpiresAt()));

            Map<String, Object> authorizationCodeMetadata = OAuth2JsonUtils.toObject(source.getAuthorizationCodeMetadata(), typeRef.getType());
            if (!ObjectUtils.isEmpty(authorizationCodeMetadata)) {
                authorization.token(authorizationCode, metadata -> metadata.putAll(authorizationCodeMetadata));
            }
        }

        if (source.getAccessTokenValue() != null) {
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    source.getAccessTokenValue(),
                    timeToInstant(source.getAccessTokenIssuedAt()),
                    timeToInstant(source.getAccessTokenExpiresAt()),
                    StringUtils.commaDelimitedListToSet(source.getAccessTokenScopes()));

            Map<String, Object> accessTokenMetadata = OAuth2JsonUtils.toObject(source.getAccessTokenMetadata(), typeRef.getType());
            if (!ObjectUtils.isEmpty(accessTokenMetadata)) {
                authorization.token(accessToken, metadata -> metadata.putAll(accessTokenMetadata));
            }
        }

        if (source.getRefreshTokenValue() != null) {
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    source.getRefreshTokenValue(),
                    timeToInstant(source.getRefreshTokenIssuedAt()),
                    timeToInstant(source.getRefreshTokenExpiresAt()));

            Map<String, Object> refreshTokenMetadata = OAuth2JsonUtils.toObject(source.getRefreshTokenMetadata(), typeRef.getType());
            if (!ObjectUtils.isEmpty(refreshTokenMetadata)) {
                authorization.token(refreshToken, metadata -> metadata.putAll(refreshTokenMetadata));
            }
        }

        if (source.getOidcIdTokenValue() != null) {
            Map<String, Object> oidcIdTokenClaims = OAuth2JsonUtils.toObject(source.getOidcIdTokenClaims(), typeRef.getType());
            OidcIdToken idToken = new OidcIdToken(
                    source.getOidcIdTokenValue(),
                    timeToInstant(source.getOidcIdTokenIssuedAt()),
                    timeToInstant(source.getOidcIdTokenExpiresAt()),
                    oidcIdTokenClaims);

            Map<String, Object> oidcIdTokenMetadata = OAuth2JsonUtils.toObject(source.getOidcIdTokenMetadata(), typeRef.getType());
            if (!ObjectUtils.isEmpty(oidcIdTokenMetadata)) {
                authorization.token(idToken, metadata -> metadata.putAll(oidcIdTokenMetadata));
            }
        }

        if (source.getUserCodeValue() != null) {
            OAuth2UserCode userCode = new OAuth2UserCode(
                    source.getUserCodeValue(),
                    timeToInstant(source.getUserCodeIssuedAt()),
                    timeToInstant(source.getUserCodeExpiresAt()));

            Map<String, Object> userCodeMetadata = OAuth2JsonUtils.toObject(source.getUserCodeMetadata(), typeRef.getType());
            if (!ObjectUtils.isEmpty(userCodeMetadata)) {
                authorization.token(userCode, metadata -> metadata.putAll(userCodeMetadata));
            }
        }

        if (source.getDeviceCodeValue() != null) {
            OAuth2DeviceCode deviceCode = new OAuth2DeviceCode(
                    source.getDeviceCodeValue(),
                    timeToInstant(source.getDeviceCodeIssuedAt()),
                    timeToInstant(source.getDeviceCodeExpiresAt()));

            Map<String, Object> deviceCodeMetadata = OAuth2JsonUtils.toObject(source.getDeviceCodeMetadata(), typeRef.getType());
            if (!ObjectUtils.isEmpty(deviceCodeMetadata)) {
                authorization.token(deviceCode, metadata -> metadata.putAll(deviceCodeMetadata));
            }
        }

        return authorization.build();
    }
}
