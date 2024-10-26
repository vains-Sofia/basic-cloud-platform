package com.basic.framework.oauth2.storage.core.service;

import com.basic.framework.oauth2.storage.core.domain.BasicAuthorizationConsent;

/**
 * oauth2认证信息服务抽象
 *
 * @author vains
 */
public interface BasicAuthorizationConsentService {

    /**
     * 保存或更新授权信息
     *
     * @param basicAuthorizationConsent 授权确认信息
     */
    void save(BasicAuthorizationConsent basicAuthorizationConsent);

    /**
     * 删除授权信息
     *
     * @param basicAuthorizationConsent 授权确认信息
     */
    void remove(BasicAuthorizationConsent basicAuthorizationConsent);

    /**
     * 根据客户端id与用户查询授权确认信息
     *
     * @param registeredClientId 客户端id
     * @param principalName      用户名
     * @return 认证信息
     */
    BasicAuthorizationConsent findById(String registeredClientId, String principalName);

}