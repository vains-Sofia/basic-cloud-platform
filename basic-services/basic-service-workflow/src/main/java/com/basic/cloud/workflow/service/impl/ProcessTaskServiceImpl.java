package com.basic.cloud.workflow.service.impl;

import com.basic.cloud.system.api.SysBasicUserClient;
import com.basic.cloud.system.api.SysRoleClient;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.cloud.workflow.api.domain.request.FindTodoTaskPageRequest;
import com.basic.cloud.workflow.api.domain.request.TaskApproveRequest;
import com.basic.cloud.workflow.api.domain.response.TaskApproveResponse;
import com.basic.cloud.workflow.api.domain.response.TodoTaskPageResponse;
import com.basic.cloud.workflow.api.enums.ApproveActionEnum;
import com.basic.cloud.workflow.service.ProcessTaskService;
import com.basic.cloud.workflow.util.PaginationUtils;
import com.basic.cloud.workflow.util.QueryBuilder;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 流程任务相关 Service 实现
 *
 * @author vains
 */
@Service
@RequiredArgsConstructor
public class ProcessTaskServiceImpl implements ProcessTaskService {

    private final TaskService taskService;

    private final SysRoleClient sysRoleClient;

    private final RuntimeService runtimeService;

    private final RepositoryService repositoryService;

    private final SysBasicUserClient sysBasicUserClient;

    @Override
    public PageResult<TodoTaskPageResponse> todoTaskPage(FindTodoTaskPageRequest request) {
        String userId = SecurityUtils.getUserId() + "";

        TaskQuery serviceTaskQuery = taskService.createTaskQuery();
        serviceTaskQuery.or()
                .taskAssignee(userId)
                .taskCandidateUser(userId);

        // 获取角色
        Result<List<String>> roleIdsResult = sysRoleClient.findRoleIdsByUserId(SecurityUtils.getUserId());
        if (roleIdsResult != null && !ObjectUtils.isEmpty(roleIdsResult.getData())) {
            serviceTaskQuery.taskCandidateGroupIn(roleIdsResult.getData());
        }

        serviceTaskQuery.endOr();
        TaskQuery taskQuery = QueryBuilder.of(serviceTaskQuery)
                .apply(TaskQuery::active)
                .apply(TaskQuery::orderByTaskCreateTime)
                .apply(TaskQuery::desc)
                .build();

        // 查询总数
        long total = taskQuery.count();

        if (total == 0) {
            return PageResult.of(request.getCurrent(), request.getSize(), total, Collections.emptyList());
        }

        // 计算适用于框架内部分页的页码和每页行数
        PaginationUtils.PageParam param = PaginationUtils.calc(Math.toIntExact(request.getCurrent()), Math.toIntExact(request.getSize()));

        // 查询任务列表
        List<Task> tasks = taskQuery.listPage(param.firstResult(), param.maxResults());
        if (ObjectUtils.isEmpty(tasks)) {
            return PageResult.of(request.getCurrent(), request.getSize(), total, Collections.emptyList());
        }

        Set<String> defIds = tasks.stream()
                .map(Task::getProcessDefinitionId)
                .collect(Collectors.toSet());

        // 批量查询流程定义（避免 N+1）
        List<ProcessDefinition> defs = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionIds(defIds)
                .list();

        Map<String, ProcessDefinition> processDefMap = defs.stream()
                .collect(Collectors.toMap(
                        ProcessDefinition::getId,
                        Function.identity()
                ));


        // 提取流程定义的 ID
        Set<String> processInstanceIds = tasks.stream()
                .map(Task::getProcessInstanceId)
                .collect(Collectors.toSet());
        // 查询流程实例
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
                .processInstanceIds(processInstanceIds).list();

        // 流程定义id与启动用户id的映射map，key -> 流程定义id, value -> startUserId
        Map<String, String> instanceIdStartUserMap = processInstanceList
                .stream()
                .collect(Collectors.toMap(
                        ProcessInstance::getProcessInstanceId, ProcessInstance::getStartUserId, (k1, k2) -> k2)
                );

        // 所有用户 id
        Set<Long> userIds = instanceIdStartUserMap.values().stream().map(Long::valueOf).collect(Collectors.toSet());

        // 查询用户信息
        List<FindBasicUserResponse> userResponseList;
        if (!ObjectUtils.isEmpty(userIds)) {
            Result<List<FindBasicUserResponse>> listResult = sysBasicUserClient.getByIds(userIds);
            userResponseList = listResult.getData();
        } else {
            userResponseList = Collections.emptyList();
        }

        // 转map，方便获取
        Map<Long, FindBasicUserResponse> userMap = userResponseList.stream()
                .collect(Collectors.toMap(FindBasicUserResponse::getId, Function.identity(), (k1, k2) -> k2));

        // 组装 DTO
        List<TodoTaskPageResponse> records = tasks.stream()
                .map(task -> buildTodoTask(task, userId, processDefMap, instanceIdStartUserMap, userMap))
                .toList();

        return PageResult.of(request.getCurrent(), request.getSize(), total, records);
    }

    @Override
    public TaskApproveResponse taskApprove(TaskApproveRequest request) {
        String currentUserId = SecurityUtils.getUserId() + "";
        // 1查询任务
        Task task = taskService.createTaskQuery()
                .taskId(request.getTaskId())
                .singleResult();

        if (task == null) {
            throw new IllegalStateException("任务不存在或已完成");
        }

        // 校验是否是当前用户的待办
        if (!Objects.equals(currentUserId, task.getAssignee())) {
            throw new SecurityException("无权审批该任务");
        }

        String processInstanceId = task.getProcessInstanceId();

        // 添加审批意见
        if (StringUtils.hasText(request.getComment())) {
            taskService.addComment(
                    task.getId(),
                    processInstanceId,
                    request.getAction().getValue(),
                    request.getComment()
            );
        }

        // 组装流程变量
        Map<String, Object> variables = new HashMap<>();
        if (request.getVariables() != null) {
            variables.putAll(request.getVariables());
        }

        // 常用约定变量
        if (ApproveActionEnum.APPROVE.equals(request.getAction())) {
            variables.put("approved", true);
        } else if (ApproveActionEnum.REJECT.equals(request.getAction())) {
            variables.put("approved", false);
        }

        // 完成任务
        taskService.complete(task.getId(), currentUserId, variables);

        // 判断流程是否结束
        ProcessInstance instance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (instance == null) {
            // 已结束
            return new TaskApproveResponse(true, null, null, null);
        }

        // 查询下一节点 UserTask
        Task nextTask = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .singleResult();

        if (nextTask == null) {
            return new TaskApproveResponse(true, null, null, null);
        }

        return new TaskApproveResponse(
                false,
                nextTask.getId(),
                nextTask.getFormKey(),
                nextTask.getName()
        );
    }

    @Override
    public void claim(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new IllegalStateException("任务不存在.");
        }
        if (!ObjectUtils.isEmpty(task.getAssignee())) {
            throw new CloudServiceException("任务已被其他人领取.");
        }
        taskService.claim(taskId, SecurityUtils.getUserId() + "");
    }

    @Override
    public void unclaim(String taskId) {
        taskService.unclaim(taskId);
    }

    /**
     * 组装待办任务
     *
     * @param task                   任务
     * @param userId                 用户 ID
     * @param processDefMap          流程定义
     * @param instanceIdStartUserMap 实例 ID 与用户 ID
     * @param userMap                用户信息 Map
     * @return 待办任务响应
     */
    private TodoTaskPageResponse buildTodoTask(
            Task task,
            String userId,
            Map<String, ProcessDefinition> processDefMap,
            Map<String, String> instanceIdStartUserMap,
            Map<Long, FindBasicUserResponse> userMap
    ) {

        TodoTaskPageResponse dto = new TodoTaskPageResponse();

        // ---- task ----
        dto.setTaskId(task.getId());
        dto.setTaskDefinitionKey(task.getTaskDefinitionKey());
        dto.setTaskName(task.getName());
        dto.setAssignee(task.getAssignee());
        dto.setCreateTime(task.getCreateTime());

        // ---- process ----
        dto.setProcessInstanceId(task.getProcessInstanceId());
//        dto.setBusinessKey(task.getBusinessKey());

        ProcessDefinition pd = processDefMap.get(task.getProcessDefinitionId());
        if (pd != null) {
            dto.setProcessDefinitionKey(pd.getKey());
            dto.setProcessDefinitionName(pd.getName());
            dto.setProcessDefinitionVersion(pd.getVersion());
        }

        // 发起人
        String startUserId = instanceIdStartUserMap.get(task.getProcessInstanceId());
        dto.setStartUserId(startUserId);

        if (!ObjectUtils.isEmpty(startUserId)) {
            FindBasicUserResponse userResponse = userMap.get(Long.parseLong(startUserId));
            if (userResponse != null) {
                dto.setStartUserName(userResponse.getNickname());
            }
        }

        // ---- form ----
        String formKey = task.getFormKey();
        dto.setFormKey(formKey);
        /*if (formKey != null) {
            dto.setFormVersion(
                    formDefinitionService.getLatestVersion(formKey)
            );
        }*/

        // ---- 前端辅助 ----
        dto.setCanClaim(task.getAssignee() == null);
        dto.setInitiatorTask(userId.equals(task.getAssignee()));

        // 获取 BpmnModel 对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        Process mainProcess = bpmnModel != null ? bpmnModel.getMainProcess() : null;

        if (mainProcess == null) {
            return dto;
        }

        Collection<FlowElement> flowElements = mainProcess.getFlowElements();

        // 判断是否可归还任务
        Boolean canUnclaim = flowElements.stream()
                .filter(Objects::nonNull)
                .filter(flowElement -> flowElement instanceof UserTask)
                .map(flowElement -> (UserTask) flowElement)
                .filter(userTask -> task.getTaskDefinitionKey().equals(userTask.getId()))
                .findFirst()
                .map(userTask -> {
                    if (!ObjectUtils.isEmpty(userTask.getAssignee())) {
                        // 流程节点指定办理人：审批状态
                        return false; // 审批
                    } else {
                        // 未指定实际办理人：拾取状态
                        // 已有实际办理人：审批/归还状态
                        // 审批或归还
                        return !ObjectUtils.isEmpty(task.getAssignee());
                    }
                })
                .orElse(null);
        dto.setCanUnclaim(canUnclaim);

        return dto;
    }

}
