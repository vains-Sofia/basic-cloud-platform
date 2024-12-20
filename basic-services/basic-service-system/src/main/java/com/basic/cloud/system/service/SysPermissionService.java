package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.FindPermissionPageRequest;
import com.basic.cloud.system.api.domain.request.SavePermissionRequest;
import com.basic.cloud.system.api.domain.response.FindPermissionResponse;
import com.basic.framework.core.domain.DataPageResult;

/**
 * RBAC权限信息Service
 *
 * @author YuJx
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

}
