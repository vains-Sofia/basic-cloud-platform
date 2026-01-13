package com.basic.cloud.workflow.service.impl;

import com.basic.cloud.system.api.SysBasicUserClient;
import com.basic.cloud.system.api.SysRoleClient;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.cloud.workflow.api.domain.request.CancelProcessInstanceRequest;
import com.basic.cloud.workflow.api.domain.request.FindProcessInstanceRequest;
import com.basic.cloud.workflow.api.domain.request.FindTodoTaskPageRequest;
import com.basic.cloud.workflow.api.domain.request.TaskApproveRequest;
import com.basic.cloud.workflow.api.domain.response.*;
import com.basic.cloud.workflow.api.enums.ApproveActionEnum;
import com.basic.cloud.workflow.domain.entity.ProcessForm;
import com.basic.cloud.workflow.mapper.ProcessFormMapper;
import com.basic.cloud.workflow.service.ProcessTaskService;
import com.basic.cloud.workflow.util.PaginationUtils;
import com.basic.cloud.workflow.util.QueryBuilder;
import com.basic.cloud.workflow.util.TimeFormatter;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
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

    private final HistoryService historyService;

    private final ProcessFormMapper processFormMapper;

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
        // 设置任务变量
        taskService.setVariablesLocal(task.getId(), variables);

        if (ApproveActionEnum.APPROVE.equals(request.getAction())) {
            // 常用的流程全局变量 - 审批通过
            variables.put("approved", true);
        } else {
            // 常用的流程全局变量 - 拒绝
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

    @Override
    public ProcessTaskDetailResponse getProcessTaskDetail(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).active().singleResult();
        if (task == null) {
            throw new IllegalStateException("任务不存在或已结束");
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .active()
                .singleResult();
        if (processInstance == null) {
            throw new IllegalStateException("流程不存在或已结束");
        }

        ProcessTaskDetailResponse detailResponse = new ProcessTaskDetailResponse();
        detailResponse.setTaskId(taskId);
        detailResponse.setProcessInstanceId(processInstance.getId());
        detailResponse.setTaskName(task.getName());
        detailResponse.setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion());
        detailResponse.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        detailResponse.setProcessDefinitionName(processInstance.getProcessDefinitionName());

        if (!ObjectUtils.isEmpty(task.getFormKey())) {
            // TODO 待修改为部署时备份的表单 scheme
            ProcessForm processForm = processFormMapper.selectById(task.getFormKey());
            if (processForm != null) {
                detailResponse.setFormContent(processForm.getFormContent());
            }
        }

        // 查询该流程实例下所有已完成的历史任务
        List<HistoricTaskInstance> tasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstance.getId())
                .includeTaskLocalVariables()
                // 只查询已完成的
                .finished()
                .orderByHistoricTaskInstanceEndTime().asc()
                .list();

        if (ObjectUtils.isEmpty(tasks)) {
            return detailResponse;
        }

        Set<Long> userIds = tasks.stream()
                .map(HistoricTaskInstance::getAssignee)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
        // 查询用户信息
        Result<List<FindBasicUserResponse>> listResult = sysBasicUserClient.getByIds(userIds);
        List<FindBasicUserResponse> userResponseList = listResult.getData();

        // 转map，方便获取
        Map<Long, FindBasicUserResponse> userMap = userResponseList.stream()
                .collect(Collectors.toMap(FindBasicUserResponse::getId, Function.identity(), (k1, k2) -> k2));

        // 查询表单 schema TODO 替换为部署时备份的 schema
        Set<String> formKeys = tasks.stream().map(HistoricTaskInstance::getFormKey).collect(Collectors.toSet());
        List<ProcessForm> processForms = processFormMapper.selectByIds(formKeys);
        if (ObjectUtils.isEmpty(processForms)) {
            return detailResponse;
        }

        Map<Long, ProcessForm> processFormMap = processForms.stream()
                .collect(Collectors.toMap(ProcessForm::getId, Function.identity(),
                        (k1, k2) -> k2));

        List<FinishedTaskResponse> finishedTasks = tasks.stream().map(e -> {
            FinishedTaskResponse finishedTaskResponse = new FinishedTaskResponse();
            finishedTaskResponse.setTaskId(e.getId());
            finishedTaskResponse.setTaskName(e.getName());
            FindBasicUserResponse userResponse = userMap.get(Long.parseLong(e.getAssignee()));
            if (userResponse == null) {
                finishedTaskResponse.setAssignee(e.getAssignee());
            } else {
                finishedTaskResponse.setAssignee(userResponse.getNickname());
            }
            finishedTaskResponse.setEndTime(e.getEndTime());
            if (!ObjectUtils.isEmpty(e.getFormKey())) {
                ProcessForm processForm = processFormMap.get(Long.parseLong(e.getFormKey()));
                if (!ObjectUtils.isEmpty(processForm)) {
                    finishedTaskResponse.setFormContent(processForm.getFormContent());
                }
            }
            Map<String, Object> formData = e.getTaskLocalVariables();
            finishedTaskResponse.setFormData(formData);
            return finishedTaskResponse;
        }).toList();

        detailResponse.setFinishedTasks(finishedTasks);

        return detailResponse;
    }

    @Override
    public PageResult<ProcessInstanceResponse> getMyProcessInstance(FindProcessInstanceRequest request) {
        // 当前登录用户 ID
        String currentUserId = SecurityUtils.getUserId() + "";

        // 构建查询参数
        HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        HistoricProcessInstanceQuery query = QueryBuilder.of(processInstanceQuery)
                .like(processInstanceQuery::processDefinitionKeyLike, request.getProcessDefinitionKey())
                .eq(processInstanceQuery::startedBy, currentUserId)
                .apply(HistoricProcessInstanceQuery::orderByProcessInstanceStartTime)
                .apply(HistoricProcessInstanceQuery::desc)
                .build();

        // 查询总数量
        long count = query.count();
        if (count == 0) {
            return PageResult.of(request.getCurrent(), request.getSize(), count, Collections.emptyList());
        }

        // 计算适用于框架内部分页的页码和每页行数
        PaginationUtils.PageParam param = PaginationUtils.calc(Math.toIntExact(request.getCurrent()), Math.toIntExact(request.getSize()));

        List<HistoricProcessInstance> processInstances = query.listPage(param.firstResult(), param.maxResults());
        if (ObjectUtils.isEmpty(processInstances)) {
            return PageResult.of(request.getCurrent(), request.getSize(), count, Collections.emptyList());
        }

        // 进行中的流程实例 ID
        Set<String> runningProcessInstances = processInstances.stream()
                .filter(e -> ObjectUtils.isEmpty(e.getEndTime()))
                .map(HistoricProcessInstance::getId)
                .collect(Collectors.toSet());

        Map<String, Task> instanceTaskMap;
        if (!ObjectUtils.isEmpty(runningProcessInstances)) {
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceIdIn(runningProcessInstances)
                    .active()
                    .list();


            if (!ObjectUtils.isEmpty(tasks)) {
                instanceTaskMap = tasks.stream()
                        .collect(Collectors.toMap(Task::getProcessInstanceId, Function.identity(), (k1, k2) -> k1));
            } else {
                instanceTaskMap = Collections.emptyMap();
            }
        } else {
            instanceTaskMap = Collections.emptyMap();
        }


        // 提取用户 ID
        Set<Long> userIdList;
        if (!ObjectUtils.isEmpty(instanceTaskMap)) {
            userIdList = instanceTaskMap.values()
                    .stream()
                    .map(Task::getAssignee)
                    .filter(assignee -> !ObjectUtils.isEmpty(assignee))
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
        } else {
            userIdList = Collections.emptySet();
        }

        // 查询用户
        List<FindBasicUserResponse> userResponseList;
        if (!ObjectUtils.isEmpty(userIdList)) {
            Result<List<FindBasicUserResponse>> listResult = sysBasicUserClient.getByIds(userIdList);
            userResponseList = listResult.getData();
        } else {
            userResponseList = Collections.emptyList();
        }

        // 用户信息转 Map
        Map<Long, FindBasicUserResponse> userResponseMap;
        if (!ObjectUtils.isEmpty(userResponseList)) {
            userResponseMap = userResponseList.stream()
                    .collect(Collectors.toMap(FindBasicUserResponse::getId, Function.identity()));
        } else {
            userResponseMap = Collections.emptyMap();
        }

        List<ProcessInstanceResponse> responseList = processInstances.stream()
                .map(instance -> {
                    ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
                    processInstanceResponse.setProcessInstanceId(instance.getId());
                    processInstanceResponse.setProcessDefinitionId(instance.getProcessDefinitionId());
                    processInstanceResponse.setProcessDefinitionName(instance.getProcessDefinitionName());
                    processInstanceResponse.setProcessDefinitionVersion(instance.getProcessDefinitionVersion());
                    processInstanceResponse.setProcessDefinitionKey(instance.getProcessDefinitionKey());
                    processInstanceResponse.setBusinessKey(instance.getBusinessKey());
                    processInstanceResponse.setStartTime(instance.getStartTime());
                    processInstanceResponse.setEndTime(instance.getEndTime());
                    if (!ObjectUtils.isEmpty(instance.getEndTime())) {
                        processInstanceResponse.setStatus("COMPLETED");
                        processInstanceResponse.setDurationInMillis(instance.getDurationInMillis());
                    } else {
                        processInstanceResponse.setStatus("RUNNING");
                        processInstanceResponse.setDurationInMillis(System.currentTimeMillis() - instance.getStartTime().getTime());
                    }

                    processInstanceResponse.setFormattedDuration(TimeFormatter.getDate(processInstanceResponse.getDurationInMillis()));

                    processInstanceResponse.setSuspended(false);
                    processInstanceResponse.setEnded(!ObjectUtils.isEmpty(instance.getEndTime()));

                    if (!ObjectUtils.isEmpty(instanceTaskMap)) {
                        Task task = instanceTaskMap.get(instance.getId());
                        if (!ObjectUtils.isEmpty(task)) {
                            processInstanceResponse.setCurrentActivityId(task.getTaskDefinitionKey());
                            processInstanceResponse.setCurrentActivityName(task.getName());
                            String assignee = task.getAssignee();
                            if (!ObjectUtils.isEmpty(assignee)) {
                                // 设置办理人
                                processInstanceResponse.setAssigneeId(assignee);
                                FindBasicUserResponse userResponse = userResponseMap.get(Long.parseLong(assignee));
                                if (userResponse != null) {
                                    processInstanceResponse.setAssigneeName(userResponse.getNickname());
                                }
                            }
                        }
                    }

                    return processInstanceResponse;
                }).toList();

        return PageResult.of(request.getCurrent(), request.getSize(), count, responseList);
    }

    @Override
    public void cancelProcessInstance(CancelProcessInstanceRequest request) {
        String currentUserId = SecurityUtils.getUserId() + "";
        String nickname;
        if (SecurityUtils.getAuthenticatedUser() != null) {
            nickname = SecurityUtils.getAuthenticatedUser().getNickname();
        } else {
            nickname = "";
        }

        // 验证流程是否存在且属于当前用户
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(request.getProcessInstanceId())
                // 确保只能取消自己发起的
                .startedBy(currentUserId)
                .singleResult();

        if (instance == null) {
            throw new CloudServiceException("流程不存在或没有操作权限");
        }

        // 检查流程是否已结束
        if (instance.isEnded()) {
            throw new CloudServiceException("流程已结束，无法取消");
        }

        // 设置流程变量记录取消信息
        Map<String, Object> variables = new HashMap<>();
        variables.put("cancelReason", request.getReason());
        variables.put("cancelOperator", currentUserId);
        variables.put("cancelTime", new Date());

        runtimeService.setVariables(instance.getId(), variables);

        String reason;
        if (!ObjectUtils.isEmpty(request.getReason())) {
            reason = request.getReason();
        } else {
            reason = nickname + "取消了申请";
        }

        // 取消流程实例
        runtimeService.deleteProcessInstance(request.getProcessInstanceId(), reason);
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
