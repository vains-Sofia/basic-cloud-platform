package com.basic.cloud.workflow.service;

import com.basic.cloud.workflow.api.domain.request.FindTodoTaskPageRequest;
import com.basic.cloud.workflow.api.domain.request.StartProcessRequest;
import com.basic.cloud.workflow.api.domain.request.TaskApproveRequest;
import com.basic.cloud.workflow.api.domain.response.StartProcessResponse;
import com.basic.cloud.workflow.api.domain.response.TaskApproveResponse;
import com.basic.cloud.workflow.api.domain.response.TodoTaskPageResponse;
import com.basic.framework.core.domain.PageResult;

/**
 * 流程任务相关 Service 接口
 *
 * @author vains
 */
public interface ProcessTaskService {

    /**
     * 启动流程实例
     *
     * @param request 启动流程参数
     * @return 开始节点与下一个节点相关数据
     */
    StartProcessResponse startProcess(StartProcessRequest request);

    /**
     * 分页查询待办任务列表
     *
     * @param request 分页参数
     * @return 待办任务列表
     */
    PageResult<TodoTaskPageResponse> todoTaskPage(FindTodoTaskPageRequest request);

    /**
     * 任务审批
     *
     * @param request 任务审批入参
     * @return 审批响应
     */
    TaskApproveResponse taskApprove(TaskApproveRequest request);
}
