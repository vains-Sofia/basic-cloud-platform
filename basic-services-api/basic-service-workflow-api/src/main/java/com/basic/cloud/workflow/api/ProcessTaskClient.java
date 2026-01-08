package com.basic.cloud.workflow.api;

import com.basic.cloud.workflow.api.domain.request.FindTodoTaskPageRequest;
import com.basic.cloud.workflow.api.domain.request.TaskApproveRequest;
import com.basic.cloud.workflow.api.domain.response.TaskApproveResponse;
import com.basic.cloud.workflow.api.domain.response.TodoTaskPageResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.redis.annotation.RedisLock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * 流程任务相关接口Client
 *
 * @author vains
 */
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
    Result<String> claim(@PathVariable String taskId);

    @RedisLock
    @PutMapping("/unclaim/{taskId}")
    @Operation(summary = "归还任务(Unclaim)", description = "归还任务、取消认领、领取任务")
    Result<String> unclaim(@PathVariable String taskId);

}
