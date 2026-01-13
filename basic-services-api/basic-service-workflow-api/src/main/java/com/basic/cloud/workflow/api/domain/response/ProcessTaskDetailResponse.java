package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 流程任务详情响应 bean
 *
 * @author vains
 */
@Data
@Schema(title = "ProcessTaskDetailResponse", description = "流程任务详情响应")
public class ProcessTaskDetailResponse implements Serializable {

    @Schema(title = "任务 ID", description = "任务 ID")
    private String taskId;

    @Schema(title = "任务名称", description = "任务名称")
    private String taskName;

    @Schema(title = "流程实例 ID", description = "流程实例 ID")
    private String processInstanceId;

    @Schema(title = "流程定义名称", description = "流程定义名称")
    private String processDefinitionName;

    @Schema(title = "流程定义 Key", description = "流程定义 Key")
    private String processDefinitionKey;

    @Schema(title = "流程定义版本", description = "流程定义版本")
    private Integer processDefinitionVersion;

    @Schema(title = "要填写的表单内容", description = "要填写的表单内容(如果有)")
    private String formContent;

    @Schema(title = "已完成任务", description = "已完成任务")
    private List<FinishedTaskResponse> finishedTasks;

}
