package com.basic.cloud.workflow.api;

import com.basic.cloud.workflow.api.domain.request.FindTodoTaskPageRequest;
import com.basic.cloud.workflow.api.domain.request.TaskApproveRequest;
import com.basic.cloud.workflow.api.domain.response.TaskApproveResponse;
import com.basic.cloud.workflow.api.domain.response.TodoTaskPageResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
