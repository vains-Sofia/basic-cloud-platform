package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.BindEmailRequest;
import com.basic.cloud.system.api.domain.request.EnhancedThirdUserRequest;
import com.basic.cloud.system.api.domain.response.EnhancedUserResponse;
import com.basic.cloud.system.api.enums.CheckBindingStatusEnum;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 第三方用户绑定 api
 *
 * @author vains
 */
@RequestMapping("/third/user")
@Tag(name = "第三方用户绑定api", description = "第三方用户绑定api")
@FeignClient(name = FeignConstants.SYSTEM_APPLICATION, path = FeignConstants.SYSTEM_CONTEXT_PATH, contextId = "SysThirdUserBindClient")
public interface SysThirdUserBindClient {

    /**
     * 检查当前三方用户是否绑定本地用户
     *
     * @return 结果
     */
    @GetMapping("/check-binding")
    @Operation(summary = "检查当前三方用户是否绑定本地用户", description = "检查当前三方用户是否绑定本地用户")
    Result<CheckBindingStatusEnum> checkBinding();

    /**
     * 获取增强的三方用户信息
     *
     * @param request 增强三方用户请求参数
     * @return 增强的三方用户信息
     */
    @PostMapping("/enhanced-third-user")
    @Operation(summary = "获取增强的三方用户信息", description = "获取增强的三方用户信息")
    Result<EnhancedUserResponse> enhancedThirdUser(@Valid @RequestBody EnhancedThirdUserRequest request);

    /**
     * 绑定邮箱
     *
     * @param request 邮箱绑定请求参数
     * @return 绑定状态
     */
    @PostMapping("/bind-email")
    @Operation(summary = "绑定邮箱", description = "绑定邮箱")
    Result<CheckBindingStatusEnum> bindEmail(@Valid @RequestBody BindEmailRequest request);

    /**
     * 获取绑定邮箱验证码
     *
     * @param email 电子邮箱地址
     * @return 绑定邮箱验证码
     */
    @GetMapping("/bind-email-code/{email}")
    @Operation(summary = "获取绑定邮箱验证码", description = "获取绑定邮箱验证码")
    @Parameter(name = "email", description = "电子邮箱地址", required = true, example = "example@gmail.com")
    Result<String> sendBindEmailCode(@Valid @NotBlank @Email @PathVariable String email);

}
