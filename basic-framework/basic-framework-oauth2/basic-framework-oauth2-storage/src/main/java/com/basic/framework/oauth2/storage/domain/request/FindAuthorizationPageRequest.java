package com.basic.framework.oauth2.storage.domain.request;

import com.basic.framework.core.domain.DataPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 认证信息分页查询入参
 *
 * @author vains
 */
@Data
@Schema(name = "认证信息分页查询入参")
@EqualsAndHashCode(callSuper = true)
public class FindAuthorizationPageRequest extends DataPageable {

    @Schema(title = "认证时使用的客户端id", description = "认证时使用的客户端id")
    private String registeredClientId;

    @Schema(title = "认证时使用的模式(grant type)", description = "认证时使用的模式(grant type)")
    private String authorizationGrantType;

    @Schema(title = "授权码签发时间-开始", description = "授权码签发时间-开始")
    private LocalDateTime authorizationCodeIssuedStart;

    @Schema(title = "授权码签发时间-结束", description = "授权码签发时间-结束")
    private LocalDateTime authorizationCodeIssuedEnd;

    @Schema(title = "access token 签发时间-开始", description = "access token 签发时间-开始")
    private LocalDateTime accessTokenIssuedStart;

    @Schema(title = "access token 签发时间-结束", description = "access token 签发时间-结束")
    private LocalDateTime accessTokenIssuedEnd;

}
