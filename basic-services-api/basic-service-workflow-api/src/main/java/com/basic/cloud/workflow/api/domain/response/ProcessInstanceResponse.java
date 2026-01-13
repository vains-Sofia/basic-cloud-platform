package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程实例响应
 *
 * @author vains
 */
@Data
@Schema(title = "ProcessInstanceResponse", description = "流程实例响应")
public class ProcessInstanceResponse implements Serializable {

    @Schema(title = "流程实例 ID", description = "流程实例 ID")
    private String processInstanceId;

    @Schema(title = "流程定义 ID", description = "流程定义 ID")
    private String processDefinitionId;

    @Schema(title = "流程定义 Key", description = "流程定义 Key")
    private String processDefinitionKey;

    @Schema(title = "流程定义名称", description = "流程定义名称")
    private String processDefinitionName;

    @Schema(title = "流程定义版本", description = "流程定义版本")
    private Integer processDefinitionVersion;

    @Schema(title = "业务 Key", description = "业务 Key")
    private String businessKey;

    @Schema(title = "流程状态", description = "流程状态：RUNNING, SUSPENDED, COMPLETED, CANCELLED")
    private String status;

    @Schema(title = "流程是否挂起", description = "流程是否挂起")
    private Boolean suspended;

    @Schema(title = "流程是否结束", description = "流程是否结束")
    private Boolean ended;

    @Schema(title = "流程实例开始时间", description = "流程实例开始时间")
    private Date startTime;

    @Schema(title = "流程实例结束时间", description = "流程实例结束时间")
    private Date endTime;

    @Schema(title = "格式化的间隔时间", description = "易读间隔时间，例如1天3小时")
    private String formattedDuration;

    @Schema(title = "流程实例从开始到结束间隔的毫秒数", description = "流程实例从开始到结束间隔的毫秒数")
    private Long durationInMillis;

    @Schema(title = "办理人 ID", description = "办理人 ID")
    private String assigneeId;

    @Schema(title = "办理人姓名", description = "办理人姓名")
    private String assigneeName;

    @Schema(title = "当前活动节点 ID", description = "当前活动节点 ID（仅运行中流程）")
    private String currentActivityId;

    @Schema(title = "当前活动节点名称", description = "当前活动节点名称（仅运行中流程）")
    private String currentActivityName;

}
