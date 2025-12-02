package com.basic.cloud.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.basic.cloud.workflow.api.domain.request.FindDefinitionHistoryPageRequest;
import com.basic.cloud.workflow.api.domain.request.FindDefinitionPageRequest;
import com.basic.cloud.workflow.api.domain.request.PublishProcessRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessDefinitionRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.PublishProcessResponse;
import com.basic.cloud.workflow.api.enums.DefinitionStatusEnum;
import com.basic.cloud.workflow.domain.entity.ProcessDefinition;
import com.basic.framework.core.domain.PageResult;

/**
 * 针对表【process_definition(流程定义表)】的数据库操作Service
 *
 * @author vains
 */
public interface ProcessDefinitionService extends IService<ProcessDefinition> {

    /**
     * 新增流程定义，BPMN 设计器暂存草稿使用
     *
     * @param processDefinitionRequest 保存流程定义入参
     * @return 主键id
     */
    String saveProcessDefinition(SaveProcessDefinitionRequest processDefinitionRequest);

    /**
     * 修改流程定义
     *
     * @param id                       主键id
     * @param processDefinitionRequest 新的流程定义数据
     */
    void updateProcessDefinition(Long id, SaveProcessDefinitionRequest processDefinitionRequest);

    /**
     * 删除流程定义
     *
     * @param processKey 流程定义key
     */
    void deleteProcessDefinition(String processKey);

    /**
     * 查询流程定义详情
     *
     * @param id 主键id
     * @return 详情
     */
    ProcessDefinitionResponse getProcessDefinition(Long id);

    /**
     * 分页查询流程定义
     *
     * @param request 分页查询入参
     * @return 流程定义分页结果
     */
    PageResult<PageProcessDefinitionResponse> getProcessDefinitionPage(FindDefinitionPageRequest request);

    /**
     * 发布流程定义
     *
     * @param id      流程定义主键id
     * @param request 发布入参
     * @return 发布的流程定义key与版本
     */
    PublishProcessResponse publishProcessDefinition(Long id, PublishProcessRequest request);

    /**
     * 切换流程定义的禁用/启用状态
     *
     * @param id         主键id
     * @param statusEnum 状态枚举
     */
    void toggleDefinitionStatus(Long id, DefinitionStatusEnum statusEnum);

    /**
     * 根据流程定义key分页查询流程定义历史版本
     *
     * @param request 流程定义key与分页参数
     * @return 分页的历史版本数据
     */
    PageResult<ProcessDefinitionResponse> getProcessDefinitionHistory(FindDefinitionHistoryPageRequest request);

    /**
     * 根据流程定义key查询流程定义
     *
     * @param processKey 流程定义key
     * @return 流程定义详情
     */
    ProcessDefinitionResponse getByProcessKey(String processKey);
}
