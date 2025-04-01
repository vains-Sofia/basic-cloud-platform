package com.basic.framework.oauth2.storage.jpa.repository;

import com.basic.framework.oauth2.storage.jpa.domain.JpaOAuth2Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * oauth2 scope 的 repository
 *
 * @author vains
 */
public interface OAuth2ScopeRepository extends
        JpaRepository<JpaOAuth2Scope, Long>, JpaSpecificationExecutor<JpaOAuth2Scope> {

    /**
     * 根据scope和是否启用查询
     *
     * @param scope   scope名称
     * @param enabled 是否启用
     * @return 列表
     */
    Optional<JpaOAuth2Scope> findByScopeAndEnabled(String scope, Boolean enabled);

}
