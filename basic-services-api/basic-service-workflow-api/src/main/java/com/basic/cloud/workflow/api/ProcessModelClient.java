package com.basic.cloud.workflow.api;

import com.basic.cloud.workflow.api.domain.request.FindModelHistoryPageRequest;
import com.basic.cloud.workflow.api.domain.request.FindModelPageRequest;
import com.basic.cloud.workflow.api.domain.request.PublishProcessRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessModelRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessModelResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessModelResponse;
import com.basic.cloud.workflow.api.domain.response.PublishProcessResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * 流程模型接口Client
 *
 * @author vains
 */
@RequestMapping("/process-model")
@Tag(name = "流程模型相关接口", description = "流程模型相关接口")
@FeignClient(name = FeignConstants.WORKFLOW_APPLICATION, path = FeignConstants.WORKFLOW_CONTEXT_PATH, contextId = "ProcessModelClient")
public interface ProcessModelClient {

    @PostMapping
    @Operation(summary = "新增流程模型", description = "BPMN 设计器暂存草稿使用")
    Result<String> saveProcessModel(
            @Valid @RequestBody SaveProcessModelRequest processModelRequest);

    @PutMapping("{id}")
    @Operation(summary = "修改流程模型", description = "修改流程模型")
    Result<String> updateProcessModel(
            @PathVariable Long id,
            @Valid @RequestBody SaveProcessModelRequest processModelRequest);

    @DeleteMapping("{processKey}")
    @Operation(summary = "删除流程模型", description = "根据流程定义key删除")
    @Parameter(name = "processKey", description = "流程定义key", required = true)
    Result<String> deleteProcessModel(@PathVariable String processKey);

    @GetMapping("{id}")
    @Parameter(name = "id", description = "流程模型主键id", required = true)
    @Operation(summary = "根据 ID 查询流程模型", description = "根据 ID 查询流程模型")
    Result<ProcessModelResponse> getProcessModel(@PathVariable("id") Long id);

    @GetMapping("/key/{processKey}")
    @Parameter(name = "processKey", description = "流程模型key", required = true)
    @Operation(summary = "根据 ProcessKey 查询流程模型", description = "根据 ProcessKey 查询流程模型")
    Result<ProcessModelResponse> getByProcessKey(@PathVariable("processKey") String processKey);

    @GetMapping("/page")
    @Operation(summary = "分页列表查询", description = "分页列表查询")
    Result<PageResult<PageProcessModelResponse>> getProcessModelPage(@Valid @SpringQueryMap FindModelPageRequest request);

    @PostMapping("/{id}/publish")
    @Parameter(name = "id", description = "流程定义主键id", required = true)
    @Operation(summary = "发布流程模型（部署）", description = "将草稿正式部署到流程引擎(激活 process_key)")
    Result<PublishProcessResponse> publishProcessModel(
            @PathVariable Long id, @RequestBody PublishProcessRequest request);

    @PostMapping("/{id}/disable")
    @Parameter(name = "id", description = "流程模型主键id", required = true)
    @Operation(summary = "禁用流程模型", description = "发布后如果不允许继续启动实例，可以禁用")
    Result<String> disableProcessModel(@PathVariable Long id);

    @PostMapping("/{id}/enable")
    @Operation(summary = "启用流程模型", description = "被禁用的流程重新启用")
    @Parameter(name = "id", description = "流程定义主键id", required = true)
    Result<String> enableProcessModel(@PathVariable Long id);

    @GetMapping("/history/page")
    @Operation(summary = "查询流程模型历史", description = "用于查询同一个 process_key 下的所有版本（草稿 + 发布）")
    Result<PageResult<ProcessModelResponse>> getProcessModelHistory(
            @Valid @SpringQueryMap FindModelHistoryPageRequest request);

    @PutMapping("/rollback/{processKey}/{version}")
    @Parameters({
            @Parameter(name = "version", description = "要回退的版本", required = true),
            @Parameter(name = "processKey", description = "流程定义的key", required = true)
    })
    @Operation(summary = "回退processKey对应的模型至指定版本", description = "回退processKey对应的模型至指定版本")
    Result<String> rollback(@PathVariable String processKey, @PathVariable Integer version);

}
