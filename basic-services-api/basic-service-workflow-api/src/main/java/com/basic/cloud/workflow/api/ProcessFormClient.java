package com.basic.cloud.workflow.api;

import com.basic.cloud.workflow.api.domain.request.FindProcessFormPageRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessFormRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessFormResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessFormResponse;
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
 * 流程表单设计相关接口
 *
 * @author vains
 */
@RequestMapping("/process-form")
@Tag(name = "流程表单相关接口", description = "流程表单相关接口")
@FeignClient(name = FeignConstants.WORKFLOW_APPLICATION, path = FeignConstants.WORKFLOW_CONTEXT_PATH, contextId = "ProcessFormClient")
public interface ProcessFormClient {

    @PostMapping
    @Operation(summary = "保存流程表单设计接口", description = "保存流程表单设计接口")
    Result<String> saveProcessForm(@Valid @RequestBody SaveProcessFormRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "修改流程表单设计接口", description = "修改流程表单设计接口")
    Result<String> updateProcessForm(@PathVariable Long id,
                                     @Valid @RequestBody SaveProcessFormRequest request);

    @DeleteMapping("/{id}")
    @Operation(summary = "删除流程表单设计接口", description = "删除流程表单设计接口")
    Result<String> deleteProcessForm(@PathVariable String id);

    @GetMapping("/{id}")
    @Operation(summary = "获取流程表单设计详情", description = "获取流程表单设计详情")
    Result<ProcessFormResponse> getProcessFormById(@PathVariable String id);

    @GetMapping("/page")
    @Operation(summary = "分页查询流程表单设计", description = "分页查询流程表单设计")
    Result<PageResult<PageProcessFormResponse>> pageProcessForm(
            @Valid @SpringQueryMap FindProcessFormPageRequest request);

}
