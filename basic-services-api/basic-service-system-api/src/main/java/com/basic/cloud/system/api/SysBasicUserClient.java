package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.FindBasicUserPageRequest;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 基础用户信息api
 *
 * @author vains
 */
@Validated
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
    @Operation(summary = "根据邮箱获取用户信息", description = "根据用户首选的邮箱地址获取用户信息")
    Result<BasicUserResponse> getByEmail(@Email @NotBlank @PathVariable String email);

    /**
     * 分页查询基础用户信息列表
     *
     * @param request 分页查询基础用户信息列表入参
     * @return 用户信息
     */
    @GetMapping("/findByPage")
    @Operation(summary = "分页查询基础用户信息列表", description = "分页查询基础用户信息列表")
    Result<PageResult<FindBasicUserResponse>> findByPage(@SpringQueryMap FindBasicUserPageRequest request);

    /**
     * 查询用户详情
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/userDetails/{id}")
    @Operation(summary = "查询用户详情", description = "根据用户id查询用户详情")
    Result<BasicUserResponse> userDetails(@NotNull @PathVariable Long id);

}
