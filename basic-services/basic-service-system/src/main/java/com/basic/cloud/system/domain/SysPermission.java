package com.basic.cloud.system.domain;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * RBAC权限表
 *
 * @author vains
 */
@Data
@Entity
@Comment(value = "RBAC权限表")
@Table(name = "sys_permission")
@SQLRestriction("deleted = false")
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE sys_permission p SET p.deleted = true WHERE id = ?")
public class SysPermission extends BasicAuditorEntity {

    /**
     * 主键id
     */
    @Id
    @Comment(value = "主键id")
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 权限名
     */
    @NotNull
    @Size(max = 50)
    @Comment(value = "权限名")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 权限码
     */
    @NotNull
    @Size(max = 50)
    @Comment(value = "权限码")
    @Column(name = "permission", nullable = false, length = 50)
    private String permission;

    /**
     * 路径
     */
    @Size(max = 100)
    @Comment(value = "路径")
    @Column(name = "path", length = 100)
    private String path;

    /**
     * HTTP请求方式
     */
    @Size(max = 10)
    @Comment(value = "HTTP请求方式")
    @Column(name = "request_method", length = 10)
    private String requestMethod;

    /**
     * 0:菜单,1:接口,2:其它
     */
    @NotNull
    @Comment(value = "0:菜单,1:接口,2:其它")
    @Column(name = "permission_type", nullable = false)
    private Integer permissionType;

    /**
     * 描述
     */
    @Size(max = 255)
    @Comment(value = "描述")
    @Column(name = "description")
    private String description;

    /**
     * 是否需要鉴权
     */
    @Comment(value = "是否需要鉴权")
    @Column(name = "need_authentication")
    private Boolean needAuthentication;

    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    @Comment(value = "是否已删除")
    private Boolean deleted;

}