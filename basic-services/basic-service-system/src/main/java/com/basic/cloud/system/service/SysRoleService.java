package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.FindRolePageRequest;
import com.basic.cloud.system.api.domain.request.SaveRoleRequest;
import com.basic.cloud.system.api.domain.response.FindRoleResponse;
import com.basic.framework.core.domain.DataPageResult;

/**
 * RBAC角色 Service 接口
 *
 * @author YuJx
 */
public interface SysRoleService {

    /**
     * 分页查询角色信息列表
     *
     * @param request 分页查询角色信息列表入参
     * @return 角色信息
     */
    DataPageResult<FindRoleResponse> findByPage(FindRolePageRequest request);

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
    void saveRole(SaveRoleRequest request);

    /**
     * 删除角色信息
     *
     * @param id 角色id
     */
    void removeById(Long id);
}
