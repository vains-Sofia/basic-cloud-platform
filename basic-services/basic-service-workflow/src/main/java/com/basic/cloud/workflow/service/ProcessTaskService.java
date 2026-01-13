package com.basic.cloud.workflow.service;

import com.basic.cloud.workflow.api.domain.request.CancelProcessInstanceRequest;
import com.basic.cloud.workflow.api.domain.request.FindProcessInstanceRequest;
import com.basic.cloud.workflow.api.domain.request.FindTodoTaskPageRequest;
import com.basic.cloud.workflow.api.domain.request.TaskApproveRequest;
import com.basic.cloud.workflow.api.domain.response.ProcessInstanceResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessTaskDetailResponse;
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

    /**
     * 拾取任务
     *
     * @param taskId 任务 ID
     */
    void claim(String taskId);

    /**
     * 归还任务
     *
     * @param taskId 任务 ID
     */
    void unclaim(String taskId);

    /**
     * 根据任务 ID 获取流程任务详情
     *
     * @param taskId 任务 ID
     * @return 流程任务详情
     */
    ProcessTaskDetailResponse getProcessTaskDetail(String taskId);

    /**
     * 查询我的流程实例
     *
     * @param request 分页查询入参
     * @return 流程实例
     */
    PageResult<ProcessInstanceResponse> getMyProcessInstance(FindProcessInstanceRequest request);

    /**
     * 取消流程实例
     *
     * @param request 取消流程实例入参
     */
    void cancelProcessInstance(CancelProcessInstanceRequest request);
}
