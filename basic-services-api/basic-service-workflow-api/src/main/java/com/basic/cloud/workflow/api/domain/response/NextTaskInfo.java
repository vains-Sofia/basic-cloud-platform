package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 下一个任务节点
 *
 * @author vains
 */
@Data
@Schema(title = "NextTaskInfo", description = "下一个任务节点")
public class NextTaskInfo {

    /**
     * 任务ID
     */
    @Schema(title = "任务ID", description = "任务ID")
    private String taskId;

    /**
     * 任务定义Key
     */
    @Schema(title = "任务定义Key", description = "任务定义Key")
    private String taskDefinitionKey;

    /**
     * 任务名称
     */
    @Schema(title = "任务名称", description = "任务名称")
    private String taskName;

    /**
     * 任务指派人
     */
    @Schema(title = "任务指派人", description = "任务指派人")
    private String assignee;

    /**
     * 表单key
     */
    @Schema(title = "表单key", description = "表单key")
    private String formKey;

    /**
     * 表单版本
     */
    @Schema(title = "表单版本", description = "表单版本")
    private Integer formVersion;

    /**
     * 是否发起人填写节点
     */
    @Schema(title = "是否发起人填写节点", description = "是否发起人填写节点")
    private boolean initiatorTask;
}
