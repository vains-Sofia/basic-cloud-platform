package com.basic.cloud.system.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 修改角色权限入参
 *
 * @author vains
 */
@Data
public class UpdateRolePermissionsRequest implements Serializable {

    @NotNull
    @Schema(title = "角色id", description = "角色id")
    private Long roleId;

    @Schema(title = "权限id列表", description = "权限id列表")
    private List<Long> permissionIds;
}
