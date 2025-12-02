package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询流程定义响应
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessDefinitionResponse extends PageProcessDefinitionResponse implements Serializable {

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

}
