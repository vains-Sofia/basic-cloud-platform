package com.basic.cloud.system.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 修改用户角色入参
 *
 * @author vains
 */
@Data
public class UpdateUserRolesRequest implements Serializable {

    @NotNull
    @Schema(title = "用户id", description = "用户id")
    private Long userId;

    @Schema(title = "角色id列表", description = "角色id列表")
    private List<Long> roleIds;

}
