package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.*;
import com.basic.cloud.system.api.domain.response.AuthenticatedUserResponse;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.data.validation.group.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
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
     * @param username 用户账号
     * @return 用户信息
     */
    @GetMapping("/getByUsername/{username}")
    @Parameter(name = "username", description = "用户账号")
    @Operation(summary = "根据用户账号获取用户信息", description = "根据用户账号获取用户信息", hidden = true)
    Result<BasicUserResponse> getByUsername(@Valid @NotBlank @PathVariable String username);

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
    Result<FindBasicUserResponse> userDetails(@Valid @NotNull @PathVariable Long id);

    /**
     * 根据邮箱获取验证码
     *
     * @param email 邮箱
     * @return 统一响应
     */
    @GetMapping("/getRegisterEmailCode/{email}")
    @Parameter(name = "email", description = "邮箱地址")
    @Operation(summary = "获取注册时使用的邮箱验证码", description = "给传入邮箱发送验证码")
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

    /**
     * 重置密码
     *
     * @param request 重置密码入参
     * @return 统一响应
     */
    @PutMapping("/resetPassword")
    @Operation(summary = "重置密码", description = "重置密码")
    Result<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request);

    /**
     * 添加一条用户信息
     *
     * @param request 用户信息
     * @return 统一响应
     */
    @PostMapping("/insertBasicUser")
    @Operation(summary = "添加一条用户信息", description = "添加一条用户信息")
    Result<String> insertBasicUser(@Validated @RequestBody SaveBasicUserRequest request);

    /**
     * 修改用户信息
     *
     * @param request 用户信息
     * @return 统一响应
     */
    @PutMapping("/updateBasicUser")
    @Operation(summary = "修改用户信息", description = "修改用户信息")
    Result<String> updateBasicUser(@Validated(Update.class) @RequestBody SaveBasicUserRequest request);

    /**
     * 删除用户信息
     *
     * @param id 用户id
     * @return 统一响应
     */
    @DeleteMapping("/removeById/{id}")
    @Parameter(name = "id", description = "用户id")
    @Operation(summary = "删除用户信息", description = "删除用户信息")
    Result<String> removeById(@Valid @NotNull @PathVariable Long id);

    /**
     * 获取登录用户信息
     *
     * @return 统一响应
     */
    @GetMapping("/loginUserinfo")
    @Operation(summary = "获取登录用户信息", description = "获取登录用户信息")
    Result<AuthenticatedUserResponse> loginUserinfo();

    /**
     * 更新用户角色
     *
     * @param request 更新用户角色入参
     * @return 统一响应
     */
    @PutMapping("/updateUserRoles")
    @Operation(summary = "更新用户角色", description = "更新用户角色")
    Result<String> updateUserRoles(@Valid @RequestBody UpdateUserRolesRequest request);

}
