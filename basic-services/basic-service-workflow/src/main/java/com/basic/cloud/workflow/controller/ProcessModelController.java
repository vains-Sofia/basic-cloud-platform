package com.basic.cloud.workflow.controller;

import com.basic.cloud.workflow.api.ProcessModelClient;
import com.basic.cloud.workflow.api.domain.request.FindModelHistoryPageRequest;
import com.basic.cloud.workflow.api.domain.request.FindModelPageRequest;
import com.basic.cloud.workflow.api.domain.request.PublishProcessRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessModelRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessModelResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessModelResponse;
import com.basic.cloud.workflow.api.domain.response.PublishProcessResponse;
import com.basic.cloud.workflow.api.enums.DefinitionStatusEnum;
import com.basic.cloud.workflow.service.ProcessModelService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程定义接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class ProcessModelController implements ProcessModelClient {

    private final ProcessModelService processModelService;

    @Override
    public Result<String> saveProcessModel(SaveProcessModelRequest processModelRequest) {
        String id = processModelService.saveProcessModel(processModelRequest);
        return Result.success(id);
    }

    @Override
    public Result<String> updateProcessModel(Long id, SaveProcessModelRequest processModelRequest) {
        processModelService.updateProcessModel(id, processModelRequest);
        return Result.success();
    }

    @Override
    public Result<String> deleteProcessModel(String processKey) {
        processModelService.deleteProcessModel(processKey);
        return Result.success();
    }

    @Override
    public Result<ProcessModelResponse> getProcessModel(Long id) {
        ProcessModelResponse processModelResponse = processModelService.getProcessModel(id);
        return Result.success(processModelResponse);
    }

    @Override
    public Result<ProcessModelResponse> getByProcessKey(String processKey) {
        ProcessModelResponse processModelResponse = processModelService.getByProcessKey(processKey);
        return Result.success(processModelResponse);
    }

    @Override
    public Result<PageResult<PageProcessModelResponse>> getProcessModelPage(FindModelPageRequest request) {
        PageResult<PageProcessModelResponse> pageResult =
                processModelService.getProcessModelPage(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<PublishProcessResponse> publishProcessModel(Long id, PublishProcessRequest request) {
        PublishProcessResponse response = processModelService.publishProcessModel(id, request);
        return Result.success(response);
    }

    @Override
    public Result<String> disableProcessModel(Long id) {
        processModelService.toggleModelStatus(id, DefinitionStatusEnum.DISABLED);
        return Result.success();
    }

    @Override
    public Result<String> enableProcessModel(Long id) {
        processModelService.toggleModelStatus(id, DefinitionStatusEnum.DRAFT);
        return Result.success();
    }

    @Override
    public Result<PageResult<ProcessModelResponse>> getProcessModelHistory(FindModelHistoryPageRequest request) {
        PageResult<ProcessModelResponse> pageResult = processModelService.getProcessModelHistory(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<String> rollback(String processKey, Integer version) {
        processModelService.rollback(processKey, version);
        return Result.success();
    }
}
