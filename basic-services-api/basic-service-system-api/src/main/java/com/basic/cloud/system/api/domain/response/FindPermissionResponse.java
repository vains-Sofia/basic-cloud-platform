package com.basic.cloud.system.api.domain.response;

import com.basic.framework.oauth2.core.enums.PermissionTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询权限响应
 *
 * @author vains
 */
@Data
@Schema(name = "权限信息响应")
public class FindPermissionResponse implements Serializable {

    @Schema(title = "主键id")
    private Long id;

    @Schema(title = "路由名称")
    private String name;

    @Schema(title = "菜单名称（兼容国际化、非国际化，如果用国际化的写法就必须在根目录的locales文件夹下对应添加）")
    private String title;

    @Schema(title = "权限码")
    private String permission;

    @Schema(title = "路径")
    private String path;

    @Schema(title = "HTTP请求方式")
    private String requestMethod;

    @Schema(title = "权限类型")
    private PermissionTypeEnum permissionType;

    @Schema(title = "所属模块名字")
    private String moduleName;

    @Schema(title = "描述")
    private String description;

    @Schema(title = "是否需要鉴权")
    private Boolean needAuthentication;

    @Schema(title = "父节点id")
    @JsonSerialize(using = ToStringSerializer.class)
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

    @Schema(title = "创建人名称")
    private String createName;

    @Schema(title = "修改人名称")
    private String updateName;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "修改时间")
    private LocalDateTime updateTime;

}
