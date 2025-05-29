package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.SysPermissionClient;
import com.basic.cloud.system.api.domain.request.FindPermissionPageRequest;
import com.basic.cloud.system.api.domain.request.FindPermissionRequest;
import com.basic.cloud.system.api.domain.request.SavePermissionRequest;
import com.basic.cloud.system.api.domain.response.FindPermissionResponse;
import com.basic.cloud.system.service.SysPermissionService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RBAC权限相关接口实现
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class SysPermissionController implements SysPermissionClient {

    private final SysPermissionService sysPermissionService;

    @Override
    public Result<DataPageResult<FindPermissionResponse>> findByPage(FindPermissionPageRequest request) {
        DataPageResult<FindPermissionResponse> pageResult = sysPermissionService.findByPage(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<List<FindPermissionResponse>> findPermissions(FindPermissionRequest request) {
        List<FindPermissionResponse> permissions = sysPermissionService.findPermissions(request);
        return Result.success(permissions);
    }

    @Override
    public Result<FindPermissionResponse> permissionDetails(Long id) {
        FindPermissionResponse permissionResponse = sysPermissionService.permissionDetails(id);
        return Result.success(permissionResponse);
    }

    @Override
    public Result<String> insertPermission(SavePermissionRequest request) {
        // 置空id，防止插入变修改
        request.setId(null);
        sysPermissionService.savePermission(request);
        return Result.success();
    }

    @Override
    public Result<String> updatePermission(SavePermissionRequest request) {
        sysPermissionService.savePermission(request);
        return Result.success();
    }

    @Override
    public Result<String> removeById(Long id) {
        sysPermissionService.removeById(id);
        return Result.success();
    }

    @Override
    public Result<List<Long>> findPermissionIdsByRoleId(Long roleId) {
        List<Long> permissionIds = sysPermissionService.findPermissionIdsByRoleId(roleId);
        return Result.success(permissionIds);
    }

    @Override
    public Result<List<Long>> findNonParentPermissions(List<Long> permissionIds) {
        List<Long> nonChildrenList = sysPermissionService.findNonParentPermissions(permissionIds);
        return Result.success(nonChildrenList);
    }
}
