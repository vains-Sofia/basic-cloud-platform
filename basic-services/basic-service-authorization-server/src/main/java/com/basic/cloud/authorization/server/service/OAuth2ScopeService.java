package com.basic.cloud.authorization.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.basic.cloud.authorization.server.domain.ScopeWithDescription;
import com.basic.cloud.authorization.server.domain.entity.OAuth2Scope;
import com.basic.cloud.authorization.server.domain.request.FindScopePageRequest;
import com.basic.cloud.authorization.server.domain.request.SaveScopeRequest;
import com.basic.cloud.authorization.server.domain.response.FindScopeResponse;
import com.basic.framework.core.domain.PageResult;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

/**
 * 针对表【oauth2_scope(oauth2客户端的scope)】的数据库操作Service
 *
 * @author vains
 */
public interface OAuth2ScopeService extends IService<OAuth2Scope> {

    /**
     * 根据入参分页查询scope信息
     *
     * @param request 分页参数
     * @return scope信息
     */
    PageResult<FindScopeResponse> findScopePage(@Valid FindScopePageRequest request);

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

}
