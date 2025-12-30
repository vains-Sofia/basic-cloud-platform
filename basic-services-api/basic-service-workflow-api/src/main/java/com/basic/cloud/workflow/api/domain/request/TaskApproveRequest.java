package com.basic.cloud.workflow.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 流程审批入参
 *
 * @author vains
 */
@Data
public class TaskApproveRequest implements Serializable {

    @NotBlank(message = "任务ID不能为空")
    @Schema(title = "任务ID", description = "任务ID")
    private String taskId;

    /**
     * APPROVE / REJECT
     */
    @NotBlank(message = "审批类型不能为空")
    @Schema(title = "审批类型", description = "审批类型")
    private String action;

    /**
     * 审批意见
     */
    @Schema(title = "审批意见", description = "审批意见")
    private String comment;

    /**
     * 表单或审批变量
     */
    @Schema(title = "表单或审批变量", description = "表单或审批变量")
    private Map<String, Object> variables;

}
