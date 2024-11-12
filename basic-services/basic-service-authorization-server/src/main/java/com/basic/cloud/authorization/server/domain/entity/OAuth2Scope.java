package com.basic.cloud.authorization.server.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * oauth2客户端的scope
 *
 * @author vains
 */
@Data
@TableName(value = "oauth2_scope")
@EqualsAndHashCode(callSuper = true)
public class OAuth2Scope extends BasicEntity implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * scope 名称
     */
    private String name;

    /**
     * scope 描述
     */
    private String description;

    /**
     * 是否启用
     */
    private Boolean enabled;
}