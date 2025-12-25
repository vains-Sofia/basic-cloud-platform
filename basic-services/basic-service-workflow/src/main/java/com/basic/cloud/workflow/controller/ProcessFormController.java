package com.basic.cloud.workflow.controller;

import com.basic.cloud.workflow.api.ProcessFormClient;
import com.basic.cloud.workflow.api.domain.request.FindProcessFormPageRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessFormRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessFormResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessFormResponse;
import com.basic.cloud.workflow.service.ProcessFormService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程表单设计相关接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class ProcessFormController implements ProcessFormClient {

    private final ProcessFormService processFormService;

    @Override
    public Result<String> saveProcessForm(SaveProcessFormRequest request) {
        String id = processFormService.saveProcessForm(request);
        return Result.success(id);
    }

    @Override
    public Result<String> updateProcessForm(Long id, SaveProcessFormRequest request) {
        String formId = processFormService.updateProcessForm(id, request);
        return Result.success(formId);
    }

    @Override
    public Result<String> deleteProcessForm(String id) {
        processFormService.deleteProcessForm(id);
        return Result.success();
    }

    @Override
    public Result<ProcessFormResponse> getProcessFormById(String id) {
        ProcessFormResponse formResponse = processFormService.getProcessFormById(id);
        return Result.success(formResponse);
    }

    @Override
    public Result<PageResult<PageProcessFormResponse>> pageProcessForm(FindProcessFormPageRequest request) {
        PageResult<PageProcessFormResponse> pageResult = processFormService.pageProcessForm(request);
        return Result.success(pageResult);
    }
}
