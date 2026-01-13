package com.basic.cloud.workflow.controller;

import com.basic.cloud.workflow.api.ProcessTaskClient;
import com.basic.cloud.workflow.api.domain.request.CancelProcessInstanceRequest;
import com.basic.cloud.workflow.api.domain.request.FindProcessInstanceRequest;
import com.basic.cloud.workflow.api.domain.request.FindTodoTaskPageRequest;
import com.basic.cloud.workflow.api.domain.request.TaskApproveRequest;
import com.basic.cloud.workflow.api.domain.response.ProcessInstanceResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessTaskDetailResponse;
import com.basic.cloud.workflow.api.domain.response.TaskApproveResponse;
import com.basic.cloud.workflow.api.domain.response.TodoTaskPageResponse;
import com.basic.cloud.workflow.service.ProcessTaskService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程任务相关接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class ProcessTaskController implements ProcessTaskClient {

    private final ProcessTaskService processTaskService;

    @Override
    public Result<PageResult<TodoTaskPageResponse>> todoTaskPage(FindTodoTaskPageRequest request) {
        PageResult<TodoTaskPageResponse> pageResult = processTaskService.todoTaskPage(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<TaskApproveResponse> taskApprove(TaskApproveRequest request) {
        TaskApproveResponse approve = processTaskService.taskApprove(request);
        return Result.success(approve);
    }

    @Override
    public Result<String> claim(String taskId) {
        processTaskService.claim(taskId);
        return Result.success();
    }

    @Override
    public Result<String> unclaim(String taskId) {
        processTaskService.unclaim(taskId);
        return Result.success();
    }

    @Override
    public Result<ProcessTaskDetailResponse> getProcessTaskDetail(String taskId) {
        ProcessTaskDetailResponse detailResponse = processTaskService.getProcessTaskDetail(taskId);
        return Result.success(detailResponse);
    }

    @Override
    public Result<PageResult<ProcessInstanceResponse>> getMyProcessInstance(FindProcessInstanceRequest request) {
        PageResult<ProcessInstanceResponse> pageResult = processTaskService.getMyProcessInstance(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<String> cancelProcessInstance(CancelProcessInstanceRequest request) {
        processTaskService.cancelProcessInstance(request);
        return Result.success();
    }
}
