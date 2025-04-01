package com.basic.framework.oauth2.storage.domain.entity;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "oauth2_application")
@EqualsAndHashCode(callSuper = true)
public class JpaOAuth2Application extends BasicAuditorEntity {

    /**
     * 主键id
     */
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 客户端id
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "client_id", nullable = false)
    private String clientId;

    /**
     * 客户端id签发时间
     */
    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "client_id_issued_at", nullable = false)
    private LocalDateTime clientIdIssuedAt;

    /**
     * 客户端秘钥
     */
    @Size(max = 255)
    @Column(name = "client_secret")
    private String clientSecret;

    /**
     * 客户端秘钥签发时间
     */
    @Column(name = "client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;

    /**
     * 客户端名称
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "client_name", nullable = false)
    private String clientName;

    /**
     * 客户端 logo
     */
    @Size(max = 255)
    @Column(name = "client_logo")
    private String clientLogo;

    /**
     * 客户端认证方式
     */
    @NotNull
    @Size(max = 1000)
    @Column(name = "client_authentication_methods", nullable = false, length = 1000)
    private String clientAuthenticationMethods;

    /**
     * 客户端支持的grant type
     */
    @NotNull
    @Size(max = 1000)
    @Column(name = "authorization_grant_types", nullable = false, length = 1000)
    private String authorizationGrantTypes;

    /**
     * 客户端回调地址
     */
    @Size(max = 1000)
    @Column(name = "redirect_uris", length = 1000)
    private String redirectUris;

    /**
     * Openid Connect登出后跳转地址
     */
    @Size(max = 1000)
    @Column(name = "post_logout_redirect_uris", length = 1000)
    private String postLogoutRedirectUris;

    /**
     * 客户端拥有的权限
     */
    @NotNull
    @Size(max = 1000)
    @Column(name = "scopes", nullable = false, length = 1000)
    private String scopes;

    /**
     * 客户端设置
     */
    @NotNull
    @Size(max = 2000)
    @Column(name = "client_settings", nullable = false, length = 2000)
    private String clientSettings;

    /**
     * 客户端申请的access token设置
     */
    @NotNull
    @Size(max = 2000)
    @Column(name = "token_settings", nullable = false, length = 2000)
    private String tokenSettings;

}