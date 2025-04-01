package com.basic.framework.oauth2.storage.domain.entity;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * oauth2 scope与权限关联表
 *
 * @author vains
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "oauth2_scope_permission")
public class JpaOAuth2ScopePermission extends BasicAuditorEntity {

    /**
     * 主键id
     */
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * oauth2 scope名
     */
    @Column(name = "scope")
    private String scope;

    /**
     * 权限id
     */
    @Column(name = "permission_id", length = 100)
    private Long permissionId;

}