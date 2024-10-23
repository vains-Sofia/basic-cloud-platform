package com.basic.framework.oauth2.storage.core.entity;

import com.basic.framework.oauth2.storage.core.domain.OAuth2ClientSettings;
import com.basic.framework.oauth2.storage.core.domain.OAuth2TokenSettings;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * oauth2 客户端信息实体
 * 必须在MybatisPlus注解中显示的指定TypeHandler，否则调用updateById时获取不到对应的TypeHandler，字段类型全部为Object
 *
 * @author vains
 * @since 2024-05-17
 */
@Data
@Schema(name = "客户端信息")
public class OAuth2Application {

    /**
     * 客户端数据id
     */
    @Schema(title = "客户端数据id")
    private Long id;

    /**
     * 客户端id
     */
    @Schema(title = "客户端id")
    private String clientId;

    /**
     * 客户端id签发时间
     */
    @Schema(title = "客户端id签发时间")
    private LocalDateTime clientIdIssuedAt;

    /**
     * 客户端密钥
     */
    @Schema(title = "客户端密钥")
    private String clientSecret;

    /**
     * 客户端密钥过期时间
     */
    @Schema(title = "客户端密钥过期时间")
    private LocalDateTime clientSecretExpiresAt;

    /**
     * 客户端名称
     */
    @Schema(title = "客户端名称")
    private String clientName;

    /**
     * 客户端认证方式
     */
    @Schema(title = "客户端认证方式")
    private Set<String> clientAuthenticationMethods;

    /**
     * 客户端支持的grant type
     */
    @Schema(title = "客户端支持的grant type")
    private Set<String> authorizationGrantTypes;

    /**
     * 客户端回调地址
     */
    @Schema(title = "客户端回调地址")
    private Set<String> redirectUris;

    /**
     * Openid Connect登出后跳转地址
     */
    @Schema(title = "Openid Connect登出后跳转地址")
    private Set<String> postLogoutRedirectUris;

    /**
     * 客户端拥有的权限
     */
    @Schema(title = "客户端拥有的权限")
    private Set<String> scopes;

    /**
     * 客户端设置
     */
    @Schema(title = "客户端设置")
    private OAuth2ClientSettings clientSettings;

    /**
     * 客户端申请的access token设置
     */
    @Schema(title = "客户端申请的access token设置")
    private OAuth2TokenSettings tokenSettings;

    /**
     * 创建人
     */
    @Schema(title = "创建人")
    private Long createBy;

    /**
     * 修改人
     */
    @Schema(title = "修改人")
    private Long updateBy;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(title = "修改时间")
    private LocalDateTime updateTime;
}
