package com.basic.cloud.system.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求入参
 *
 * @author vains
 */
@Data
@Schema(name = "用户注册请求入参")
public class UserRegisterRequest implements Serializable {

    @Schema(title = "昵称")
    private String nickname;

    @Schema(title = "密码")
    private String password;

    @Email
    @NotBlank
    @Schema(title = "邮箱地址")
    private String email;

    @NotBlank
    @Schema(title = "邮箱验证码")
    private String emailCaptcha;

}
