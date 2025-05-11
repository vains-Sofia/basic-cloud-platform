package com.basic.cloud.system.domain;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

/**
 * RBAC用户角色关联表
 *
 * @author vains
 */
@Data
@Entity
@Table(name = "sys_user_role")
@Comment(value = "RBAC用户角色关联表")
@EqualsAndHashCode(callSuper = true)
public class SysUserRole extends BasicAuditorEntity {

    /**
     * 主键id
     */
    @Id
    @Comment(value = "主键id")
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("角色id")
    @Column(name = "role_id")
    private Long roleId;

    @Comment("用户id")
    @Column(name = "user_id")
    private Long userId;

}