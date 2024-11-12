package com.basic.framework.oauth2.storage.mybatis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
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
@TableName(value = "oauth2_authorization_consent", autoResultMap = true)
public class MybatisOAuth2AuthorizationConsent extends BasicEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @TableField("registered_client_id")
    private String registeredClientId;

    @TableField("principal_name")
    private String principalName;

    @TableField("authorities")
    private Set<String> authorities;
}
