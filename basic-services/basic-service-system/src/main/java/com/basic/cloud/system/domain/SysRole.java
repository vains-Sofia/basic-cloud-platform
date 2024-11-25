package com.basic.cloud.system.domain;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import java.util.Set;

/**
 * RBAC角色表
 *
 * @author vains
 */
@Data
@Entity
@Table(name = "sys_role")
@Comment(value = "RBAC角色表")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BasicAuditorEntity {

    /**
     * 主键id
     */
    @Id
    @Comment(value = "主键id")
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 角色代码
     */
    @Size(max = 50)
    @Comment(value = "角色代码")
    @Column(name = "code", length = 50)
    private String code;

    /**
     * 角色名称
     */
    @Size(max = 50)
    @Comment(value = "角色名称")
    @Column(name = "name", length = 50)
    private String name;

    /**
     * 角色描述
     */
    @Size(max = 255)
    @Comment(value = "角色描述")
    @Column(name = "description")
    private String description;

    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    @Comment(value = "是否已删除")
    private Boolean deleted;

    /**
     * 角色拥有的权限
     */
    @ManyToMany
    @JoinTable(
            name = "sys_role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<SysPermission> permissions;

}