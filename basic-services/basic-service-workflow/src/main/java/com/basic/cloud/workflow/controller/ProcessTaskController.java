package com.basic.cloud.workflow.controller;

import com.basic.cloud.workflow.api.ProcessTaskClient;
import com.basic.cloud.workflow.api.domain.request.FindTodoTaskPageRequest;
import com.basic.cloud.workflow.api.domain.request.StartProcessRequest;
import com.basic.cloud.workflow.api.domain.request.TaskApproveRequest;
import com.basic.cloud.workflow.api.domain.response.StartProcessResponse;
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
    public Result<StartProcessResponse> startProcess(StartProcessRequest request) {
        StartProcessResponse response = processTaskService.startProcess(request);
        return Result.success(response);
    }

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
}
