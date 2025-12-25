package com.basic.cloud.workflow.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 保存表单设计入参
 *
 * @author vains
 */
@Data
@Schema(title = "SaveProcessFormRequest", description = "保存表单设计入参")
public class SaveProcessFormRequest implements Serializable {

    /**
     * 表单标题
     */
    @NotBlank(message = "表单标题不能为空")
    @Schema(title = "表单标题", description = "表单标题")
    private String title;

    /**
     * 表单描述
     */
    @Schema(title = "表单描述", description = "表单描述")
    private String description;

    /**
     * 表单设计器生成、使用的完整JSON配置
     */
    @NotBlank(message = "表单内容不能为空")
    @Schema(title = "表单内容", description = "表单设计器生成、使用的完整JSON配置")
    private String formContent;

}
