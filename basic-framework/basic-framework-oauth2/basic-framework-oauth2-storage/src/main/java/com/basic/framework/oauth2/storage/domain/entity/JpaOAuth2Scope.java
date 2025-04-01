package com.basic.framework.oauth2.storage.domain.entity;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * oauth2客户端的scope
 *
 * @author vains
 */
@Data
@Entity
@Table(name = "oauth2_scope")
@EqualsAndHashCode(callSuper = true)
public class JpaOAuth2Scope extends BasicAuditorEntity implements Serializable {
    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * scope 名称
     */
    private String scope;

    /**
     * scope 描述
     */
    private String description;

    /**
     * 是否启用
     */
    private Boolean enabled;
}