package com.basic.framework.oauth2.storage.core.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 重置scope权限入参
 *
 * @author vains
 */
@Data
public class ResetScopePermissionRequest implements Serializable {

    /**
     * scope名称
     */
    @NotBlank
    @Schema(title = "scope名称")
    private String scope;

    /**
     * 权限id(为空代表移除scope的所有权限)
     */
    @Schema(title = "权限id", description = "为空代表移除scope的所有权限")
    private List<Long> permissionsId;

}
