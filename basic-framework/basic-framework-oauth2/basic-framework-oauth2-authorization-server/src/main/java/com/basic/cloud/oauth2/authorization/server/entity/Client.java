package com.basic.cloud.oauth2.authorization.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.cloud.mybatis.plus.domain.BasicEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

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
@TableName("client")
public class Client extends BasicEntity {

    @TableId("id")
    private Long id;

    @TableField("client_id")
    private String clientId;

    @TableField("client_id_issued_at")
    private LocalDateTime clientIdIssuedAt;

    @TableField("client_secret")
    private String clientSecret;

    @TableField("client_secret_expires_at")
    private LocalDateTime clientSecretExpiresAt;

    @TableField("client_name")
    private String clientName;

    @TableField("client_authentication_methods")
    private Set<ClientAuthenticationMethod> clientAuthenticationMethods;

    @TableField("authorization_grant_types")
    private Set<AuthorizationGrantType> authorizationGrantTypes;

    @TableField("redirect_uris")
    private Set<String> redirectUris;

    @TableField("post_logout_redirect_uris")
    private Set<String> postLogoutRedirectUris;

    @TableField("scopes")
    private Set<String> scopes;

    @TableField("client_settings")
    private ClientSettings clientSettings;

    @TableField("token_settings")
    private TokenSettings tokenSettings;
}
