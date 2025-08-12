package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.model.DynamicRouter;
import com.basic.cloud.system.api.domain.request.FindPermissionPageRequest;
import com.basic.cloud.system.api.domain.request.FindPermissionRequest;
import com.basic.cloud.system.api.domain.request.SavePermissionRequest;
import com.basic.cloud.system.api.domain.response.FindPermissionResponse;
import com.basic.framework.core.domain.DataPageResult;

import java.util.List;

/**
 * SysPermissionService defines a set of methods for managing system permissions,
 * including querying, modifying, and caching permission data. It facilitates
 * operations to ensure effective handling of permission-related functionalities.
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
    List<String> findPermissionIdsByRoleId(Long roleId);

    /**
     * 根据权限id列表查询非父权限的权限id列表
     *
     * @param permissionIds 权限id列表
     * @return 非子权限的权限id列表
     */
    List<String> findNonParentPermissions(List<Long> permissionIds);

    /**
     * 批量修改权限信息
     *
     * @param requests 权限信息列表
     */
    void batchUpdatePermissions(List<SavePermissionRequest> requests);

    /**
     * 检索当前用户的动态菜单路由器列表。
     *
     * @return 包装 DynamicRouter 对象列表的结果，表示用户的菜单结构
     */
    List<DynamicRouter> findUserRouters();

}
