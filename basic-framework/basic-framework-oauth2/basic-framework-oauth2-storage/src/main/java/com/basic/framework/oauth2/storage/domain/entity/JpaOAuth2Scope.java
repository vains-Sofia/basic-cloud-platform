package com.basic.framework.oauth2.storage.domain.entity;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

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
    @Comment(value = "主键id")
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * scope 名称
     */
    @Comment(value = "scope 名称")
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * scope 编码
     */
    @Comment(value = "scope 编码")
    @Column(name = "scope", nullable = false)
    private String scope;

    /**
     * scope 描述
     */
    @Column(name = "description")
    @Comment(value = "scope 描述")
    private String description;

    /**
     * 是否启用
     */
    @Column(name = "enabled")
    @Comment(value = "是否启用")
    private Boolean enabled;
}