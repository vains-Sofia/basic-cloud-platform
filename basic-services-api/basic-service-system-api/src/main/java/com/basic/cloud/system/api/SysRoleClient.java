package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.FindRolePageRequest;
import com.basic.cloud.system.api.domain.request.SaveRoleRequest;
import com.basic.cloud.system.api.domain.response.FindRoleResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.data.validation.group.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * RBAC角色相关接口
 *
 * @author vains
 */
@RequestMapping("/role")
@Tag(name = "RBAC角色相关接口", description = "RBAC角色相关接口")
@FeignClient(name = FeignConstants.SYSTEM_APPLICATION, path = FeignConstants.SYSTEM_CONTEXT_PATH, contextId = "SysRoleClient")
public interface SysRoleClient {

    /**
     * 分页查询角色信息列表
     *
     * @param request 分页查询角色信息列表入参
     * @return 角色信息
     */
    @GetMapping("/findByPage")
    @Operation(summary = "分页查询角色信息列表", description = "分页查询角色信息列表")
    Result<DataPageResult<FindRoleResponse>> findByPage(@Valid @SpringQueryMap FindRolePageRequest request);

    /**
     * 查询角色详情
     *
     * @param id 角色id
     * @return 角色详情数据
     */
    @GetMapping("/roleDetails/{id}")
    @Parameter(name = "id", description = "角色id")
    @Operation(summary = "查询权限详情", description = "根据权限id查询权限详情")
    Result<FindRoleResponse> roleDetails(@Valid @NotNull @PathVariable Long id);

    /**
     * 添加角色信息
     *
     * @param request 角色信息
     * @return 统一响应
     */
    @PostMapping("/insertRole")
    @Operation(summary = "添加角色信息", description = "添加角色信息")
    Result<String> insertRole(@Validated @RequestBody SaveRoleRequest request);

    /**
     * 修改角色信息
     *
     * @param request 角色信息
     * @return 统一响应
     */
    @PutMapping("/updateRole")
    @Operation(summary = "修改角色信息", description = "修改角色信息")
    Result<String> updateRole(@Validated(Update.class) @RequestBody SaveRoleRequest request);

    /**
     * 删除角色信息
     *
     * @param id 角色id
     * @return 统一响应
     */
    @DeleteMapping("/removeById/{id}")
    @Parameter(name = "id", description = "角色id")
    @Operation(summary = "删除角色信息", description = "删除角色信息")
    Result<String> removeById(@Valid @NotNull @PathVariable Long id);

}
