package com.basic.cloud.workflow.controller;

import com.basic.cloud.workflow.api.ProcessDefinitionClient;
import com.basic.cloud.workflow.api.domain.request.FindDefinitionPageRequest;
import com.basic.cloud.workflow.api.domain.request.StartProcessRequest;
import com.basic.cloud.workflow.api.domain.request.SuspensionStateChangeRequest;
import com.basic.cloud.workflow.api.domain.response.ProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.PageProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.StartProcessResponse;
import com.basic.cloud.workflow.service.ProcessDefinitionService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 已部署的流程定义相关接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class ProcessDefinitionController implements ProcessDefinitionClient {

    private final ProcessDefinitionService processDefinitionService;

    @Override
    public Result<PageResult<PageProcessDefinitionResponse>> pageQuery(FindDefinitionPageRequest request) {
        PageResult<PageProcessDefinitionResponse> pageResult = processDefinitionService.pageQuery(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<String> changeSuspensionState(String processDefinitionId, SuspensionStateChangeRequest request) {
        processDefinitionService.changeSuspensionState(processDefinitionId, request);
        return Result.success();
    }

    @Override
    public Result<ProcessDefinitionResponse> getDeployDefinitionDetail(String processDefinitionId) {
        ProcessDefinitionResponse details = processDefinitionService.getProcessDefinitionDetail(processDefinitionId);
        return Result.success(details);
    }

    @Override
    public Result<String> getBpmnXml(String processDefinitionId) {
        String bpmnXml = processDefinitionService.getBpmnXml(processDefinitionId);
        return Result.success(bpmnXml);
    }

    @Override
    public Result<StartProcessResponse> startProcess(StartProcessRequest request) {
        StartProcessResponse response = processDefinitionService.startProcess(request);
        return Result.success(response);
    }

}
