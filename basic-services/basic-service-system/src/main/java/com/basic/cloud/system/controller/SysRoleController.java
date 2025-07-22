package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.SysRoleClient;
import com.basic.cloud.system.api.domain.request.FindRolePageRequest;
import com.basic.cloud.system.api.domain.request.FindRoleRequest;
import com.basic.cloud.system.api.domain.request.SaveRoleRequest;
import com.basic.cloud.system.api.domain.request.UpdateRolePermissionsRequest;
import com.basic.cloud.system.api.domain.response.FindRoleResponse;
import com.basic.cloud.system.domain.SysUserRole;
import com.basic.cloud.system.repository.SysUserRoleRepository;
import com.basic.cloud.system.service.SysRoleService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RBAC角色相关接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class SysRoleController implements SysRoleClient {

    private final SysRoleService sysRoleService;

    private final SysUserRoleRepository userRoleRepository;

    @Override
    public Result<DataPageResult<FindRoleResponse>> findByPage(FindRolePageRequest request) {
        DataPageResult<FindRoleResponse> pageResult = sysRoleService.findByPage(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<FindRoleResponse> roleDetails(Long id) {
        FindRoleResponse roleResponse = sysRoleService.roleDetails(id);
        return Result.success(roleResponse);
    }

    @Override
    public Result<String> insertRole(SaveRoleRequest request) {
        // 置空id，防止插入变修改
        request.setId(null);
        sysRoleService.saveRole(request);
        return Result.success();
    }

    @Override
    public Result<String> updateRole(SaveRoleRequest request) {
        sysRoleService.saveRole(request);
        return Result.success();
    }

    @Override
    public Result<String> removeById(Long id) {
        sysRoleService.removeById(id);
        return Result.success();
    }

    @Override
    public Result<List<String>> findRoleIdsByUserId(Long userId) {
        List<SysUserRole> userRoles = userRoleRepository.findByUserId(userId);
        if (ObjectUtils.isEmpty(userRoles)) {
            return Result.success(null);
        }
        return Result.success(userRoles.stream().map(SysUserRole::getRoleId).map(String::valueOf).toList());
    }

    @Override
    public Result<List<FindRoleResponse>> findRoles(FindRoleRequest request) {
        List<FindRoleResponse> roles = sysRoleService.findRoles(request);
        return Result.success(roles);
    }

    @Override
    public Result<String> updateRolePermissions(UpdateRolePermissionsRequest request) {
        sysRoleService.updateRolePermissions(request);
        return Result.success();
    }
}
