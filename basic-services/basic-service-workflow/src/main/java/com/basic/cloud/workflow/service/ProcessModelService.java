package com.basic.cloud.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.basic.cloud.workflow.api.domain.request.FindModelHistoryPageRequest;
import com.basic.cloud.workflow.api.domain.request.FindModelPageRequest;
import com.basic.cloud.workflow.api.domain.request.PublishProcessRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessModelRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessModelResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessModelResponse;
import com.basic.cloud.workflow.api.domain.response.PublishProcessResponse;
import com.basic.cloud.workflow.api.enums.DefinitionStatusEnum;
import com.basic.cloud.workflow.domain.entity.ProcessModel;
import com.basic.framework.core.domain.PageResult;

/**
 * 针对表【process_definition(流程模型表)】的数据库操作Service
 *
 * @author vains
 */
public interface ProcessModelService extends IService<ProcessModel> {

    /**
     * 新增流程模型，BPMN 设计器暂存草稿使用
     *
     * @param processModelRequest 保存流程模型入参
     * @return 主键id
     */
    String saveProcessModel(SaveProcessModelRequest processModelRequest);

    /**
     * 修改流程模型
     *
     * @param id                  主键id
     * @param processModelRequest 新的流程模型数据
     */
    void updateProcessModel(Long id, SaveProcessModelRequest processModelRequest);

    /**
     * 删除流程模型
     *
     * @param processKey 流程定义key
     */
    void deleteProcessModel(String processKey);

    /**
     * 查询流程模型详情
     *
     * @param id 主键id
     * @return 详情
     */
    ProcessModelResponse getProcessModel(Long id);

    /**
     * 分页查询流程模型
     *
     * @param request 分页查询入参
     * @return 流程模型分页结果
     */
    PageResult<PageProcessModelResponse> getProcessModelPage(FindModelPageRequest request);

    /**
     * 发布流程模型
     *
     * @param id      流程模型主键id
     * @param request 发布入参
     * @return 发布的流程定义key与版本
     */
    PublishProcessResponse publishProcessModel(Long id, PublishProcessRequest request);

    /**
     * 切换流程模型的禁用/启用状态
     *
     * @param id         主键id
     * @param statusEnum 状态枚举
     */
    void toggleModelStatus(Long id, DefinitionStatusEnum statusEnum);

    /**
     * 根据流程定义key分页查询流程模型历史版本
     *
     * @param request 流程定义key与分页参数
     * @return 分页的历史版本数据
     */
    PageResult<ProcessModelResponse> getProcessModelHistory(FindModelHistoryPageRequest request);

    /**
     * 根据流程定义key查询流程模型
     *
     * @param processKey 流程定义key
     * @return 流程模型详情
     */
    ProcessModelResponse getByProcessKey(String processKey);

    /**
     * 回退processKey对应的模型模型至version版本
     *
     * @param processKey 流程定义key
     * @param version    版本号
     */
    void rollback(String processKey, Integer version);
}
