package com.basic.cloud.system.api.domain.request;

import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 增强三方用户入参
 *
 * @author YuJx
 */
@Data
public class EnhancedThirdUserRequest implements Serializable {

    /**
     * 三方平台标识
     */
    @NotNull
    @Schema(title = "三方平台标识", description = "三方平台标识")
    private OAuth2AccountPlatformEnum provider;

    /**
     * 三方用户ID
     */
    @NotBlank
    @Schema(title = "三方用户ID", description = "三方用户ID")
    private String providerUserId;

}
