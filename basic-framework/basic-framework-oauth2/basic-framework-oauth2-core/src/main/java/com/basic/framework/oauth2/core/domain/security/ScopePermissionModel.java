package com.basic.framework.oauth2.core.domain.security;

import lombok.Data;

import java.io.Serializable;

/**
 * scope与权限关联
 *
 * @author vains
 */
@Data
public class ScopePermissionModel implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * oauth2 scope名
     */
    private String scope;

    /**
     * 权限id
     */
    private Long permissionId;

}
