package com.basic.framework.oauth2.storage.mybatis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
import com.basic.framework.mybatis.plus.handler.type.BasicCollectionTypeHandler;
import com.basic.framework.oauth2.storage.core.domain.model.BasicClientSettings;
import com.basic.framework.oauth2.storage.core.domain.model.BasicTokenSettings;
import lombok.Getter;
import lombok.Setter;

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

    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

    @TableField("client_logo")
    private String clientLogo;

    @TableField(value = "client_authentication_methods", typeHandler = BasicCollectionTypeHandler.class)
    private Set<String> clientAuthenticationMethods;

    @TableField(value = "authorization_grant_types", typeHandler = BasicCollectionTypeHandler.class)
    private Set<String> authorizationGrantTypes;

    @TableField(value = "redirect_uris", typeHandler = BasicCollectionTypeHandler.class)
    private Set<String> redirectUris;

    @TableField(value = "post_logout_redirect_uris", typeHandler = BasicCollectionTypeHandler.class)
    private Set<String> postLogoutRedirectUris;

    @TableField(value = "scopes", typeHandler = BasicCollectionTypeHandler.class)
    private Set<String> scopes;

    @TableField(value = "client_settings", typeHandler = JacksonTypeHandler.class)
    private BasicClientSettings clientSettings;

    @TableField(value = "token_settings", typeHandler = JacksonTypeHandler.class)
    private BasicTokenSettings tokenSettings;

    @TableField("system_client")
    private Boolean systemClient;
}
