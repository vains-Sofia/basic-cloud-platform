package com.basic.cloud.workflow.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 保存流程定义入参
 *
 * @author vains
 */
@Data
@Schema(title = "SaveProcessDefinitionRequest", description = "保存流程定义入参")
public class SaveProcessDefinitionRequest implements Serializable {

    /**
     * 流程定义key
     */
    @NotBlank(message = "流程定义key不能为空")
    @Schema(title = "流程定义key", description = "流程定义key")
    private String processKey;

    /**
     * 流程定义名称
     */
    @NotBlank(message = "流程定义名称不能为空")
    @Schema(title = "流程定义名称", description = "流程定义名称")
    private String processName;

    /**
     * BPMN XML 内容
     */
    @Schema(title = "BPMN XML 内容", description = "BPMN XML 内容")
    private String processXml;

    /**
     * 如果前端使用 bpmn-js JSON，可额外存储
     */
    @Schema(title = "BPMN JSON 内容", description = "预留字段，暂时没有使用")
    private String processJson;

    /**
     * 分类（请假、采购等）
     */
    @Schema(title = "分类", description = "请假、采购等")
    private String category;

    /**
     * 说明
     */
    @Schema(title = "流程说明", description = "流程说明")
    private String remark;

}
