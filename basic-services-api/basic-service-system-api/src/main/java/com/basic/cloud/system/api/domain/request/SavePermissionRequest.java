package com.basic.cloud.system.api.domain.request;

import com.basic.framework.data.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    @Schema(title = "权限名")
    private String name;

    /**
     * 权限码
     */
    @NotBlank
    @Schema(title = "权限码")
    private String permission;

    /**
     * 路径
     */
    @NotBlank
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
    @NotNull
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

}
