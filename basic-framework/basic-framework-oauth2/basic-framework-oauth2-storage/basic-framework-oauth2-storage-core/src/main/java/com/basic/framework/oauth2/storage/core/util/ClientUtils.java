package com.basic.framework.oauth2.storage.core.util;

import com.basic.framework.oauth2.storage.core.domain.OAuth2ClientSettings;
import com.basic.framework.oauth2.storage.core.domain.OAuth2TokenSettings;
import com.basic.framework.oauth2.storage.core.enums.TimeToLiveUnitEnum;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.Set;

/**
 * 客户端转换工具类
 *
 * @author vains
 */
@UtilityClass
public class ClientUtils {

    /**
     * 生成一个客户端设置对象
     *
     * @param clientSettings 客户端设置入参
     * @return 返回客户端设置
     */
    public static ClientSettings resolveOAuth2ClientSettings(OAuth2ClientSettings clientSettings, Set<String> clientAuthenticationMethods) {
        ClientSettings.Builder builder = ClientSettings.builder();
        if (clientSettings == null) {
            return builder.build();
        }

        builder.requireProofKey(clientSettings.getRequireProofKey() == null ? Boolean.FALSE : clientSettings.getRequireProofKey());
        builder.requireAuthorizationConsent(clientSettings.getRequireAuthorizationConsent() == null ? Boolean.FALSE : clientSettings.getRequireAuthorizationConsent());

        if (!ObjectUtils.isEmpty(clientSettings.getTokenEndpointAuthenticationSigningAlgorithm())) {
            if (!ObjectUtils.isEmpty(clientAuthenticationMethods)) {
                if (clientAuthenticationMethods.contains(ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue())) {
                    MacAlgorithm macAlgorithm = MacAlgorithm.from(clientSettings.getTokenEndpointAuthenticationSigningAlgorithm());
                    if (macAlgorithm == null) {
                        macAlgorithm = MacAlgorithm.HS256;
                    }
                    builder.tokenEndpointAuthenticationSigningAlgorithm(macAlgorithm);
                } else if (clientAuthenticationMethods.contains(ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue())) {
                    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.from(clientSettings.getTokenEndpointAuthenticationSigningAlgorithm());
                    if (signatureAlgorithm == null) {
                        signatureAlgorithm = SignatureAlgorithm.RS256;
                    }
                    builder.tokenEndpointAuthenticationSigningAlgorithm(signatureAlgorithm);
                    if (!ObjectUtils.isEmpty(clientSettings.getJwkSetUrl())) {
                        builder.jwkSetUrl(clientSettings.getJwkSetUrl());
                    }
                }
            }
        }

        if (!ObjectUtils.isEmpty(clientSettings.getX509CertificateSubjectDN())) {
            builder.x509CertificateSubjectDN(clientSettings.getX509CertificateSubjectDN());
        }

        return builder.build();
    }

    /**
     * 根据spring的设置生成自己的设置bean
     *
     * @param clientSettings 客户端设置
     * @return 设置
     */
    public static OAuth2ClientSettings resolveClientSettings(ClientSettings clientSettings) {
        OAuth2ClientSettings model = new OAuth2ClientSettings();

        if (clientSettings == null) {
            return model;
        }

        model.setJwkSetUrl(clientSettings.getJwkSetUrl());
        model.setRequireProofKey(clientSettings.isRequireProofKey());
        model.setRequireAuthorizationConsent(clientSettings.isRequireAuthorizationConsent());
        model.setX509CertificateSubjectDN(clientSettings.getX509CertificateSubjectDN());
        if (!ObjectUtils.isEmpty(clientSettings.getTokenEndpointAuthenticationSigningAlgorithm())) {
            model.setTokenEndpointAuthenticationSigningAlgorithm(clientSettings.getTokenEndpointAuthenticationSigningAlgorithm().getName());
        }
        return model;
    }

    /**
     * 生成一个Token设置对象
     *
     * @param tokenSettings Token设置入参
     * @return 返回Token设置
     */
    public static TokenSettings resolveOAuth2TokenSettings(OAuth2TokenSettings tokenSettings) {
        TokenSettings.Builder builder = TokenSettings.builder();
        if (tokenSettings == null) {
            return builder.build();
        }

        // 授权码有效时长
        if (!ObjectUtils.isEmpty(tokenSettings.getAuthorizationCodeTimeToLive())) {
            builder.authorizationCodeTimeToLive(Duration.of(tokenSettings.getAuthorizationCodeTimeToLive(), tokenSettings.getAuthorizationCodeTimeToLiveUnit().getUnit()));
        }

        // access token 有效时长
        if (!ObjectUtils.isEmpty(tokenSettings.getAccessTokenTimeToLive())) {
            builder.accessTokenTimeToLive(Duration.of(tokenSettings.getAccessTokenTimeToLive(), tokenSettings.getAccessTokenTimeToLiveUnit().getUnit()));
        }

        // access token的格式
        if (tokenSettings.getAccessTokenFormat().equals(OAuth2TokenFormat.SELF_CONTAINED.getValue())) {
            builder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
        } else if (tokenSettings.getAccessTokenFormat().equals(OAuth2TokenFormat.REFERENCE.getValue())) {
            builder.accessTokenFormat(OAuth2TokenFormat.REFERENCE);
        } else {
            // 默认使用jwt token
            builder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
        }

        // 设备码有效时长
        if (!ObjectUtils.isEmpty(tokenSettings.getDeviceCodeTimeToLive())) {
            builder.deviceCodeTimeToLive(Duration.of(tokenSettings.getDeviceCodeTimeToLive(), tokenSettings.getDeviceCodeTimeToLiveUnit().getUnit()));
        }

        // 设置refresh token是否可重复使用
        if (!ObjectUtils.isEmpty(tokenSettings.getReuseRefreshTokens())) {
            builder.reuseRefreshTokens(tokenSettings.getReuseRefreshTokens());
        }

        // refresh token有效时长
        if (!ObjectUtils.isEmpty(tokenSettings.getRefreshTokenTimeToLive())) {
            builder.refreshTokenTimeToLive(Duration.of(tokenSettings.getRefreshTokenTimeToLive(), tokenSettings.getRefreshTokenTimeToLiveUnit().getUnit()));
        }

        // 对ID Token进行签名的JWS算法。
        if (!ObjectUtils.isEmpty(tokenSettings.getIdTokenSignatureAlgorithm())) {
            SignatureAlgorithm algorithm = SignatureAlgorithm.from(tokenSettings.getIdTokenSignatureAlgorithm());
            if (algorithm == null) {
                algorithm = SignatureAlgorithm.RS256;
            }
            builder.idTokenSignatureAlgorithm(algorithm);
        }

        // 如果访问令牌必须绑定到客户端x509使用tls_client_auth或self_signed_tls_client_auth方法进行客户端身份验证期间接收的证书，则设置为true。
        if (!ObjectUtils.isEmpty(tokenSettings.getX509CertificateBoundAccessTokens())) {
            builder.x509CertificateBoundAccessTokens(tokenSettings.getX509CertificateBoundAccessTokens());
        }

        return builder.build();
    }

    /**
     * 生成一个Token设置对象
     *
     * @param tokenSettings Token设置入参
     * @return 返回Token设置
     */
    public static OAuth2TokenSettings resolveTokenSettings(TokenSettings tokenSettings) {
        OAuth2TokenSettings model = new OAuth2TokenSettings();
        if (tokenSettings == null) {
            return model;
        }

        // 授权码有效时长
        model.setAuthorizationCodeTimeToLive(tokenSettings.getAuthorizationCodeTimeToLive().toSeconds());
        // 默认以秒的形式保存
        model.setAuthorizationCodeTimeToLiveUnit(TimeToLiveUnitEnum.SECONDS);

        // access token 有效时长
        model.setAccessTokenTimeToLive(tokenSettings.getAccessTokenTimeToLive().toSeconds());
        // 默认以秒的形式保存
        model.setAccessTokenTimeToLiveUnit(TimeToLiveUnitEnum.SECONDS);

        // access token的格式
        model.setAccessTokenFormat(tokenSettings.getAccessTokenFormat().getValue());

        // 设备码有效时长
        model.setDeviceCodeTimeToLive(tokenSettings.getDeviceCodeTimeToLive().toSeconds());
        // 默认以秒的形式保存
        model.setDeviceCodeTimeToLiveUnit(TimeToLiveUnitEnum.SECONDS);

        // 设置refresh token是否可重复使用
        model.setReuseRefreshTokens(tokenSettings.isReuseRefreshTokens());

        // 设置refresh token是否可重复使用
        model.setRefreshTokenTimeToLive(tokenSettings.getRefreshTokenTimeToLive().toSeconds());
        // 默认以秒的形式保存
        model.setRefreshTokenTimeToLiveUnit(TimeToLiveUnitEnum.SECONDS);

        // 对ID Token进行签名的JWS算法。
        model.setIdTokenSignatureAlgorithm(tokenSettings.getIdTokenSignatureAlgorithm().getName());

        // 如果访问令牌必须绑定到客户端x509使用tls_client_auth或self_signed_tls_client_auth方法进行客户端身份验证期间接收的证书，则设置为true。
        model.setX509CertificateBoundAccessTokens(tokenSettings.isX509CertificateBoundAccessTokens());

        return model;
    }

}
