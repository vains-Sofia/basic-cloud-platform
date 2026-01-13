package com.basic.cloud.workflow.api;

import com.basic.cloud.workflow.api.domain.request.CancelProcessInstanceRequest;
import com.basic.cloud.workflow.api.domain.request.FindProcessInstanceRequest;
import com.basic.cloud.workflow.api.domain.request.FindTodoTaskPageRequest;
import com.basic.cloud.workflow.api.domain.request.TaskApproveRequest;
import com.basic.cloud.workflow.api.domain.response.ProcessInstanceResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessTaskDetailResponse;
import com.basic.cloud.workflow.api.domain.response.TaskApproveResponse;
import com.basic.cloud.workflow.api.domain.response.TodoTaskPageResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.redis.annotation.RedisLock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 流程任务相关接口Client
 *
 * @author vains
 */
@Validated
@RequestMapping("/process-task")
@Tag(name = "流程任务相关接口", description = "流程任务相关接口")
@FeignClient(name = FeignConstants.WORKFLOW_APPLICATION, path = FeignConstants.WORKFLOW_CONTEXT_PATH, contextId = "StartProcessClient")
public interface ProcessTaskClient {

    @GetMapping("/todo/page")
    @Operation(summary = "查询我的待办任务列表", description = "查询我的待办任务列表")
    Result<PageResult<TodoTaskPageResponse>> todoTaskPage(@Valid @SpringQueryMap FindTodoTaskPageRequest request);

    @PostMapping("/approve")
    @Operation(summary = "任务审批", description = "任务审批")
    Result<TaskApproveResponse> taskApprove(@Valid @RequestBody TaskApproveRequest request);

    @PutMapping("/claim/{taskId}")
    @Operation(summary = "拾取任务(Claim)", description = "拾取、认领、领取任务")
    Result<String> claim(@NotBlank @PathVariable String taskId);

    @RedisLock
    @PutMapping("/unclaim/{taskId}")
    @Operation(summary = "归还任务(Unclaim)", description = "归还任务、取消认领、领取任务")
    Result<String> unclaim(@NotBlank @PathVariable String taskId);

    @GetMapping("/process/task/detail/{taskId}")
    @Operation(summary = "获取流程任务详情", description = "获取流程任务详情")
    Result<ProcessTaskDetailResponse> getProcessTaskDetail(@NotBlank @PathVariable String taskId);

    @GetMapping("/my-instances")
    @Operation(summary = "获取我的流程实例", description = "获取我的流程实例")
    Result<PageResult<ProcessInstanceResponse>> getMyProcessInstance(@Valid @SpringQueryMap FindProcessInstanceRequest request);

    @DeleteMapping("/cancel-process")
    @Operation(summary = "取消流程实例", description = "取消流程实例")
    Result<String> cancelProcessInstance(@Valid @RequestBody CancelProcessInstanceRequest request);

}
