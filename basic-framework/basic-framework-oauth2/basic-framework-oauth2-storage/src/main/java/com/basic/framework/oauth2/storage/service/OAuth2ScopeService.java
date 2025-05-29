package com.basic.framework.oauth2.storage.service;

import com.basic.framework.core.domain.PageResult;
import com.basic.framework.oauth2.storage.domain.model.ScopeWithDescription;
import com.basic.framework.oauth2.storage.domain.request.FindScopePageRequest;
import com.basic.framework.oauth2.storage.domain.request.ResetScopePermissionRequest;
import com.basic.framework.oauth2.storage.domain.request.SaveScopeRequest;
import com.basic.framework.oauth2.storage.domain.response.FindScopeResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

/**
 * 针对表【oauth2_scope(oauth2客户端的scope)】的数据库操作Service
 *
 * @author vains
 */
public interface OAuth2ScopeService {

    /**
     * 根据入参分页查询scope信息
     *
     * @param request 分页参数
     * @return scope信息
     */
    PageResult<FindScopeResponse> findScopePage(@Valid FindScopePageRequest request);

    /**
     * 查询所有scope信息
     *
     * @return scope信息
     */
    List<FindScopeResponse> findScopeAll();

    /**
     * 根据scope 名查询列表
     *
     * @param scopes scope 名
     * @return 列表
     */
    List<ScopeWithDescription> findByScopes(Set<String> scopes);

    /**
     * 保存scope数据
     *
     * @param request scope数据
     */
    void saveScope(@Valid SaveScopeRequest request);

    /**
     * 修改scope数据
     *
     * @param request scope数据
     */
    void updateScope(SaveScopeRequest request);

    /**
     * 重置scope的权限
     *
     * @param request 重置scope权限入参
     */
    void resetScopePermission(ResetScopePermissionRequest request);

    /**
     * 根据ID删除scope信息
     *
     * @param id scope ID
     */
    void removeScopeById(Long id);

    /**
     * 根据scope查询权限ID
     *
     * @param scope scope名称
     * @return 权限ID列表
     */
    List<Long> findPermissionIdsByScope(String scope);
}
