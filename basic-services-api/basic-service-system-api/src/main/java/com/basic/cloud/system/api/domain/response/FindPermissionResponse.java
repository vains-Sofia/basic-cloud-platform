package com.basic.cloud.system.api.domain.response;

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

    /**
     * 主键id
     */
    @Schema(title = "主键id")
    private Long id;

    /**
     * 权限名
     */
    @Schema(title = "权限名")
    private String name;

    /**
     * 权限码
     */
    @Schema(title = "权限码")
    private String permission;

    /**
     * 路径
     */
    @Schema(title = "路径")
    private String path;

    /**
     * HTTP请求方式
     */
    @Schema(title = "HTTP请求方式")
    private String requestMethod;

    /**
     * 0:菜单,1:接口,2:其它
     */
    @Schema(title = "0:菜单,1:接口,2:其它")
    private Integer permissionType;

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

    @Schema(title = "创建人名称")
    private String createName;

    @Schema(title = "修改人名称")
    private String updateName;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "修改时间")
    private LocalDateTime updateTime;
    
}
