package com.basic.cloud.workflow.service;

import com.basic.cloud.workflow.api.domain.request.FindDefinitionPageRequest;
import com.basic.cloud.workflow.api.domain.request.StartProcessRequest;
import com.basic.cloud.workflow.api.domain.request.SuspensionStateChangeRequest;
import com.basic.cloud.workflow.api.domain.response.ProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.PageProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.StartProcessResponse;
import com.basic.framework.core.domain.PageResult;

/**
 * 已部署的流程定义 Service 接口
 *
 * @author vains
 */
public interface ProcessDefinitionService {

    /**
     * 分页查询部署后的流程定义数据
     *
     * @param request 分页查询入参
     * @return 部署后的流程定义数据
     */
    PageResult<PageProcessDefinitionResponse> pageQuery(FindDefinitionPageRequest request);

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
    ProcessDefinitionResponse getProcessDefinitionDetail(String processDefinitionId);

    /**
     * 启动流程实例
     *
     * @param request 启动流程参数
     * @return 开始节点与下一个节点相关数据
     */
    StartProcessResponse startProcess(StartProcessRequest request);

}
