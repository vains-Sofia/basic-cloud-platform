package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.FindPermissionPageRequest;
import com.basic.cloud.system.api.domain.request.FindPermissionRequest;
import com.basic.cloud.system.api.domain.request.SavePermissionRequest;
import com.basic.cloud.system.api.domain.response.FindPermissionResponse;
import com.basic.framework.core.domain.DataPageResult;

import java.util.List;

/**
 * RBAC权限信息Service
 *
 * @author vains
 */
public interface SysPermissionService {

    /**
     * 分页查询权限信息列表
     *
     * @param request 分页查询权限信息列表入参
     * @return 权限信息
     */
    DataPageResult<FindPermissionResponse> findByPage(FindPermissionPageRequest request);

    /**
     * 查询权限详情
     *
     * @param id 权限id
     * @return 权限信息
     */
    FindPermissionResponse permissionDetails(Long id);

    /**
     * 添加或修改权限信息
     *
     * @param request 权限信息
     */
    void savePermission(SavePermissionRequest request);

    /**
     * 删除权限信息
     *
     * @param id 权限id
     */
    void removeById(Long id);

    /**
     * 刷新权限缓存
     */
    void refreshPermissionCache();

    /**
     * 查询权限信息列表
     *
     * @param request 查询权限信息列表入参
     * @return 权限信息
     */
    List<FindPermissionResponse> findPermissions(FindPermissionRequest request);

    /**
     * 根据角色id查询权限id列表
     *
     * @param roleId 角色id
     * @return 权限id列表
     */
    List<Long> findPermissionIdsByRoleId(Long roleId);

    /**
     * 根据权限id列表查询非父权限的权限id列表
     *
     * @param permissionIds 权限id列表
     * @return 非子权限的权限id列表
     */
    List<Long> findNonParentPermissions(List<Long> permissionIds);
}
