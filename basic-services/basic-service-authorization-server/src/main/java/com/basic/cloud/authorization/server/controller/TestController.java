package com.basic.cloud.authorization.server.controller;

import com.basic.cloud.authorization.server.domain.request.TestValidationRequest;
import com.basic.framework.core.domain.Result;
import com.basic.framework.data.validation.annotation.Phone;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 测试接口
 *
 * @author vains
 */
@RestController
@RequestMapping("/test")
@Tag(name = "认证服务测试接口", description = "提供认证服务集成效果")
public class TestController {

    @GetMapping("/test01")
    @PreAuthorize("hasAnyAuthority('profile')")
    @Operation(summary = "测试接口，需要有profile权限", description = "测试接口，需要有profile权限")
    public String test01() {
        return "Hello, test01";
    }

    @GetMapping("/validatePhone")
    @Parameter(name = "phone", description = "手机号")
    @Operation(summary = "测试手机号验证", description = "测试手机号验证")
    public Result<String> validatePhone(@NotBlank @Phone String phone) {
        return Result.success(phone);
    }

    @PostMapping(value = "/validateForm")
    @Operation(summary = "测试表单验证", description = "测试表单验证")
    public Result<TestValidationRequest> validateForm(@Valid TestValidationRequest validationDto) {
        return Result.success(validationDto);
    }

    @PostMapping("/validateJson")
    @Operation(summary = "测试Json请求验证", description = "测试Json请求验证")
    public Result<TestValidationRequest> validateJson(@Valid @RequestBody TestValidationRequest validationDto) {
        return Result.success(validationDto);
    }

}
