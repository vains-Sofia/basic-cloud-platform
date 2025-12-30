package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 发起流程实例响应
 *
 * @author vains
 */
@Data
@Schema(title = "StartProcessResponse", description = "发起流程实例响应")
public class StartProcessResponse implements Serializable {

    /**
     * 流程实例id
     */
    @Schema(title = "流程实例id", description = "流程实例id")
    private String processInstanceId;

    /**
     * 流程定义Key
     */
    @Schema(title = "流程定义Key", description = "流程定义Key")
    private String processDefinitionKey;

    /**
     * 业务唯一ID
     */
    @Schema(title = "业务唯一ID", description = "业务唯一ID")
    private String businessKey;

    /**
     * 发起流程的用户ID
     */
    @Schema(title = "发起流程的用户ID", description = "发起流程的用户ID")
    private String startUserId;

    /**
     * 下一个任务节点
     */
    @Schema(title = "下一个任务节点", description = "下一个任务节点")
    private NextTaskInfo nextTask;

    /**
     * 前端重定向相关
     */
    @Schema(title = "前端重定向相关", description = "前端重定向相关")
    private RedirectInfo redirect;

}
