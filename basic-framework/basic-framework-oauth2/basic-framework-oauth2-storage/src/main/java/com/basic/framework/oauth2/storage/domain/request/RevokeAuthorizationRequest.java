package com.basic.framework.oauth2.storage.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 撤销令牌请求入参
 *
 * @author vains
 */
@Data
public class RevokeAuthorizationRequest implements Serializable {

    @NotBlank
    @Schema(title = "访问令牌", description = "OAuth2授权登录流程中获取的access token")
    private String accessToken;

}
