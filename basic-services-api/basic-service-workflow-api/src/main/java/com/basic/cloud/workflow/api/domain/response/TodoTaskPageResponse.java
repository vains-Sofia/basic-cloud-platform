package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 待办任务列表响应
 *
 * @author vains
 */
@Data
@Schema(title = "TodoTaskPageResponse", description = "待办任务列表响应")
public class TodoTaskPageResponse implements Serializable {

    // ---- 任务维度 ----

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务定义Key
     */
    private String taskDefinitionKey;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 指派人
     */
    private String assignee;

    /**
     * 创建时间
     */
    private Date createTime;

    // ---- 流程维度 ----

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 流程定义Key
     */
    private String processDefinitionKey;

    /**
     * 流程定义名称
     */
    private String processDefinitionName;

    /**
     * 流程定义版本
     */
    private Integer processDefinitionVersion;

    /**
     * 业务唯一ID
     */
    private String businessKey;

    /**
     * 启动用户ID
     */
    private String startUserId;

    /**
     * 启动用户名称
     */
    private String startUserName;

    // ---- 表单 ----

    /**
     * 表单主键ID
     */
    private String formKey;

    /**
     * 表单版本
     */
    private Integer formVersion;

    // ---- 前端辅助 ----

    /**
     * 是否可以拾取
     */
    private Boolean canClaim;

    /**
     * 是否可以归还
     */
    private Boolean canUnclaim;

    /**
     * 是否为当前用户任务
     */
    private Boolean initiatorTask;

}
