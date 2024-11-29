package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.FindBasicUserPageRequest;
import com.basic.cloud.system.api.domain.request.UserRegisterRequest;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * 基础用户信息api
 *
 * @author vains
 */
@RequestMapping("/user")
@Tag(name = "基础用户信息接口", description = "基础用户信息接口")
@FeignClient(name = FeignConstants.SYSTEM_APPLICATION, path = FeignConstants.SYSTEM_CONTEXT_PATH, contextId = "SysBasicUserClient")
public interface SysBasicUserClient {

    /**
     * 根据用户邮件地址获取用户基础信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @GetMapping("/getByEmail/{email}")
    @Parameter(name = "email", description = "用户首选的邮箱地址")
    @Operation(summary = "根据邮箱获取用户信息", description = "根据用户首选的邮箱地址获取用户信息", hidden = true)
    Result<BasicUserResponse> getByEmail(@Valid @Email @NotBlank @PathVariable String email);

    /**
     * 分页查询基础用户信息列表
     *
     * @param request 分页查询基础用户信息列表入参
     * @return 用户信息
     */
    @GetMapping("/findByPage")
    @Operation(summary = "分页查询基础用户信息列表", description = "分页查询基础用户信息列表")
    Result<PageResult<FindBasicUserResponse>> findByPage(@Valid @SpringQueryMap FindBasicUserPageRequest request);

    /**
     * 查询用户详情
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/userDetails/{id}")
    @Parameter(name = "id", description = "用户id")
    @Operation(summary = "查询用户详情", description = "根据用户id查询用户详情")
    Result<BasicUserResponse> userDetails(@Valid @NotNull @PathVariable Long id);

    /**
     * 根据邮箱获取验证码
     *
     * @param email 邮箱
     * @return 统一响应
     */
    @GetMapping("/getRegisterEmailCode/{email}")
    @Parameter(name = "email", description = "邮箱地址")
    @Operation(summary = "根据邮箱获取验证码", description = "获取注册时使用的邮箱验证码")
    Result<String> getRegisterEmailCode(@Valid @NotBlank @Email @PathVariable String email);

    /**
     * 用户注册
     *
     * @param request 用户注册入参
     * @return 统一响应
     */
    @PostMapping("/userRegister")
    @Operation(summary = "用户注册", description = "用户注册")
    Result<String> userRegister(@Valid @RequestBody UserRegisterRequest request);

}
