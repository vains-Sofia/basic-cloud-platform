package com.basic.framework.oauth2.storage.repository;

import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2Authorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OAuth2AuthorizationRepository extends
        JpaRepository<JpaOAuth2Authorization, String>, JpaSpecificationExecutor<JpaOAuth2Authorization> {

    /**
     * 根据state查询认证信息
     *
     * @param state state的值
     * @return 认证信息
     */
    Optional<JpaOAuth2Authorization> findByState(String state);

    /**
     * 根据授权码查询认证信息
     *
     * @param authorizationCode 授权码
     * @return 认证信息
     */
    Optional<JpaOAuth2Authorization> findByAuthorizationCodeValue(String authorizationCode);

    /**
     * 根据 access token 查询认证信息
     *
     * @param accessToken 认证授权后签发的access token
     * @return 认证信息
     */
    Optional<JpaOAuth2Authorization> findByAccessTokenValue(String accessToken);

    /**
     * 根据 refresh token 查询认证信息
     *
     * @param refreshToken 刷新token
     * @return 认证信息
     */
    Optional<JpaOAuth2Authorization> findByRefreshTokenValue(String refreshToken);

    /**
     * 根据 oidc id token 查询认证信息
     *
     * @param idToken 认证授权后签发的oidc id token
     * @return 认证信息
     */
    Optional<JpaOAuth2Authorization> findByOidcIdTokenValue(String idToken);

    /**
     * 根据 user code 查询认证信息
     *
     * @param userCode 用户码
     * @return 认证信息
     */
    Optional<JpaOAuth2Authorization> findByUserCodeValue(String userCode);

    /**
     * 根据 device token 查询认证信息
     *
     * @param deviceCode 设备码
     * @return 认证信息
     */
    Optional<JpaOAuth2Authorization> findByDeviceCodeValue(String deviceCode);

    /**
     * 根据 state/授权码/access token/刷新token/oidc id token/用户码/设备码 查询认证信息
     *
     * @param token 认证授权后签发的access token
     * @return 认证信息
     */
    @Query("select a from JpaOAuth2Authorization a where a.state = :token" +
            " or a.authorizationCodeValue = :token" +
            " or a.accessTokenValue = :token" +
            " or a.refreshTokenValue = :token" +
            " or a.oidcIdTokenValue = :token" +
            " or a.userCodeValue = :token" +
            " or a.deviceCodeValue = :token"
    )
    Optional<JpaOAuth2Authorization> findByToken(@Param("token") String token);
}