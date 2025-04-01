package com.basic.framework.oauth2.storage.repository;

import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OAuth2ApplicationRepository extends
        JpaRepository<JpaOAuth2Application, Long>, JpaSpecificationExecutor<JpaOAuth2Application> {

    /**
     * 根据客户端id查询客户端信息
     *
     * @param clientId 客户端id
     * @return 客户端信息
     */
    Optional<JpaOAuth2Application> findByClientId(String clientId);

    /**
     * 根据客户端删除客户端信息
     *
     * @param clientId 客户端id
     */
    void deleteByClientId(String clientId);

}