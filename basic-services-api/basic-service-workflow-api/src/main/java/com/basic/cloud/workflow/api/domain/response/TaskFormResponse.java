package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 任务表单响应
 *
 * @author YuJx
 */
@Data
@Schema(title = "TaskFormResponse", description = "任务表单响应")
public class TaskFormResponse {

    @Schema(title = "任务定义Key", description = "任务定义Key")
    private String taskDefinitionKey;

    @Schema(title = "任务表单Key", description = "任务表单Key")
    private String formKey;

}
