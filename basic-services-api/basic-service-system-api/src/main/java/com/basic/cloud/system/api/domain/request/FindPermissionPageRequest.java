package com.basic.cloud.system.api.domain.request;

import com.basic.framework.oauth2.core.enums.PermissionTypeEnum;
import com.basic.framework.core.domain.DataPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询权限信息入参
 *
 * @author vains
 */
@Data
@Schema(name = "分页查询权限信息入参")
@EqualsAndHashCode(callSuper = true)
public class FindPermissionPageRequest extends DataPageable {

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
