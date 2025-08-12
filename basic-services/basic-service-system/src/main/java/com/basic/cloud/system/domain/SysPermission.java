package com.basic.cloud.system.domain;

import com.basic.framework.oauth2.core.enums.PermissionTypeEnum;
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
    @Size(max = 100)
    @Comment(value = "路由名称")
    @Column(name = "name", length = 100)
    private String name;

    /**
     * 菜单名称
     */
    @NotNull
    @Size(max = 100)
    @Comment(value = "菜单名称（兼容国际化、非国际化，如果用国际化的写法就必须在根目录的locales文件夹下对应添加）")
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * 权限码
     */
    @Size(max = 50)
    @Comment(value = "权限码")
    @Column(name = "permission", length = 50)
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
     * 权限类型 0:菜单,1:接口,2:其它
     */
    @NotNull
    @Comment(value = "权限类型 0:菜单,1:接口,2:其它")
    @Column(name = "permission_type", nullable = false)
    private PermissionTypeEnum permissionType;

    /**
     * 所属模块名字
     */
    @Comment(value = "所属模块名字")
    @Column(name = "module_name")
    private String moduleName;

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

    /**
     * 父节点id
     */
    @Comment(value = "父节点id")
    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Comment(value = "组件路径")
    @Column(name = "component")
    private String component;

    @Size(max = 100)
    @Comment(value = "路由重定向")
    @Column(name = "redirect", length = 100)
    private String redirect;

    @Size(max = 100)
    @Comment(value = "菜单图标")
    @Column(name = "icon", length = 100)
    private String icon;

    @Size(max = 100)
    @Comment(value = "右侧图标")
    @Column(name = "extra_icon", length = 100)
    private String extraIcon;

    @Size(max = 100)
    @Comment(value = "进场动画（页面加载动画）")
    @Column(name = "enter_transition", length = 100)
    private String enterTransition;

    @Size(max = 100)
    @Comment(value = "离场动画（页面加载动画）")
    @Column(name = "leave_transition", length = 100)
    private String leaveTransition;

    @Size(max = 1000)
    @Comment(value = "链接地址（需要内嵌的iframe链接地址）")
    @Column(name = "frame_src", length = 1000)
    private String frameSrc;

    @Size(max = 100)
    @Comment(value = "加载动画（内嵌的iframe页面是否开启首次加载动画）")
    @Column(name = "frame_loading", length = 100)
    private String frameLoading;

    @Column(name = "keep_alive")
    @Comment(value = "缓存页面（是否缓存该路由页面，开启后会保存该页面的整体状态，刷新后会清空状态）")
    private Boolean keepAlive;

    @Column(name = "show_link")
    @Comment(value = "是否显示该菜单")
    private Boolean showLink;

    @Column(name = "hidden_tag")
    @Comment(value = "隐藏标签页（当前菜单名称或自定义信息禁止添加到标签页）")
    private Boolean hiddenTag;

    @Column(name = "fixed_tag")
    @Comment(value = "固定标签页（当前菜单名称是否固定显示在标签页且不可关闭）")
    private Boolean fixedTag;

    @Column(name = "show_parent")
    @Comment(value = "是否显示父级菜单")
    private Boolean showParent;

    @Column(name = "`rank`")
    @Comment(value = "菜单排序")
    private Integer rank;

    @Column(name = "`active_path`")
    @Comment(value = "指定激活菜单即可获得高亮，`activePath`为指定激活菜单的`path`")
    private String activePath;

}