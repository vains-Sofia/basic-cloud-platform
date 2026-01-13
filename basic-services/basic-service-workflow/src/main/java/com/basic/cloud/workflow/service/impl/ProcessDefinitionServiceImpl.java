package com.basic.cloud.workflow.service.impl;

import com.basic.cloud.workflow.api.domain.request.FindDefinitionPageRequest;
import com.basic.cloud.workflow.api.domain.request.StartProcessRequest;
import com.basic.cloud.workflow.api.domain.request.SuspensionStateChangeRequest;
import com.basic.cloud.workflow.api.domain.response.*;
import com.basic.cloud.workflow.api.enums.SuspensionStateEnum;
import com.basic.cloud.workflow.service.ProcessDefinitionService;
import com.basic.cloud.workflow.util.PaginationUtils;
import com.basic.cloud.workflow.util.QueryBuilder;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
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
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    private final TaskService taskService;

    private final RuntimeService runtimeService;

    private final IdentityService identityService;

    private final RepositoryService repositoryService;

    @Override
    public PageResult<PageProcessDefinitionResponse> pageQuery(FindDefinitionPageRequest request) {
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
            List<PageProcessDefinitionResponse> responseList = processDefinitions
                    .stream()
                    .map(pd -> {
                        PageProcessDefinitionResponse response = new PageProcessDefinitionResponse();
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
    public ProcessDefinitionResponse getProcessDefinitionDetail(String processDefinitionId) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        Assert.notNull(pd, "ProcessDefinition not found: " + processDefinitionId);

        Deployment deployment = repositoryService.createDeploymentQuery()
                .deploymentId(pd.getDeploymentId())
                .singleResult();

        ProcessDefinitionResponse dto = new ProcessDefinitionResponse();
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

    @Override
    public StartProcessResponse startProcess(StartProcessRequest request) {
        String startUserId = SecurityUtils.getUserId() + "";

        // 设置流程发起人（Flowable 标准做法）
        identityService.setAuthenticatedUserId(startUserId);
        Map<String, Object> variables;
        if (ObjectUtils.isEmpty(request.getVariables())) {
            variables = new HashMap<>(1);
        } else {
            variables = request.getVariables();
        }
        // 设置流程发起人
        variables.put(BpmnXMLConstants.ATTRIBUTE_EVENT_START_INITIATOR, startUserId);

        try {
            // 启动流程实例
            ProcessInstance processInstance =
                    runtimeService.startProcessInstanceByKey(
                            request.getProcessDefinitionKey(),
                            request.getBusinessKey(),
                            variables
                    );

            // 查询当前激活的任务
            List<Task> activeTasks = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .active()
                    .list();

            // 决策 nextTask
            Task nextTask = decideNextTask(activeTasks, startUserId);

            // 构建返回对象
            StartProcessResponse response = new StartProcessResponse();
            response.setProcessInstanceId(processInstance.getId());
            response.setProcessDefinitionKey(request.getProcessDefinitionKey());
            response.setBusinessKey(request.getBusinessKey());
            response.setStartUserId(startUserId);

            if (nextTask != null) {
                response.setNextTask(buildNextTaskInfo(nextTask, startUserId));
                response.setRedirect(RedirectInfo.taskForm(nextTask.getId()));
            } else {
                response.setRedirect(decideRedirect(activeTasks));
            }

            return response;

        } finally {
            identityService.setAuthenticatedUserId(null);
        }
    }

    /**
     * 决策“是否存在唯一可操作的下一个任务”
     */
    private Task decideNextTask(List<Task> tasks, String startUserId) {

        if (tasks == null || tasks.isEmpty()) {
            return null;
        }

        // ① assignee 就是发起人（最优先）
        List<Task> assignedToMe = tasks.stream()
                .filter(t -> startUserId.equals(t.getAssignee()))
                .toList();

        if (assignedToMe.size() == 1) {
            return assignedToMe.getFirst();
        }

        // ② 候选人包含发起人
        List<Task> candidateTasks = tasks.stream()
                .filter(t -> taskService.createTaskQuery()
                        .taskId(t.getId())
                        .taskCandidateUser(startUserId)
                        .count() > 0)
                .toList();

        if (candidateTasks.size() == 1) {
            return candidateTasks.getFirst();
        }

        // ③ 只有一个任务（兜底）
        if (tasks.size() == 1) {
            return tasks.getFirst();
        }

        // ④ 多任务（并行 / 不确定）
        return null;
    }

    /**
     * 构建 NextTaskInfo
     */
    private NextTaskInfo buildNextTaskInfo(Task task, String startUserId) {

        NextTaskInfo info = new NextTaskInfo();
        info.setTaskId(task.getId());
        info.setTaskDefinitionKey(task.getTaskDefinitionKey());
        info.setTaskName(task.getName());
        info.setAssignee(task.getAssignee());
        info.setInitiatorTask(startUserId.equals(task.getAssignee()));

        // 读取 BPMN 中的 formKey
        String formKey = task.getFormKey();
        info.setFormKey(formKey);

//        if (formKey != null) {
//            // 查询你自己表单系统中的版本
//            Integer latestVersion = processFormMapper.getLatestVersion(formKey);
//            info.setFormVersion(latestVersion);
//        }

        return info;
    }

    /**
     * 决策前端跳转策略
     */
    private RedirectInfo decideRedirect(List<Task> tasks) {

        if (tasks == null || tasks.isEmpty()) {
            return RedirectInfo.none();
        }

        if (tasks.size() > 1) {
            return RedirectInfo.taskList();
        }

        Task onlyTask = tasks.getFirst();
        if (onlyTask.getFormKey() == null) {
            return RedirectInfo.taskList();
        }

        return RedirectInfo.taskForm(onlyTask.getId());
    }

}
