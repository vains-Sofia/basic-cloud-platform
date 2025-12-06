package com.basic.cloud.workflow.controller;

import com.basic.cloud.workflow.api.DeploymentDefinitionClient;
import com.basic.cloud.workflow.api.domain.request.FindDeployDefinitionPageRequest;
import com.basic.cloud.workflow.api.domain.request.SuspensionStateChangeRequest;
import com.basic.cloud.workflow.api.domain.response.DeployDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.PageDeployDefinitionResponse;
import com.basic.cloud.workflow.service.DeploymentDefinitionService;
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
public class DeploymentDefinitionController implements DeploymentDefinitionClient {

    private final DeploymentDefinitionService deploymentDefinitionService;

    @Override
    public Result<PageResult<PageDeployDefinitionResponse>> pageQuery(FindDeployDefinitionPageRequest request) {
        PageResult<PageDeployDefinitionResponse> pageResult = deploymentDefinitionService.pageQuery(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<String> changeSuspensionState(String processDefinitionId, SuspensionStateChangeRequest request) {
        deploymentDefinitionService.changeSuspensionState(processDefinitionId, request);
        return Result.success();
    }

    @Override
    public Result<DeployDefinitionResponse> getDeployDefinitionDetail(String processDefinitionId) {
        DeployDefinitionResponse details = deploymentDefinitionService.getDeployDefinitionDetail(processDefinitionId);
        return Result.success(details);
    }

    @Override
    public Result<String> getBpmnXml(String processDefinitionId) {
        String bpmnXml = deploymentDefinitionService.getBpmnXml(processDefinitionId);
        return Result.success(bpmnXml);
    }

}
