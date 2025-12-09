package com.basic.cloud.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.basic.cloud.workflow.api.constants.FlowableConstants;
import com.basic.cloud.workflow.api.domain.request.FindModelHistoryPageRequest;
import com.basic.cloud.workflow.api.domain.request.FindModelPageRequest;
import com.basic.cloud.workflow.api.domain.request.PublishProcessRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessModelRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessModelResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessModelResponse;
import com.basic.cloud.workflow.api.domain.response.PublishProcessResponse;
import com.basic.cloud.workflow.api.enums.DefinitionStatusEnum;
import com.basic.cloud.workflow.domain.entity.ProcessModel;
import com.basic.cloud.workflow.mapper.ProcessModelMapper;
import com.basic.cloud.workflow.service.ProcessModelService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.exception.CloudServiceException;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 针对表【process_definition(流程定义表)】的数据库操作Service实现
 *
 * @author vains
 */
@Service
@RequiredArgsConstructor
public class ProcessModelServiceImpl extends ServiceImpl<ProcessModelMapper, ProcessModel>
        implements ProcessModelService {

    private final RepositoryService repositoryService;

    @Override
    public String saveProcessModel(SaveProcessModelRequest processModelRequest) {
        ProcessModel processModel = new ProcessModel();
        BeanUtils.copyProperties(processModelRequest, processModel);

        // 查询当前最新版本的流程定义
        ProcessModel existsDefinition = baseMapper.selectLatestDefinition(processModelRequest.getProcessKey());
        if (existsDefinition == null) {
            // 设置默认版本
            processModel.setVersion(1);
            // 设置默认状态
            processModel.setStatus(DefinitionStatusEnum.DRAFT);
        } else {
            if (existsDefinition.getStatus() == DefinitionStatusEnum.DISABLED) {
                throw new CloudServiceException(
                        "流程 [" + processModel.getProcessKey() + "] 已被禁用，无法修改.");
            }
            // 版本递增
            processModel.setVersion(existsDefinition.getVersion() + 1);
            processModel.setCreateTime(existsDefinition.getCreateTime());
        }

        // 添加流程定义
        baseMapper.insert(processModel);

        return processModel.getId() + "";
    }

    @Override
    public void updateProcessModel(Long id, SaveProcessModelRequest processModelRequest) {
        Assert.notNull(id, "主键id不能为空.");

        // 检验是否存在
        ProcessModel existingProcessModel = baseMapper.selectById(id);
        if (existingProcessModel == null) {
            throw new CloudServiceException("流程不存在.");
        }

        // 修改元数据，不新增版本
        ProcessModel processModel = new ProcessModel();
        BeanUtils.copyProperties(processModelRequest, processModel);
        processModel.setStatus(existingProcessModel.getStatus());
        processModel.setVersion(existingProcessModel.getVersion());
        processModel.setId(id);
        processModel.setDeleted(existingProcessModel.getDeleted());
        baseMapper.updateById(processModel);
    }

    @Override
    public void deleteProcessModel(String processKey) {
        Assert.hasText(processKey, "流程定义key不能为空.");
        LambdaUpdateWrapper<ProcessModel> wrapper = Wrappers.lambdaUpdate(ProcessModel.class)
                .eq(ProcessModel::getProcessKey, processKey);
        baseMapper.delete(wrapper);
    }

    @Override
    public ProcessModelResponse getProcessModel(Long id) {
        Assert.notNull(id, "主键id不能为空.");
        ProcessModel processModel = baseMapper.selectById(id);
        if (processModel != null) {
            ProcessModelResponse processModelResponse = new ProcessModelResponse();
            BeanUtils.copyProperties(processModel, processModelResponse);
            return processModelResponse;
        }
        return null;
    }

    @Override
    public PageResult<PageProcessModelResponse> getProcessModelPage(FindModelPageRequest request) {
        // 分页查询
        IPage<ProcessModel> paginated = baseMapper.
                selectLatestDefinitionsPage(Page.of(request.getCurrent(), request.getSize()), request);

        // 转为响应bean
        IPage<PageProcessModelResponse> responsePage = paginated.convert(e -> {
            PageProcessModelResponse processDefinitionResponse = new PageProcessModelResponse();
            BeanUtils.copyProperties(e, processDefinitionResponse);
            return processDefinitionResponse;
        });

        return PageResult.of(
                responsePage.getCurrent(), responsePage.getSize(), responsePage.getTotal(), responsePage.getRecords());
    }

    @Override
    public PublishProcessResponse publishProcessModel(Long id, PublishProcessRequest request) {
        Assert.notNull(id, "主键id不能为空.");
        ProcessModel processModel = baseMapper.selectById(id);
        if (processModel == null) {
            throw new CloudServiceException("流程不存在.");
        }

        if (processModel.getStatus() == DefinitionStatusEnum.DISABLED) {
            throw new CloudServiceException("流程已被禁用，发布失败.");
        }

        // xml特殊处理
        String xml = resolveXml(processModel.getProcessXml());
        Assert.hasText(xml, "请绘制流程图以后再发布.");

        // 部署流程
        repositoryService.createDeployment()
                .addString((processModel.getProcessKey() + FlowableConstants.BPMN_XML_SUFFIX), xml)
                .key(processModel.getProcessKey())
                .name(processModel.getProcessName())
                .category(processModel.getCategory())
                .deploy();

        // 更新流程定义为发布状态
        if (!ObjectUtils.isEmpty(request.getRemark())) {
            processModel.setRemark(request.getRemark());
        }
        processModel.setStatus(DefinitionStatusEnum.PUBLISH);
        baseMapper.updateById(processModel);

        // 响应
        return new PublishProcessResponse(processModel.getProcessKey(), processModel.getVersion());
    }

    @Override
    public void toggleModelStatus(Long id, DefinitionStatusEnum statusEnum) {
        Assert.notNull(id, "主键id不能为空.");
        ProcessModel processModel = baseMapper.selectById(id);
        if (processModel == null) {
            throw new CloudServiceException("流程不存在.");
        }
        if (statusEnum == null) {
            DefinitionStatusEnum status = processModel.getStatus();
            processModel.setStatus(
                    status == DefinitionStatusEnum.DRAFT ? DefinitionStatusEnum.DISABLED : DefinitionStatusEnum.DRAFT);
        } else {
            processModel.setStatus(statusEnum);
        }

        baseMapper.updateById(processModel);
    }

    @Override
    public PageResult<ProcessModelResponse> getProcessModelHistory(FindModelHistoryPageRequest request) {
        LambdaQueryWrapper<ProcessModel> wrapper = Wrappers.lambdaQuery(ProcessModel.class)
                .eq(ProcessModel::getProcessKey, request.getProcessKey())
                .orderByDesc(ProcessModel::getUpdateTime);

        // 分页查询
        Page<ProcessModel> paginated = baseMapper.selectPage(Page.of(request.getCurrent(), request.getSize()), wrapper);

        IPage<ProcessModelResponse> responsePage = paginated.convert(e -> {
            ProcessModelResponse processModelResponse = new ProcessModelResponse();
            BeanUtils.copyProperties(e, processModelResponse);
            return processModelResponse;
        });

        return PageResult.of(
                responsePage.getCurrent(), responsePage.getSize(), responsePage.getTotal(), responsePage.getRecords());
    }

    @Override
    public ProcessModelResponse getByProcessKey(String processKey) {
        Assert.notNull(processKey, "流程定义key不能为空.");
        ProcessModel processModel = baseMapper.selectLatestDefinition(processKey);
        if (processModel != null) {
            ProcessModelResponse processModelResponse = new ProcessModelResponse();
            BeanUtils.copyProperties(processModel, processModelResponse);
            return processModelResponse;
        }
        return null;
    }

    @Override
    public void rollback(String processKey, Integer version) {
        LambdaQueryWrapper<ProcessModel> wrapper = Wrappers.<ProcessModel>lambdaQuery()
                .eq(ProcessModel::getProcessKey, processKey)
                .eq(ProcessModel::getVersion, version);
        List<ProcessModel> processModels = baseMapper.selectList(wrapper);
        Assert.notEmpty(processModels, "流程定义不存在.");

        ProcessModel processModel = processModels.getFirst();
        SaveProcessModelRequest request = new SaveProcessModelRequest();
        BeanUtils.copyProperties(processModel, request);
        request.setRemark("回退版本至v" + version);
        this.saveProcessModel(request);
    }

    /**
     * Camunda提供的扩展属性flowable无法发起流程
     *
     * @param processXml 流程xml
     * @return 适用于Flowable的流程xml
     */
    private String resolveXml(String processXml) {
        Assert.hasText(processXml, "请绘制流程图以后再发布.");
        String bpmnXml = processXml.replaceAll("xmlns:camunda=\"http://camunda.org/schema/1.0/bpmn\"", "xmlns:flowable=\"http://flowable.org/bpmn\" ");
        bpmnXml = bpmnXml.replaceAll("targetNamespace=\"http://bpmn.io/bpmn\"", "targetNamespace=\"http://www.flowable.org/processdef\"");
        bpmnXml = bpmnXml.replaceAll("exporterVersion=\"5.1.2\"", "exporterVersion=\"7.1.0\"");
        bpmnXml = bpmnXml.replaceAll("camunda:", "flowable:");
        return bpmnXml;
    }
}




