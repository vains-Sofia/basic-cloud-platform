package com.basic.cloud.workflow.controller;

import com.basic.cloud.workflow.api.ProcessDefinitionClient;
import com.basic.cloud.workflow.api.domain.request.FindDefinitionHistoryPageRequest;
import com.basic.cloud.workflow.api.domain.request.FindDefinitionPageRequest;
import com.basic.cloud.workflow.api.domain.request.PublishProcessRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessDefinitionRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.PublishProcessResponse;
import com.basic.cloud.workflow.api.enums.DefinitionStatusEnum;
import com.basic.cloud.workflow.service.ProcessDefinitionService;
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
public class ProcessDefinitionController implements ProcessDefinitionClient {

    private final ProcessDefinitionService processDefinitionService;

    @Override
    public Result<String> saveProcessDefinition(SaveProcessDefinitionRequest processDefinitionRequest) {
        String id = processDefinitionService.saveProcessDefinition(processDefinitionRequest);
        return Result.success(id);
    }

    @Override
    public Result<String> updateProcessDefinition(Long id, SaveProcessDefinitionRequest processDefinitionRequest) {
        processDefinitionService.updateProcessDefinition(id, processDefinitionRequest);
        return Result.success();
    }

    @Override
    public Result<String> deleteProcessDefinition(String processKey) {
        processDefinitionService.deleteProcessDefinition(processKey);
        return Result.success();
    }

    @Override
    public Result<ProcessDefinitionResponse> getProcessDefinition(Long id) {
        ProcessDefinitionResponse processDefinitionResponse = processDefinitionService.getProcessDefinition(id);
        return Result.success(processDefinitionResponse);
    }

    @Override
    public Result<ProcessDefinitionResponse> getByProcessKey(String processKey) {
        ProcessDefinitionResponse processDefinitionResponse = processDefinitionService.getByProcessKey(processKey);
        return Result.success(processDefinitionResponse);
    }

    @Override
    public Result<PageResult<PageProcessDefinitionResponse>> getProcessDefinitionPage(FindDefinitionPageRequest request) {
        PageResult<PageProcessDefinitionResponse> pageResult =
                processDefinitionService.getProcessDefinitionPage(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<PublishProcessResponse> publishProcessDefinition(Long id, PublishProcessRequest request) {
        PublishProcessResponse response = processDefinitionService.publishProcessDefinition(id, request);
        return Result.success(response);
    }

    @Override
    public Result<String> disableProcessDefinition(Long id) {
        processDefinitionService.toggleDefinitionStatus(id, DefinitionStatusEnum.DISABLED);
        return Result.success();
    }

    @Override
    public Result<String> enableProcessDefinition(Long id) {
        processDefinitionService.toggleDefinitionStatus(id, DefinitionStatusEnum.DRAFT);
        return Result.success();
    }

    @Override
    public Result<PageResult<ProcessDefinitionResponse>> getProcessDefinitionHistory(FindDefinitionHistoryPageRequest request) {
        PageResult<ProcessDefinitionResponse> pageResult = processDefinitionService.getProcessDefinitionHistory(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<String> rollback(String processKey, Integer version) {
        processDefinitionService.rollback(processKey, version);
        return null;
    }
}
