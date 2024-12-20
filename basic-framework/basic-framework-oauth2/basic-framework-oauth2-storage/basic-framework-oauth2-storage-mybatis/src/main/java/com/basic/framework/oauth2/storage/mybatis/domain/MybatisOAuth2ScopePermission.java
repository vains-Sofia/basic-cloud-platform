package com.basic.framework.oauth2.storage.mybatis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * oauth2 scope与权限关联表
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "oauth2_scope_permission")
public class MybatisOAuth2ScopePermission extends BasicEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
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