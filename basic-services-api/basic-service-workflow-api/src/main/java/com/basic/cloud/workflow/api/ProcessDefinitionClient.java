package com.basic.cloud.workflow.api;

import com.basic.cloud.workflow.api.domain.request.FindDefinitionPageRequest;
import com.basic.cloud.workflow.api.domain.request.SuspensionStateChangeRequest;
import com.basic.cloud.workflow.api.domain.response.ProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.PageProcessDefinitionResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * 已部署的流程定义相关接口
 *
 * @author vains
 */
@RequestMapping("/process-definition")
@Tag(name = "已部署的流程定义相关接口", description = "已部署的流程定义相关接口")
@FeignClient(name = FeignConstants.WORKFLOW_APPLICATION, path = FeignConstants.WORKFLOW_CONTEXT_PATH, contextId = "ProcessDefinitionClient")
public interface ProcessDefinitionClient {

    @GetMapping("/page")
    @Operation(summary = "分页查询部署后的流程定义数据", description = "分页查询部署后的流程定义数据")
    Result<PageResult<PageProcessDefinitionResponse>> pageQuery(@Valid @SpringQueryMap FindDefinitionPageRequest request);

    @PutMapping("/change-suspension-state/{processDefinitionId}")
    @Operation(summary = "改变流程定义状态", description = "改变流程定义状态")
    Result<String> changeSuspensionState(@PathVariable String processDefinitionId,
                                         @Valid @RequestBody SuspensionStateChangeRequest request);

    @GetMapping("/{processDefinitionId}")
    @Operation(summary = "查询流程定义详情", description = "查询流程定义详情")
    Result<ProcessDefinitionResponse> getDeployDefinitionDetail(@PathVariable String processDefinitionId);

    @GetMapping("/{processDefinitionId}/bpmn")
    @Operation(summary = "根据部署后的流程定义ID获取BPMN XML", description = "根据部署后的流程定义ID获取BPMN XML")
    Result<String> getBpmnXml(@PathVariable String processDefinitionId);

}
