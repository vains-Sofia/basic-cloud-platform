package com.basic.cloud.system.api.domain.request;

import com.basic.cloud.system.api.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典项请求参数
 * <p>
 * 用于创建或更新字典项的请求参数对象。
 * </p>
 * <p>
 * 注意：此类是一个简单的请求对象，通常用于API请求中。
 * </p>
 *
 * @author vains
 */
@Data
public class SysDictItemRequest implements Serializable {

    @NotBlank(message = "类型编码不能为空")
    @Schema(title = "字典类型编码", description = "字典类型的唯一编码")
    private String typeCode;

    @Size(max = 50)
    @NotBlank(message = "项键不能为空")
    @Schema(title = "项键", description = "字典项的唯一键，用于标识该项")
    private String itemKey;

    @Size(max = 100)
    @NotBlank(message = "项值不能为空")
    @Schema(title = "项值", description = "字典项的值，通常是用户可见的描述信息")
    private String itemValue;

    @Schema(title = "排序序号", description = "排序序号", defaultValue = "0")
    private Integer sortOrder = 0;

    @Schema(title = "状态", description = "状态（Y=启用，N=禁用）", defaultValue = "Y")
    private StatusEnum status = StatusEnum.ENABLE;

    @Schema(title = "多语言 JSON 值", description = "用于存储多语言支持的 JSON 字符串")
    private String i18nJson;
}
