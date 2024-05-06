package com.basic.cloud.authorization.server.controller;

import com.basic.cloud.authorization.server.domain.dto.TestValidationDto;
import com.basic.cloud.core.domain.Result;
import com.basic.cloud.data.validation.annotation.Phone;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
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
    @PreAuthorize("hasAnyAuthority('SCOPE_profile')")
    public String test01() {
        return "Hello, test01";
    }

    @GetMapping("/validatePhone")
    public Result<String> validatePhone(@NotBlank @Phone String phone) {
        return Result.success(phone);
    }

    @Operation(summary = "测试表单验证", description = "测试表单验证")
    @PostMapping(value = "/validateForm", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result<TestValidationDto> validateForm(@Valid TestValidationDto validationDto) {
        return Result.success(validationDto);
    }

    @PostMapping("/validateJson")
    public Result<TestValidationDto> validateJson(@Valid @RequestBody TestValidationDto validationDto) {
        return Result.success(validationDto);
    }

}
