package com.basic.cloud.system.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 重置密码入参
 *
 * @author vains
 */
@Data
@Schema(name = "重置密码入参")
public class ResetPasswordRequest implements Serializable {

    @NotNull
    @Schema(title = "用户id")
    private Long userId;

    @NotBlank
    @Schema(title = "新的密码")
    private String password;

}
