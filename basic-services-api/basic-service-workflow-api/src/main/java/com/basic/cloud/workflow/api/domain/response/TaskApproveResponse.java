package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 流程审批响应
 *
 * @author vains
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "TaskApproveResponse", description = "流程审批响应")
public class TaskApproveResponse implements Serializable {

    /**
     * 是否流程结束
     */
    private boolean processEnded;

    /**
     * 下一节点 taskId
     */
    private String nextTaskId;

    /**
     * 下一节点 formKey
     */
    private String nextFormKey;

    /**
     * 下一节点 name
     */
    private String nextTaskName;
}
