package com.basic.cloud.oauth2.authorization.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.cloud.mybatis.plus.domain.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author vains
 * @since 2024-05-17
 */
@Getter
@Setter
@TableName(value = "authorization_consent", autoResultMap = true)
public class AuthorizationConsent extends BasicEntity {

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    @TableField("registered_client_id")
    private String registeredClientId;

    @TableField("principal_name")
    private String principalName;

    @TableField("authorities")
    private Set<String> authorities;
}
