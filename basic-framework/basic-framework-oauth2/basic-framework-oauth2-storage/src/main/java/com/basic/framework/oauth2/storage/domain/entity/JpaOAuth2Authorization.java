package com.basic.framework.oauth2.storage.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * oauth2认证信息
 *
 * @author vains
 */
@Data
@Entity
@Table(name = "oauth2_authorization")
public class JpaOAuth2Authorization {

    /**
     * 数据id
     */
    @Id
    @Size(max = 50)
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    /**
     * 认证时使用的客户端id
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "registered_client_id", nullable = false)
    private String registeredClientId;

    /**
     * 认证用户Id
     */
    @Column(name = "principal_id")
    private Long principalId;

    /**
     * 认证用户名
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "principal_name", nullable = false)
    private String principalName;

    /**
     * 认证时使用的模式(grant type)
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "authorization_grant_type", nullable = false)
    private String authorizationGrantType;

    /**
     * 用户授权申请的scope，最终存储的还是授权确认的scope
     */
    @Size(max = 1000)
    @Column(name = "authorized_scopes", length = 1000)
    private String authorizedScopes;

    /**
     * 属性
     */
    @Lob
    @Column(name = "attributes")
    private String attributes;

    /**
     * state
     */
    @Size(max = 500)
    @Column(name = "state", length = 500)
    private String state;

    /**
     * 授权码的值
     */
    @Lob
    @Column(name = "authorization_code_value")
    private String authorizationCodeValue;

    /**
     * 授权码签发时间
     */
    @Column(name = "authorization_code_issued_at")
    private LocalDateTime authorizationCodeIssuedAt;

    /**
     * 授权码过期时间
     */
    @Column(name = "authorization_code_expires_at")
    private LocalDateTime authorizationCodeExpiresAt;

    /**
     * 授权码元数据
     */
    @Lob
    @Column(name = "authorization_code_metadata")
    private String authorizationCodeMetadata;

    /**
     * 认证后签发的access token
     */
    @Lob
    @Column(name = "access_token_value")
    private String accessTokenValue;

    /**
     * access token 签发时间
     */
    @Column(name = "access_token_issued_at")
    private LocalDateTime accessTokenIssuedAt;

    /**
     * access token 过期时间
     */
    @Column(name = "access_token_expires_at")
    private LocalDateTime accessTokenExpiresAt;

    /**
     * access token 元数据
     */
    @Lob
    @Column(name = "access_token_metadata")
    private String accessTokenMetadata;

    /**
     * access token类型，一般是bearer
     */
    @Size(max = 255)
    @Column(name = "access_token_type")
    private String accessTokenType;

    /**
     * access token中包含的scope(授权确认过的scope)
     */
    @Lob
    @Column(name = "access_token_scopes")
    private String accessTokenScopes;

    /**
     * 认证后签发的 refresh token
     */
    @Lob
    @Column(name = "refresh_token_value")
    private String refreshTokenValue;

    /**
     * refresh token 签发时间
     */
    @Column(name = "refresh_token_issued_at")
    private LocalDateTime refreshTokenIssuedAt;

    /**
     * refresh token 过期时间
     */
    @Column(name = "refresh_token_expires_at")
    private LocalDateTime refreshTokenExpiresAt;

    /**
     * refresh token 元数据
     */
    @Lob
    @Column(name = "refresh_token_metadata")
    private String refreshTokenMetadata;

    /**
     * 认证后签发的 oidc id token
     */
    @Lob
    @Column(name = "oidc_id_token_value")
    private String oidcIdTokenValue;

    /**
     * oidc id token 签发时间
     */
    @Column(name = "oidc_id_token_issued_at")
    private LocalDateTime oidcIdTokenIssuedAt;

    /**
     * oidc id token 过期时间
     */
    @Column(name = "oidc_id_token_expires_at")
    private LocalDateTime oidcIdTokenExpiresAt;

    /**
     * oidc id token 元数据
     */
    @Lob
    @Column(name = "oidc_id_token_metadata")
    private String oidcIdTokenMetadata;

    /**
     * oidc id token 声明(Claims)信息(一般情况下是用户数据)
     */
    @Lob
    @Column(name = "oidc_id_token_claims")
    private String oidcIdTokenClaims;

    /**
     * 设备码模式(Device Flow)中的 user code
     */
    @Lob
    @Column(name = "user_code_value")
    private String userCodeValue;

    /**
     * user code 签发时间
     */
    @Column(name = "user_code_issued_at")
    private LocalDateTime userCodeIssuedAt;

    /**
     * user code 过期时间
     */
    @Column(name = "user_code_expires_at")
    private LocalDateTime userCodeExpiresAt;

    /**
     * user code 元数据
     */
    @Lob
    @Column(name = "user_code_metadata")
    private String userCodeMetadata;

    /**
     * 设备码模式(Device Flow)中的 device code
     */
    @Lob
    @Column(name = "device_code_value")
    private String deviceCodeValue;

    /**
     * device code 签发时间
     */
    @Column(name = "device_code_issued_at")
    private LocalDateTime deviceCodeIssuedAt;

    /**
     * device code 过期时间
     */
    @Column(name = "device_code_expires_at")
    private LocalDateTime deviceCodeExpiresAt;

    /**
     * device code 元数据
     */
    @Lob
    @Column(name = "device_code_metadata")
    private String deviceCodeMetadata;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

}