package com.basic.cloud.system.api.domain.request;

import com.basic.cloud.system.api.enums.PermissionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 查询权限信息入参
 *
 * @author vains
 */
@Data
@Schema(name = "查询权限信息入参")
public class FindPermissionRequest {

    /**
     * 权限名
     */
    @Schema(title = "权限名", description = "权限名")
    private String name;

    /**
     * 权限码
     */
    @Schema(title = "权限码", description = "权限码")
    private String permission;

    /**
     * 路径
     */
    @Schema(title = "路径", description = "路径")
    private String path;

    /**
     * 菜单类型
     */
    @Schema(title = "菜单类型", description = "菜单类型")
    private PermissionTypeEnum permissionType;

}
