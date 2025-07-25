package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.FindPermissionPageRequest;
import com.basic.cloud.system.api.domain.request.FindPermissionRequest;
import com.basic.cloud.system.api.domain.request.SavePermissionRequest;
import com.basic.cloud.system.api.domain.response.FindPermissionResponse;
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

import java.util.List;

/**
 * RBAC权限相关api
 *
 * @author vains
 */
@RequestMapping("/permission")
@Tag(name = "RBAC权限相关接口", description = "RBAC权限相关接口")
@FeignClient(name = FeignConstants.SYSTEM_APPLICATION, path = FeignConstants.SYSTEM_CONTEXT_PATH, contextId = "SysPermissionClient")
public interface SysPermissionClient {

    /**
     * 分页查询权限信息列表
     *
     * @param request 分页查询权限信息列表入参
     * @return 权限信息
     */
    @GetMapping("/findByPage")
    @Operation(summary = "分页查询权限信息列表", description = "分页查询权限信息列表")
    Result<DataPageResult<FindPermissionResponse>> findByPage(@Valid @SpringQueryMap FindPermissionPageRequest request);

    /**
     * 查询权限信息列表
     *
     * @param request 查询权限信息列表入参
     * @return 权限信息
     */
    @GetMapping("/findPermissions")
    @Operation(summary = "查询权限信息列表", description = "查询权限信息列表")
    Result<List<FindPermissionResponse>> findPermissions(@Valid @SpringQueryMap FindPermissionRequest request);

    /**
     * 查询权限详情
     *
     * @param id 权限id
     * @return 权限信息
     */
    @GetMapping("/permissionDetails/{id}")
    @Parameter(name = "id", description = "权限id")
    @Operation(summary = "查询权限详情", description = "根据权限id查询权限详情")
    Result<FindPermissionResponse> permissionDetails(@Valid @NotNull @PathVariable Long id);

    /**
     * 添加权限信息
     *
     * @param request 权限信息
     * @return 统一响应
     */
    @PostMapping("/insertPermission")
    @Operation(summary = "添加权限信息", description = "添加权限信息")
    Result<String> insertPermission(@Validated @RequestBody SavePermissionRequest request);

    /**
     * 修改权限信息
     *
     * @param request 权限信息
     * @return 统一响应
     */
    @PutMapping("/updatePermission")
    @Operation(summary = "修改权限信息", description = "修改权限信息")
    Result<String> updatePermission(@Validated(Update.class) @RequestBody SavePermissionRequest request);

    /**
     * 删除权限信息
     *
     * @param id 权限id
     * @return 统一响应
     */
    @DeleteMapping("/removeById/{id}")
    @Parameter(name = "id", description = "权限id")
    @Operation(summary = "删除权限信息", description = "删除权限信息")
    Result<String> removeById(@Valid @NotNull @PathVariable Long id);

    /**
     * 根据角色id查询权限id列表
     *
     * @param roleId 角色id
     * @return 权限id列表
     */
    @GetMapping("/findPermissionIdsByRoleId/{roleId}")
    @Operation(summary = "根据角色id查询权限id列表", description = "根据角色id查询权限id列表")
    Result<List<String>> findPermissionIdsByRoleId(@Valid @NotNull @PathVariable Long roleId);

    /**
     * 权限id列表获取没有子节点的权限id列表
     *
     * @param permissionIds 权限id列表
     * @return 权限id列表
     */
    @PostMapping("/findNonParentPermissions")
    @Operation(summary = "权限id列表获取没有子节点的权限id列表", description = "权限id列表获取没有子节点的权限id列表")
    Result<List<String>> findNonParentPermissions(@Valid @NotNull @RequestBody List<Long> permissionIds);

}
