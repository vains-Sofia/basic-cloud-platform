package com.basic.framework.oauth2.storage.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 分页查询认证信息响应bean
 *
 * @author vains
 */
@Data
@Schema(name = "分页查询认证信息响应bean")
public class FindAuthorizationResponse implements Serializable {

    @Schema(title = "数据id")
    private String id;

    @Schema(title = "认证时使用的客户端id")
    private String registeredClientId;

    @Schema(title = "认证时使用的客户端名称")
    private String registeredClientName;

    @Schema(title = "认证时使用的客户端Logo")
    private String registeredClientLogo;

    @Schema(title = "认证用户名")
    private String principalName;

    @Schema(title = "认证时使用的模式(grant type)")
    private String authorizationGrantType;

    @Schema(title = "用户授权申请的scope", description = "最终存储的还是授权确认的scope")
    private Set<String> authorizedScopes;

    @Schema(title = "授权码的值")
    private String authorizationCodeValue;

    @Schema(title = "授权码签发时间")
    private LocalDateTime authorizationCodeIssuedAt;

    @Schema(title = "授权码过期时间")
    private LocalDateTime authorizationCodeExpiresAt;

    @Schema(title = "授权码已失效", description = "如果授权码已被使用或已过期，则为true")
    private Boolean authorizationCodeInvalidated;

    @Schema(title = "认证后签发的access token")
    private String accessTokenValue;

    @Schema(title = "access token 签发时间")
    private LocalDateTime accessTokenIssuedAt;

    @Schema(title = "access token 过期时间")
    private LocalDateTime accessTokenExpiresAt;

    @Schema(title = "access token 是否已失效", description = "如果access token已过期或被撤销，则为true")
    private Boolean accessTokenInvalidated;

    @Schema(title = "access token类型", description = "一般是bearer")
    private String accessTokenType;

    @Schema(title = "access token中包含的scope", description = "授权确认过的scope")
    private String accessTokenScopes;

    @Schema(title = "认证后签发的 refresh token")
    private String refreshTokenValue;

    @Schema(title = "refresh token 签发时间")
    private LocalDateTime refreshTokenIssuedAt;

    @Schema(title = "refresh token 过期时间")
    private LocalDateTime refreshTokenExpiresAt;

    @Schema(title = "refresh token 是否已失效", description = "如果refresh token已过期或被撤销，则为true")
    private Boolean refreshTokenInvalidated;

    @Schema(title = "认证后签发的 oidc id token")
    private String oidcIdTokenValue;

    @Schema(title = "oidc id token 签发时间")
    private LocalDateTime oidcIdTokenIssuedAt;

    @Schema(title = "oidc id token 过期时间")
    private LocalDateTime oidcIdTokenExpiresAt;

    @Schema(title = "oidc id token 是否已失效", description = "如果oidc id token已过期或被撤销，则为true")
    private Boolean oidcIdTokenInvalidated;

    @Schema(title = "设备码模式(Device Flow)中的 user code")
    private String userCodeValue;

    @Schema(title = "user code 签发时间")
    private LocalDateTime userCodeIssuedAt;

    @Schema(title = "user code 过期时间")
    private LocalDateTime userCodeExpiresAt;

    @Schema(title = "user code 是否已失效", description = "如果user code已过期或被撤销，则为true")
    private Boolean userCodeInvalidated;

    @Schema(title = "设备码模式(Device Flow)中的 device code")
    private String deviceCodeValue;

    @Schema(title = "device code 签发时间")
    private LocalDateTime deviceCodeIssuedAt;

    @Schema(title = "device code 过期时间")
    private LocalDateTime deviceCodeExpiresAt;

    @Schema(title = "device code 是否已失效", description = "如果device code已过期或被撤销，则为true")
    private Boolean deviceCodeInvalidated;

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
