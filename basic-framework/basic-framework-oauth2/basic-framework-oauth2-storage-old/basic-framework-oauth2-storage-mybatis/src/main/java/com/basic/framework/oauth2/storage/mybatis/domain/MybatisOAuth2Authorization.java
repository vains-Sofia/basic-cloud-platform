package com.basic.framework.oauth2.storage.mybatis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author vains
 * @since 2024-05-17
 */
@Getter
@Setter
@TableName(value = "oauth2_authorization", autoResultMap = true)
public class MybatisOAuth2Authorization extends BasicEntity {

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @TableField("registered_client_id")
    private String registeredClientId;

    @TableField("principal_name")
    private String principalName;

    @TableField("authorization_grant_type")
    private String authorizationGrantType;

    @TableField("authorized_scopes")
    private Set<String> authorizedScopes;

    @TableField(value = "attributes")
    private String attributes;

    @TableField("state")
    private String state;

    @TableField("authorization_code_value")
    private String authorizationCodeValue;

    @TableField("authorization_code_issued_at")
    private LocalDateTime authorizationCodeIssuedAt;

    @TableField("authorization_code_expires_at")
    private LocalDateTime authorizationCodeExpiresAt;

    @TableField(value = "authorization_code_metadata")
    private String authorizationCodeMetadata;

    @TableField("access_token_value")
    private String accessTokenValue;

    @TableField("access_token_issued_at")
    private LocalDateTime accessTokenIssuedAt;

    @TableField("access_token_expires_at")
    private LocalDateTime accessTokenExpiresAt;

    @TableField(value = "access_token_metadata")
    private String accessTokenMetadata;

    @TableField("access_token_type")
    private String accessTokenType;

    @TableField("access_token_scopes")
    private String accessTokenScopes;

    @TableField("refresh_token_value")
    private String refreshTokenValue;

    @TableField("refresh_token_issued_at")
    private LocalDateTime refreshTokenIssuedAt;

    @TableField("refresh_token_expires_at")
    private LocalDateTime refreshTokenExpiresAt;

    @TableField(value = "refresh_token_metadata")
    private String refreshTokenMetadata;

    @TableField("oidc_id_token_value")
    private String oidcIdTokenValue;

    @TableField("oidc_id_token_issued_at")
    private LocalDateTime oidcIdTokenIssuedAt;

    @TableField("oidc_id_token_expires_at")
    private LocalDateTime oidcIdTokenExpiresAt;

    @TableField(value = "oidc_id_token_metadata")
    private String oidcIdTokenMetadata;

    @TableField(value = "oidc_id_token_claims")
    private String oidcIdTokenClaims;

    @TableField("user_code_value")
    private String userCodeValue;

    @TableField("user_code_issued_at")
    private LocalDateTime userCodeIssuedAt;

    @TableField("user_code_expires_at")
    private LocalDateTime userCodeExpiresAt;

    @TableField(value = "user_code_metadata")
    private String userCodeMetadata;

    @TableField("device_code_value")
    private String deviceCodeValue;

    @TableField("device_code_issued_at")
    private LocalDateTime deviceCodeIssuedAt;

    @TableField("device_code_expires_at")
    private LocalDateTime deviceCodeExpiresAt;

    @TableField(value = "device_code_metadata")
    private String deviceCodeMetadata;
}
