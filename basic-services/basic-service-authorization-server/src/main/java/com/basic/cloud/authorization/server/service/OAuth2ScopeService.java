package com.basic.cloud.authorization.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.basic.cloud.authorization.server.domain.entity.OAuth2Scope;
import com.basic.cloud.authorization.server.domain.request.FindScopePageRequest;
import com.basic.cloud.authorization.server.domain.request.FindScopeResult;
import com.basic.framework.core.domain.PageResult;

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
    PageResult<FindScopeResult> findScopePage(FindScopePageRequest request);

}
