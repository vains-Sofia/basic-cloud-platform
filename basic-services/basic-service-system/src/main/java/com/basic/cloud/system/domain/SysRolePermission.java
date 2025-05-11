package com.basic.cloud.system.domain;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

/**
 * RBAC角色权限关联表
 *
 * @author vains
 */
@Data
@Entity
@Table(name = "sys_role_permission")
@EqualsAndHashCode(callSuper = true)
@Comment(value = "RBAC角色权限关联表")
public class SysRolePermission extends BasicAuditorEntity {

    @Id
    @Comment("主键id")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Comment("角色id")
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @NotNull
    @Comment("权限id")
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;

}