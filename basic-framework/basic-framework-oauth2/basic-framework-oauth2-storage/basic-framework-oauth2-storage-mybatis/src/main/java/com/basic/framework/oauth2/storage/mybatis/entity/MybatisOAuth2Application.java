package com.basic.framework.oauth2.storage.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
import com.basic.framework.mybatis.plus.handler.type.BasicCollectionTypeHandler;
import com.basic.framework.oauth2.storage.mybatis.handler.AuthenticationMethodsTypeHandler;
import com.basic.framework.oauth2.storage.mybatis.handler.AuthorizationGrantTypesTypeHandler;
import com.basic.framework.oauth2.storage.mybatis.handler.scan.ClientSettingsTypeHandler;
import com.basic.framework.oauth2.storage.mybatis.handler.scan.TokenSettingsTypeHandler;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * oauth2 客户端信息实体
 * 必须在MybatisPlus注解中显示的指定TypeHandler，否则调用updateById时获取不到对应的TypeHandler，字段类型全部为Object
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

    @TableField(value = "redirect_uris", typeHandler = BasicCollectionTypeHandler.class)
    private Set<String> redirectUris;

    @TableField(value = "post_logout_redirect_uris", typeHandler = BasicCollectionTypeHandler.class)
    private Set<String> postLogoutRedirectUris;

    @TableField(value = "scopes", typeHandler = BasicCollectionTypeHandler.class)
    private Set<String> scopes;

    @TableField(value = "client_settings", typeHandler = ClientSettingsTypeHandler.class)
    private ClientSettings clientSettings;

    @TableField(value = "token_settings", typeHandler = TokenSettingsTypeHandler.class)
    private TokenSettings tokenSettings;
}
