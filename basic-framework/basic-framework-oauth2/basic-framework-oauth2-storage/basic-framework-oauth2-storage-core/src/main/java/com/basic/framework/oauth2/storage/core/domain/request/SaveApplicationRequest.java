package com.basic.framework.oauth2.storage.core.domain.request;

import com.basic.framework.data.validation.group.Update;
import com.basic.framework.oauth2.storage.core.domain.model.BasicClientSettings;
import com.basic.framework.oauth2.storage.core.domain.model.BasicTokenSettings;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 保存/更新客户端信息入参
 *
 * @author vains
 */
@Data
@Schema(title = "保存/更新客户端信息入参")
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
    @NotBlank(message = "{oauth2.application.clientId.notBlank}")
    private String clientId;

    /**
     * 客户端密钥过期时间
     */
    @Schema(title = "客户端密钥过期时间")
    private LocalDateTime clientSecretExpiresAt;

    /**
     * 客户端名称
     */
    @Schema(title = "客户端名称")
    @NotBlank(message = "{oauth2.application.clientName.notBlank}")
    private String clientName;

    /**
     * 客户端Logo
     */
    @Schema(title = "客户端Logo")
    private String clientLogo;

    /**
     * 客户端认证方式
     */
    @Schema(title = "客户端认证方式")
    @NotNull(message = "{oauth2.application.clientAuthenticationMethods.NotNull}")
    @Size(min = 1, message = "{oauth2.application.clientAuthenticationMethods.Min}")
    private Set<String> clientAuthenticationMethods;

    /**
     * 客户端支持的grant type
     */
    @Schema(title = "客户端支持的grant type")
    @NotNull(message = "{oauth2.application.authorizationGrantTypes.NotNull}")
    @Size(min = 1, message = "{oauth2.application.authorizationGrantTypes.Min}")
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
