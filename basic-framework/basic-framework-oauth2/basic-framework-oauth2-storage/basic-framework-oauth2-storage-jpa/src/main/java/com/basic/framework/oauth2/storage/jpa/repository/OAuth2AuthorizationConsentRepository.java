package com.basic.framework.oauth2.storage.jpa.repository;

import com.basic.framework.oauth2.storage.jpa.domain.JpaOAuth2AuthorizationConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OAuth2AuthorizationConsentRepository extends
        JpaRepository<JpaOAuth2AuthorizationConsent, Long>, JpaSpecificationExecutor<JpaOAuth2AuthorizationConsent> {

    /**
     * 根据客户端id和授权确认用户名查询授权确认信息
     *
     * @param registeredClientId 客户端id
     * @param principalName      用户名
     * @return 授权确认信息
     */
    Optional<JpaOAuth2AuthorizationConsent> findByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

    /**
     * 根据客户端id和授权确认用户名删除授权确认信息
     *
     * @param registeredClientId 客户端id
     * @param principalName      用户名
     */
    void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}