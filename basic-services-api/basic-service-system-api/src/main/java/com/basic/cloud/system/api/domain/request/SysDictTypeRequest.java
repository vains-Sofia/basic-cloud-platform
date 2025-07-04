package com.basic.cloud.system.api.domain.request;

import com.basic.cloud.system.api.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典类型请求参数
 *
 * @author vains
 */
@Data
@Schema(title = "字典类型请求参数", description = "字典类型请求参数")
public class SysDictTypeRequest {

    @Schema(title = "字典类型编码", description = "字典类型编码")
    @NotBlank(message = "字典类型编码不能为空")
    private String typeCode;

    @Schema(title = "字典名称", description = "字典名称")
    @NotBlank(message = "字典名称不能为空")
    private String name;

    @Schema(title = "字典类型描述", description = "字典类型描述")
    private String description;

    @Schema(title = "状态", description = "状态（Y=启用，N=禁用）")
    private StatusEnum status = StatusEnum.ENABLE;
}