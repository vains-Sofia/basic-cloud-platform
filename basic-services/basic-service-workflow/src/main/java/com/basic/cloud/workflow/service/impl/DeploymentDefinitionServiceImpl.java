package com.basic.cloud.workflow.service.impl;

import com.basic.cloud.workflow.api.domain.request.FindDeployDefinitionPageRequest;
import com.basic.cloud.workflow.api.domain.request.SuspensionStateChangeRequest;
import com.basic.cloud.workflow.api.domain.response.DeployDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.PageDeployDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.TaskFormResponse;
import com.basic.cloud.workflow.api.enums.SuspensionStateEnum;
import com.basic.cloud.workflow.service.DeploymentDefinitionService;
import com.basic.cloud.workflow.util.PaginationUtils;
import com.basic.cloud.workflow.util.QueryBuilder;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.exception.CloudServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 已部署的流程定义 Service 实现
 *
 * @author vains
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeploymentDefinitionServiceImpl implements DeploymentDefinitionService {

    private final RepositoryService repositoryService;

    @Override
    public PageResult<PageDeployDefinitionResponse> pageQuery(FindDeployDefinitionPageRequest request) {
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        // 组装查询条件
        ProcessDefinitionQuery processDefinitionQuery = QueryBuilder.of(definitionQuery)
                .like(definitionQuery::processDefinitionNameLike, request.getName())
                .like(definitionQuery::processDefinitionKeyLike, request.getProcessKey())
                .like(definitionQuery::processDefinitionCategoryLike, request.getCategory())
                .build();
        processDefinitionQuery.latestVersion();
        if (request.getActive() != null) {
            if (request.getActive()) {
                processDefinitionQuery.active();
            } else {
                processDefinitionQuery.suspended();
            }
        }
        processDefinitionQuery.orderByDeploymentId().desc();

        // 计算适用于框架内部分页的页码和每页行数
        PaginationUtils.PageParam param = PaginationUtils.calc(Math.toIntExact(request.getCurrent()), Math.toIntExact(request.getSize()));
        // 查询
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.listPage(param.firstResult(), param.maxResults());
        if (!ObjectUtils.isEmpty(processDefinitions)) {
            // 提取部署 ID
            List<String> deploymentIds = processDefinitions.stream().map(ProcessDefinition::getDeploymentId).toList();
            List<Deployment> deployments = repositoryService.createDeploymentQuery().deploymentIds(deploymentIds).list();
            // 转为响应bean
            List<PageDeployDefinitionResponse> responseList = processDefinitions
                    .stream()
                    .map(pd -> {
                        PageDeployDefinitionResponse response = new PageDeployDefinitionResponse();
                        response.setId(pd.getId());
                        response.setName(pd.getName());
                        response.setKey(pd.getKey());
                        response.setCategory(pd.getCategory());
                        response.setVersion(pd.getVersion());
                        response.setSuspended(pd.isSuspended());
                        response.setDeploymentId(pd.getDeploymentId());
                        if (!ObjectUtils.isEmpty(deployments)) {
                            // 提取部署时间
                            Deployment deployment = deployments.stream().filter(d -> d.getId().equals(pd.getDeploymentId())).findFirst().orElse(null);
                            response.setDeploymentTime(Optional.ofNullable(deployment).map(Deployment::getDeploymentTime).orElse(null));
                        }
                        return response;
                    }).toList();

            return PageResult.of(request.getCurrent(), request.getSize(), processDefinitionQuery.count(), responseList);
        }

        return PageResult.of(request.getCurrent(), request.getSize(), processDefinitionQuery.count(), List.of());
    }

    @Override
    public void changeSuspensionState(String processDefinitionId, SuspensionStateChangeRequest request) {
        // 默认激活/挂起关联的流程实例
        boolean includeProcessInstances = request.getIncludeProcessInstances() == null || request.getIncludeProcessInstances();
        if (Objects.equals(request.getState(), SuspensionStateEnum.ACTIVE)) {
            if (log.isDebugEnabled()) {
                log.debug("激活流程定义【{}】，是否激活关联的流程实例【{}】.", processDefinitionId, includeProcessInstances);
            }
            // 激活指定的流程定义
            repositoryService.activateProcessDefinitionById(processDefinitionId, includeProcessInstances, (null));
        } else {
            if (log.isDebugEnabled()) {
                log.debug("挂起流程定义【{}】，是否挂起关联的流程实例【{}】.", processDefinitionId, includeProcessInstances);
            }
            // 挂起指定的流程定义
            repositoryService.suspendProcessDefinitionById(processDefinitionId, includeProcessInstances, (null));
        }
    }

    @Override
    public String getBpmnXml(String processDefinitionId) {
        Assert.hasText(processDefinitionId, "流程定义ID不能为空");
        // 查询流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        Assert.notNull(processDefinition, "流程定义不存在");
        String deploymentId = processDefinition.getDeploymentId();
        try (InputStream stream = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName())) {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("获取流程定义【{}】的BPMN XML失败.", processDefinitionId, e);
            throw new CloudServiceException("获取流程定义" + processDefinitionId + "的BPMN XML失败");
        }
    }

    @Override
    public DeployDefinitionResponse getDeployDefinitionDetail(String processDefinitionId) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        Assert.notNull(pd, "ProcessDefinition not found: " + processDefinitionId);

        Deployment deployment = repositoryService.createDeploymentQuery()
                .deploymentId(pd.getDeploymentId())
                .singleResult();

        DeployDefinitionResponse dto = new DeployDefinitionResponse();
        dto.setId(pd.getId());
        dto.setName(pd.getName());
        dto.setKey(pd.getKey());
        dto.setVersion(pd.getVersion());
        dto.setCategory(pd.getCategory());
        dto.setSuspended(pd.isSuspended());
        dto.setDeploymentId(pd.getDeploymentId());
        if (deployment != null) {
            dto.setDeploymentTime(deployment.getDeploymentTime());
        }
        dto.setResourceName(pd.getResourceName());
        dto.setDiagramResourceName(pd.getDiagramResourceName());

        // 启动权限 & 表单绑定：Flowable 原生并不强制存放启动权限，通常业务系统在扩展表或 model/meta 存储
        // 如果你把 startFormKey 存在 formRepository 或 processDefinition.extensionElements，可以尝试下面读取：

        // task forms: 遍历 bpmn model 的 userTask extensionElements 查找 formKey
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pd.getId());
        List<TaskFormResponse> taskForms = getTaskFormResponses(bpmnModel);
        dto.setTaskForms(taskForms);

        // 尝试获取启动表单key
        Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
        if (!ObjectUtils.isEmpty(flowElements)) {
            // 尝试获取开始节点
            flowElements.stream()
                    .filter(fe -> fe instanceof StartEvent)
                    .findFirst()
                    .map(StartEvent.class::cast)
                    .ifPresent(startEvent -> dto.setStartFormKey(startEvent.getFormKey()));
        }

        // 启动用户/组：如果使用 identityLink 去管理启动权限，需要在自定义表或使用 processDefinition identity links（Flowable 支持）
        // Flowable没有直接的API列出process-definition级别的 identity links，常见做法：在部署时把权限写入自定义表或 model meta
        // 这里返回空，或在你的系统中额外查询。
        dto.setStartUsers(Collections.emptyList());
        dto.setStartGroups(Collections.emptyList());

        return dto;
    }

    /**
     * 获取任务表单
     *
     * @param bpmnModel BPMN模型
     * @return 任务表单列表
     */
    private List<TaskFormResponse> getTaskFormResponses(BpmnModel bpmnModel) {
        List<TaskFormResponse> taskForms = new ArrayList<>();
        if (bpmnModel != null) {
            for (FlowElement fe : bpmnModel.getMainProcess().getFlowElements()) {
                if (fe instanceof UserTask ut) {
                    TaskFormResponse t = new TaskFormResponse();
                    t.setTaskDefinitionKey(ut.getId());
                    t.setFormKey(ut.getFormKey());
                    taskForms.add(t);
                }
            }
        }
        return taskForms;
    }
}
