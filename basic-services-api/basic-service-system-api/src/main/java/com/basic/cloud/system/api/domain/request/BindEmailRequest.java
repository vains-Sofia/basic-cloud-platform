package com.basic.cloud.system.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 绑定email入参
 *
 * @author YuJx
 */
@Data
public class BindEmailRequest implements Serializable {

    @Email
    @NotBlank
    @Schema(title = "邮箱地址", description = "用户绑定的邮箱地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank
    @Schema(title = "验证码", description = "用户绑定邮箱时的验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String emailCaptcha;

}
