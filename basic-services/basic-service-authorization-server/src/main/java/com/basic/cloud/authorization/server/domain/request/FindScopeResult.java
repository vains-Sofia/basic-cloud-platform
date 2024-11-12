package com.basic.cloud.authorization.server.domain.request;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * oauth2客户端的scope
 *
 * @author vains
 */
@Data
@TableName(value = "oauth2_scope")
public class FindScopeResult implements Serializable {
    /**
     * 主键id
     */
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