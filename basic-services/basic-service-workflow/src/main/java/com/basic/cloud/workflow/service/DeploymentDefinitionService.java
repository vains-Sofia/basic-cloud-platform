package com.basic.cloud.workflow.service;

import com.basic.cloud.workflow.api.domain.request.FindDeployDefinitionPageRequest;
import com.basic.cloud.workflow.api.domain.request.SuspensionStateChangeRequest;
import com.basic.cloud.workflow.api.domain.response.DeployDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.PageDeployDefinitionResponse;
import com.basic.framework.core.domain.PageResult;

/**
 * 已部署的流程定义 Service 接口
 *
 * @author vains
 */
public interface DeploymentDefinitionService {

    /**
     * 分页查询部署后的流程定义数据
     *
     * @param request 分页查询入参
     * @return 部署后的流程定义数据
     */
    PageResult<PageDeployDefinitionResponse> pageQuery(FindDeployDefinitionPageRequest request);

    /**
     * 改变流程定义状态
     *
     * @param processDefinitionId 流程定义 ID
     * @param request             改变流程定义状态入参
     */
    void changeSuspensionState(String processDefinitionId, SuspensionStateChangeRequest request);

    /**
     * 根据部署后的流程定义ID获取BPMN XML
     *
     * @param processDefinitionId 部署后的流程定义ID
     * @return BPMN XML
     */
    String getBpmnXml(String processDefinitionId);

    /**
     * 查询流程定义详情（基础信息 + deploymentTime）
     *
     * @param processDefinitionId 流程定义 ID
     * @return 流程定义详情
     */
    DeployDefinitionResponse getDeployDefinitionDetail(String processDefinitionId);

}
