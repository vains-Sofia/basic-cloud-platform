package com.basic.framework.oauth2.storage.service;

import com.basic.framework.core.domain.PageResult;
import com.basic.framework.oauth2.storage.domain.request.FindAuthorizationPageRequest;
import com.basic.framework.oauth2.storage.domain.response.FindAuthorizationResponse;
import com.basic.framework.oauth2.storage.domain.security.BasicAuthorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

/**
 * oauth2认证信息服务抽象
 *
 * @author vains
 */
public interface BasicAuthorizationService {

    /**
     * 保存或更新认证信息
     *
     * @param basicAuthorization 认证信息
     */
    void save(BasicAuthorization basicAuthorization);

    /**
     * 删除认证信息
     *
     * @param id 认证信息id
     */
    void remove(String id);

    /**
     * 根据数据id查询认证信息
     *
     * @param id 数据id
     * @return 认证信息
     */
    BasicAuthorization findById(String id);

    /**
     * 根据token和token类型查询认证信息
     *
     * @param token     值
     * @param tokenType 类型
     * @return 认证信息
     */
    BasicAuthorization findByToken(String token, OAuth2TokenType tokenType);

    /**
     * 分页查询认证信息列表
     *
     * @param request 分页请求入参
     * @return 认证信息分页列表
     */
    PageResult<FindAuthorizationResponse> findAuthorizationPage(FindAuthorizationPageRequest request);

}