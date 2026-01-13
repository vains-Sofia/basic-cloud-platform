package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 已完成任务响应
 *
 * @author vains
 */
@Data
@Schema(title = "FinishedTaskResponse", description = "已完成任务响应")
public class FinishedTaskResponse implements Serializable {

    @Schema(title = "任务 ID", description = "任务 ID")
    private String taskId;

    @Schema(title = "任务名称", description = "任务名称")
    private String taskName;

    @Schema(title = "审批人、提交人", description = "审批人、提交人")
    private String assignee;

    @Schema(title = "表单 schema", description = "表单 schema")
    private String formContent;

    @Schema(title = "表单数据", description = "表单数据")
    private Map<String, Object> formData;

    @Schema(title = "提交时间", description = "提交时间")
    private Date endTime;

}
