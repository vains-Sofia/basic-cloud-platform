package com.basic.framework.oauth2.storage.repository;

import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2ScopePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

/**
 * oauth2 scope与权限关联repository
 *
 * @author vains
 */
public interface OAuth2ScopePermissionRepository extends
        JpaRepository<JpaOAuth2ScopePermission, Long>, JpaSpecificationExecutor<JpaOAuth2ScopePermission> {

    /**
     * 根据scope删除关联关系
     *
     * @param scope scope名称
     */
    void deleteByScope(String scope);

    /**
     * 根据scope查询权限关联数据
     *
     * @param scopes 客户端的scope
     * @return scope与权限关联数据
     */
    List<JpaOAuth2ScopePermission> findByScopeIn(Collection<String> scopes);
}
