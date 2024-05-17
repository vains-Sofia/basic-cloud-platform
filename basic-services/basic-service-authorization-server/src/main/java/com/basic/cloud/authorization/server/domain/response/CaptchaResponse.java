package com.basic.cloud.authorization.server.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取验证码返回
 *
 * @author vains
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "获取验证码返回", description = "获取验证码响应类")
public class CaptchaResponse {

    @Schema(description = "验证码id")
    private String captchaId;

    @Schema(description = "验证码的值")
    private String code;

}
