package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.FindRolePageRequest;
import com.basic.cloud.system.api.domain.request.FindRoleRequest;
import com.basic.cloud.system.api.domain.request.SaveRoleRequest;
import com.basic.cloud.system.api.domain.request.UpdateRolePermissionsRequest;
import com.basic.cloud.system.api.domain.response.FindRoleResponse;
import com.basic.framework.core.domain.DataPageResult;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * RBAC角色 Service 接口
 *
 * @author vains
 */
public interface SysRoleService {

    /**
     * 分页查询角色信息列表
     *
     * @param request 分页查询角色信息列表入参
     * @return 角色信息
     */
    DataPageResult<FindRoleResponse> findByPage(@Validated FindRolePageRequest request);

    /**
     * 查询角色详情
     *
     * @param id 权限id
     * @return 角色详情数据
     */
    FindRoleResponse roleDetails(Long id);

    /**
     * 保存/修改角色数据
     *
     * @param request 新的角色信息
     */
    void saveRole(@Validated SaveRoleRequest request);

    /**
     * 删除角色信息
     *
     * @param id 角色id
     */
    void removeById(Long id);

    /**
     * 查询角色信息列表
     *
     * @param request 查询角色信息列表入参
     * @return 角色信息
     */
    List<FindRoleResponse> findRoles(@Validated FindRoleRequest request);

    /**
     * 变更角色权限
     *
     * @param request 变更角色权限入参
     */
    void updateRolePermissions(@Validated UpdateRolePermissionsRequest request);
}
