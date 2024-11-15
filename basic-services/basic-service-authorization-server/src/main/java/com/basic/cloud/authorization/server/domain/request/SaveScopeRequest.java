package com.basic.cloud.authorization.server.domain.request;

import com.basic.framework.data.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 保存scope入参
 *
 * @author vains
 */
@Data
@Schema(name = "保存/修改scope入参")
public class SaveScopeRequest implements Serializable {

    @NotNull(groups = {Update.class})
    @Schema(title = "主键id", description = "主键id")
    private Long id;

    @NotBlank
    @Schema(title = "scope 名称", description = "scope 名称")
    private String scope;

    @Schema(title = "scope 描述", description = "scope 描述")
    private String description;

    @NotNull(groups = {Update.class})
    @Schema(title = "是否启用", description = "仅修改时使用")
    private Boolean enabled;

}
