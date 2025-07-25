package com.basic.framework.oauth2.storage.domain.request;

import com.basic.framework.data.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.io.Serializable;

/**
 * 保存scope入参
 *
 * @author vains
 */
@Data
@Schema(name = "插入或修改scope入参")
public class SaveScopeRequest implements Serializable {

    @NotNull(groups = {Update.class})
    @Schema(title = "主键id", description = "主键id")
    private Long id;

    @NotBlank(groups = {Update.class, Default.class})
    @Schema(title = "scope 名称", description = "scope 名称")
    private String name;

    @NotBlank(groups = {Update.class, Default.class})
    @Schema(title = "scope 编码", description = "scope 编码")
    private String scope;

    @Schema(title = "scope 描述", description = "scope 描述")
    private String description;

    @NotNull(groups = {Update.class})
    @Schema(title = "是否启用", description = "仅修改时使用")
    private Boolean enabled;

}
