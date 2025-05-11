package com.basic.cloud.system.api.domain.request;

import com.basic.cloud.system.api.enums.PermissionTypeEnum;
import com.basic.framework.data.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.io.Serializable;

/**
 * 保存或修改权限信息入参
 *
 * @author vains
 */
@Data
@Schema(name = "保存或修改权限信息入参")
public class SavePermissionRequest implements Serializable {

    /**
     * 主键id
     */
    @Schema(title = "主键id")
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 权限名
     */
    @Schema(title = "权限名")
    @NotBlank(groups = {Update.class, Default.class})
    private String name;

    @NotBlank(groups = {Update.class, Default.class})
    @Schema(title = "菜单名称（兼容国际化、非国际化，如果用国际化的写法就必须在根目录的locales文件夹下对应添加）")
    private String title;

    /**
     * 权限码
     */
    @Schema(title = "权限码")
    private String permission;

    /**
     * 路径
     */
    @Schema(title = "路径")
    @NotBlank(groups = {Update.class, Default.class})
    private String path;

    /**
     * HTTP请求方式
     */
    @Schema(title = "HTTP请求方式")
    private String requestMethod;

    /**
     * 菜单类型
     */
    @Schema(title = "菜单类型")
    @NotNull(groups = {Update.class, Default.class})
    private PermissionTypeEnum permissionType;

    @Schema(title = "所属模块名字")
    private String moduleName;

    /**
     * 描述
     */
    @Schema(title = "描述")
    private String description;

    /**
     * 是否需要鉴权
     */
    @Schema(title = "是否需要鉴权")
    private Boolean needAuthentication;

    @Schema(title = "父节点id")
    private Long parentId;

    @Schema(title = "组件路径")
    private String component;

    @Schema(title = "路由重定向")
    private String redirect;

    @Schema(title = "菜单图标")
    private String icon;

    @Schema(title = "右侧图标")
    private String extraIcon;

    @Schema(title = "进场动画（页面加载动画）")
    private String enterTransition;

    @Schema(title = "离场动画（页面加载动画）")
    private String leaveTransition;

    @Schema(title = "链接地址（需要内嵌的iframe链接地址）")
    private String frameSrc;

    @Schema(title = "加载动画（内嵌的iframe页面是否开启首次加载动画）")
    private String frameLoading;

    @Schema(title = "缓存页面（是否缓存该路由页面，开启后会保存该页面的整体状态，刷新后会清空状态）")
    private Boolean keepAlive;

    @Schema(title = "是否显示该菜单")
    private Boolean showLink;

    @Schema(title = "隐藏标签页（当前菜单名称或自定义信息禁止添加到标签页）")
    private Boolean hiddenTag;

    @Schema(title = "固定标签页（当前菜单名称是否固定显示在标签页且不可关闭）")
    private Boolean fixedTag;

    @Schema(title = "是否显示父级菜单")
    private Boolean showParent;

    @Schema(title = "菜单排序")
    private Integer rank;

}
