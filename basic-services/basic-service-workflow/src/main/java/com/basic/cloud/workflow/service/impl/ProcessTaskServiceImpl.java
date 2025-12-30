package com.basic.cloud.workflow.service.impl;

import com.basic.cloud.system.api.SysBasicUserClient;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.cloud.workflow.api.domain.request.FindTodoTaskPageRequest;
import com.basic.cloud.workflow.api.domain.request.StartProcessRequest;
import com.basic.cloud.workflow.api.domain.request.TaskApproveRequest;
import com.basic.cloud.workflow.api.domain.response.*;
import com.basic.cloud.workflow.service.ProcessTaskService;
import com.basic.cloud.workflow.util.PaginationUtils;
import com.basic.cloud.workflow.util.QueryBuilder;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.engine.IdentityService;
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

    private final RuntimeService runtimeService;

    private final IdentityService identityService;

    private final RepositoryService repositoryService;

    private final SysBasicUserClient sysBasicUserClient;

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

    @Override
    public PageResult<TodoTaskPageResponse> todoTaskPage(FindTodoTaskPageRequest request) {
        String userId = SecurityUtils.getUserId() + "";

        TaskQuery serviceTaskQuery = taskService.createTaskQuery();
        serviceTaskQuery.or()
                .taskAssignee(userId)
                .taskCandidateUser(userId)
                .endOr();
        TaskQuery taskQuery = QueryBuilder.of(serviceTaskQuery)
                .apply(TaskQuery::active)
                .apply(TaskQuery::orderByTaskCreateTime)
                .apply(TaskQuery::desc)
                .build();

        // 1️⃣ 查询总数
        long total = taskQuery.count();

        if (total == 0) {
            return PageResult.of(request.getCurrent(), request.getSize(), total, Collections.emptyList());
        }

        // 计算适用于框架内部分页的页码和每页行数
        PaginationUtils.PageParam param = PaginationUtils.calc(Math.toIntExact(request.getCurrent()), Math.toIntExact(request.getSize()));

        // 2️⃣ 查询任务列表
        List<Task> tasks = taskService.createTaskQuery()
                .active()
                .or()
                .taskAssignee(userId)
                .taskCandidateUser(userId)
                .endOr()
                .orderByTaskCreateTime()
                .desc()
                .listPage(param.firstResult(), param.maxResults());

        // 3️⃣ 批量查询流程定义（避免 N+1）
        Map<String, ProcessDefinition> processDefMap =
                loadProcessDefinitions(tasks);

        // 提取流程定义id
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

        // 所有用户id
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

        // 根据用户id查询用户信息

        // 4️⃣ 组装 DTO
        List<TodoTaskPageResponse> records = tasks.stream()
                .map(task -> buildTodoTask(task, userId, processDefMap, instanceIdStartUserMap, userMap))
                .toList();

        return PageResult.of(request.getCurrent(), request.getSize(), total, records);
    }

    @Override
    public TaskApproveResponse taskApprove(TaskApproveRequest request) {
        String currentUserId = SecurityUtils.getUserId() + "";
        // 1️⃣ 查询任务
        Task task = taskService.createTaskQuery()
                .taskId(request.getTaskId())
                .singleResult();

        if (task == null) {
            throw new IllegalStateException("任务不存在或已完成");
        }

        // 2️⃣ 校验是否是当前用户的待办
        if (!Objects.equals(currentUserId, task.getAssignee())) {
            throw new SecurityException("无权审批该任务");
        }

        String processInstanceId = task.getProcessInstanceId();

        // 3️⃣ 添加审批意见
        if (StringUtils.hasText(request.getComment())) {
            taskService.addComment(
                    task.getId(),
                    processInstanceId,
                    request.getAction(),
                    request.getComment()
            );
        }

        // 4️⃣ 组装流程变量
        Map<String, Object> variables = new HashMap<>();
        if (request.getVariables() != null) {
            variables.putAll(request.getVariables());
        }

        // 常用约定变量
        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            variables.put("approved", true);
        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {
            variables.put("approved", false);
        }

        // 5️⃣ 完成任务
        taskService.complete(task.getId(), variables);

        // 6️⃣ 判断流程是否结束
        ProcessInstance instance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (instance == null) {
            // 已结束
            return new TaskApproveResponse(true, null, null, null);
        }

        // 7️⃣ 查询下一节点 UserTask
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

        return dto;
    }

    private Map<String, ProcessDefinition> loadProcessDefinitions(
            List<Task> tasks
    ) {

        Set<String> defIds = tasks.stream()
                .map(Task::getProcessDefinitionId)
                .collect(Collectors.toSet());

        if (defIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<ProcessDefinition> defs = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionIds(defIds)
                .list();

        return defs.stream()
                .collect(Collectors.toMap(
                        ProcessDefinition::getId,
                        Function.identity()
                ));
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
