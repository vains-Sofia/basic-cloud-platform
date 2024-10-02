package com.basic.cloud.oauth2.storage.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.cloud.mybatis.plus.domain.BasicEntity;
import com.basic.cloud.oauth2.storage.mybatis.handler.AuthenticationMethodsTypeHandler;
import com.basic.cloud.oauth2.storage.mybatis.handler.AuthorizationGrantTypesTypeHandler;
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
@TableName(value = "oauth2_application", autoResultMap = true)
public class MybatisOAuth2Application extends BasicEntity {

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

    @TableField(value = "client_authentication_methods", typeHandler = AuthenticationMethodsTypeHandler.class)
    private Set<ClientAuthenticationMethod> clientAuthenticationMethods;

    @TableField(value = "authorization_grant_types", typeHandler = AuthorizationGrantTypesTypeHandler.class)
    private Set<AuthorizationGrantType> authorizationGrantTypes;

    @TableField("redirect_uris")
    private Set<String> redirectUris;

    @TableField("post_logout_redirect_uris")
    private Set<String> postLogoutRedirectUris;

    @TableField("scopes")
    private Set<String> scopes;

    @TableField(value = "client_settings")
    private ClientSettings clientSettings;

    @TableField(value = "token_settings")
    private TokenSettings tokenSettings;
}
