package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.SysRoleClient;
import com.basic.cloud.system.api.domain.request.FindRolePageRequest;
import com.basic.cloud.system.api.domain.request.SaveRoleRequest;
import com.basic.cloud.system.api.domain.response.FindRoleResponse;
import com.basic.cloud.system.service.SysRoleService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * RBAC角色相关接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class SysRoleController implements SysRoleClient {

    private final SysRoleService sysRoleService;

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
}
