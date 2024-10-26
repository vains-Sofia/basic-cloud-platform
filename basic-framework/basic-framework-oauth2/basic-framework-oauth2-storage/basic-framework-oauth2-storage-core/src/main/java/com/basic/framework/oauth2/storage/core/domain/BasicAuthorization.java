package com.basic.framework.oauth2.storage.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * oauth2认证信息
 *
 * @author vains
 */
@Data
@Schema(name = "oauth2认证信息")
public class BasicAuthorization {

    @Schema(title = "数据id")
    private String id;

    @Schema(title = "认证时使用的客户端id")
    private String registeredClientId;

    @Schema(title = "认证用户名")
    private String principalName;

    @Schema(title = "认证时使用的模式(grant type)")
    private String authorizationGrantType;

    @Schema(title = "用户申请并确认的scope")
    private Set<String> authorizedScopes;

    @Schema(title = "属性")
    private String attributes;

    @Schema(title = "state")
    private String state;

    @Schema(title = "授权码的值")
    private String authorizationCodeValue;

    @Schema(title = "授权码签发时间")
    private LocalDateTime authorizationCodeIssuedAt;

    @Schema(title = "授权码过期时间")
    private LocalDateTime authorizationCodeExpiresAt;

    @Schema(title = "授权码元数据")
    private String authorizationCodeMetadata;

    @Schema(title = "认证后签发的access token")
    private String accessTokenValue;

    @Schema(title = "access token 签发时间")
    private LocalDateTime accessTokenIssuedAt;

    @Schema(title = "access token 过期时间")
    private LocalDateTime accessTokenExpiresAt;

    @Schema(title = "access token 元数据")
    private String accessTokenMetadata;

    @Schema(title = "access token类型", description = "一般是bearer")
    private String accessTokenType;

    @Schema(title = "access token中包含的scope")
    private String accessTokenScopes;

    @Schema(title = "认证后签发的 refresh token")
    private String refreshTokenValue;

    @Schema(title = "refresh token 签发时间")
    private LocalDateTime refreshTokenIssuedAt;

    @Schema(title = "refresh token 过期时间")
    private LocalDateTime refreshTokenExpiresAt;

    @Schema(title = "refresh token 元数据")
    private String refreshTokenMetadata;

    @Schema(title = "认证后签发的 oidc id token")
    private String oidcIdTokenValue;

    @Schema(title = "oidc id token 签发时间")
    private LocalDateTime oidcIdTokenIssuedAt;

    @Schema(title = "oidc id token 过期时间")
    private LocalDateTime oidcIdTokenExpiresAt;

    @Schema(title = "oidc id token 元数据")
    private String oidcIdTokenMetadata;

    @Schema(title = "oidc id token 声明(Claims)信息", description = "一般情况下是用户数据")
    private String oidcIdTokenClaims;

    @Schema(title = "设备码模式(Device Flow)中的 user code")
    private String userCodeValue;

    @Schema(title = "user code 签发时间")
    private LocalDateTime userCodeIssuedAt;

    @Schema(title = "user code 过期时间")
    private LocalDateTime userCodeExpiresAt;

    @Schema(title = "user code 元数据")
    private String userCodeMetadata;

    @Schema(title = "设备码模式(Device Flow)中的 device code")
    private String deviceCodeValue;

    @Schema(title = "device code 签发时间")
    private LocalDateTime deviceCodeIssuedAt;

    @Schema(title = "device code 过期时间")
    private LocalDateTime deviceCodeExpiresAt;

    @Schema(title = "device code 元数据")
    private String deviceCodeMetadata;

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
