package com.basic.framework.oauth2.storage.core.service;

import com.basic.framework.core.domain.PageResult;
import com.basic.framework.oauth2.storage.core.domain.BasicApplication;
import com.basic.framework.oauth2.storage.core.domain.request.FindApplicationPageRequest;
import com.basic.framework.oauth2.storage.core.domain.request.SaveApplicationRequest;
import com.basic.framework.oauth2.storage.core.domain.response.BasicApplicationResponse;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

/**
 * oauth2客户端信息服务接口
 *
 * @author vains
 */
public interface BasicApplicationService {

    /**
     * 保存或更新客户端
     *
     * @param basicApplication 客户端信息
     */
    void save(BasicApplication basicApplication);

    /**
     * 根据数据id查询客户端信息
     *
     * @param id 数据id
     * @return 客户端信息，不存在返回null
     */
    BasicApplication findById(String id);

    /**
     * 根据客户端id查询客户端信息
     *
     * @param clientId 客户端id
     * @return 客户端信息，不存在返回null
     */
    BasicApplication findByClientId(String clientId);

    /**
     * 根据条件分页查询客户端信息
     *
     * @param request 分页参数
     * @return 分页的客户端信息
     */
    PageResult<BasicApplicationResponse> findByPage(FindApplicationPageRequest request);

    /**
     * 创建客户端信息
     *
     * @param request 新的客户端信息
     */
    String saveApplication(SaveApplicationRequest request);

    /**
     * 更新客户端信息
     *
     * @param request 新的客户端信息
     */
    void updateApplication(SaveApplicationRequest request);

    /**
     * 根据客户端id删除客户端信息
     *
     * @param clientId 客户端id
     */
    void removeByClientId(String clientId);

    /**
     * 验证回调地址
     *
     * @param request 保存/更新客户端入参
     */
    default void validRedirectUris(SaveApplicationRequest request) {
        if (request.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE.getValue())) {
            // 授权码/PKCE模式下回调地址不能为空
            Assert.notEmpty(request.getRedirectUris(), "回调地址不能为空.");
        }
    }

}
