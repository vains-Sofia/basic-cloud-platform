package com.basic.framework.oauth2.storage.domain.request;

import com.basic.framework.data.validation.group.Update;
import com.basic.framework.oauth2.storage.domain.model.BasicClientSettings;
import com.basic.framework.oauth2.storage.domain.model.BasicTokenSettings;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 保存/更新客户端信息入参
 *
 * @author vains
 */
@Data
@Schema(title = "插入或更新客户端信息入参")
public class SaveApplicationRequest {

    /**
     * 客户端数据id
     */
    @Schema(title = "客户端数据id")
    @NotNull(groups = {Update.class})
    private Long id;

    /**
     * 客户端id
     */
    @Schema(title = "客户端id")
    @NotBlank(message = "{oauth2.application.clientId.notBlank}", groups = {Update.class, Default.class})
    private String clientId;

    /**
     * 客户端密钥
     */
    @Schema(title = "客户端secret")
    @NotBlank(message = "{oauth2.application.clientSecret.notBlank}", groups = {Default.class})
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
    @NotBlank(message = "{oauth2.application.clientName.notBlank}", groups = {Update.class, Default.class})
    private String clientName;

    /**
     * 客户端Logo
     */
    @Schema(title = "客户端Logo")
    private String clientLogo;

    /**
     * 客户端描述
     */
    @Schema(title = "客户端描述")
    private String description;

    /**
     * 客户端认证方式
     */
    @Schema(title = "客户端认证方式")
    @NotNull(message = "{oauth2.application.clientAuthenticationMethods.NotNull}", groups = {Update.class, Default.class})
    @Size(min = 1, message = "{oauth2.application.clientAuthenticationMethods.Min}", groups = {Update.class, Default.class})
    private Set<String> clientAuthenticationMethods;

    /**
     * 客户端支持的grant type
     */
    @Schema(title = "客户端支持的grant type")
    @NotNull(message = "{oauth2.application.authorizationGrantTypes.NotNull}", groups = {Update.class, Default.class})
    @Size(min = 1, message = "{oauth2.application.authorizationGrantTypes.Min}", groups = {Update.class, Default.class})
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
    private BasicClientSettings clientSettings;

    /**
     * 客户端申请的access token设置
     */
    @Schema(title = "客户端申请的access token设置")
    private BasicTokenSettings tokenSettings;

}
