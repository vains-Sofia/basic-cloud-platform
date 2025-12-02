package com.basic.cloud.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.basic.cloud.workflow.api.constants.FlowableConstants;
import com.basic.cloud.workflow.api.domain.request.FindDefinitionHistoryPageRequest;
import com.basic.cloud.workflow.api.domain.request.FindDefinitionPageRequest;
import com.basic.cloud.workflow.api.domain.request.PublishProcessRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessDefinitionRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessDefinitionResponse;
import com.basic.cloud.workflow.api.domain.response.PublishProcessResponse;
import com.basic.cloud.workflow.api.enums.DefinitionStatusEnum;
import com.basic.cloud.workflow.domain.entity.ProcessDefinition;
import com.basic.cloud.workflow.mapper.ProcessDefinitionMapper;
import com.basic.cloud.workflow.service.ProcessDefinitionService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.exception.CloudServiceException;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * 针对表【process_definition(流程定义表)】的数据库操作Service实现
 *
 * @author vains
 */
@Service
@RequiredArgsConstructor
public class ProcessDefinitionServiceImpl extends ServiceImpl<ProcessDefinitionMapper, ProcessDefinition>
        implements ProcessDefinitionService {

    private final RepositoryService repositoryService;

    @Override
    public String saveProcessDefinition(SaveProcessDefinitionRequest processDefinitionRequest) {
        ProcessDefinition processDefinition = new ProcessDefinition();
        BeanUtils.copyProperties(processDefinitionRequest, processDefinition);

        // 查询当前最新版本的流程定义
        ProcessDefinition existsDefinition = baseMapper.selectLatestDefinition(processDefinitionRequest.getProcessKey());
        if (existsDefinition == null) {
            // 设置默认版本
            processDefinition.setVersion(1);
            // 设置默认状态
            processDefinition.setStatus(DefinitionStatusEnum.DRAFT);
        } else {
            if (existsDefinition.getStatus() == DefinitionStatusEnum.DISABLED) {
                throw new CloudServiceException(
                        "流程 [" + processDefinition.getProcessKey() + "] 已被禁用，无法修改.");
            }
            // 版本递增
            processDefinition.setVersion(existsDefinition.getVersion() + 1);
            // 置空id
            processDefinition.setId(null);
            processDefinition.setCreateTime(existsDefinition.getCreateTime());
        }

        // 添加流程定义
        baseMapper.insert(processDefinition);

        return processDefinition.getId() + "";
    }

    @Override
    public void updateProcessDefinition(Long id, SaveProcessDefinitionRequest processDefinitionRequest) {
        Assert.notNull(id, "主键id不能为空.");

        // 检验是否存在
        ProcessDefinition existingProcessDefinition = baseMapper.selectById(id);
        if (existingProcessDefinition == null) {
            throw new CloudServiceException("流程不存在.");
        }

        // 修改元数据，不新增版本
        ProcessDefinition processDefinition = new ProcessDefinition();
        BeanUtils.copyProperties(processDefinitionRequest, processDefinition);
        processDefinition.setStatus(existingProcessDefinition.getStatus());
        processDefinition.setVersion(existingProcessDefinition.getVersion());
        processDefinition.setId(id);
        processDefinition.setDeleted(existingProcessDefinition.getDeleted());
        baseMapper.updateById(processDefinition);
    }

    @Override
    public void deleteProcessDefinition(String processKey) {
        Assert.hasText(processKey, "流程定义key不能为空.");
        LambdaUpdateWrapper<ProcessDefinition> wrapper = Wrappers.lambdaUpdate(ProcessDefinition.class)
                .eq(ProcessDefinition::getProcessKey, processKey);
        baseMapper.delete(wrapper);
    }

    @Override
    public ProcessDefinitionResponse getProcessDefinition(Long id) {
        Assert.notNull(id, "主键id不能为空.");
        ProcessDefinition processDefinition = baseMapper.selectById(id);
        if (processDefinition != null) {
            ProcessDefinitionResponse processDefinitionResponse = new ProcessDefinitionResponse();
            BeanUtils.copyProperties(processDefinition, processDefinitionResponse);
            return processDefinitionResponse;
        }
        return null;
    }

    @Override
    public PageResult<PageProcessDefinitionResponse> getProcessDefinitionPage(FindDefinitionPageRequest request) {
        // 分页查询
        IPage<ProcessDefinition> paginated = baseMapper.
                selectLatestDefinitionsPage(Page.of(request.getCurrent(), request.getSize()), request);

        // 转为响应bean
        IPage<PageProcessDefinitionResponse> responsePage = paginated.convert(e -> {
            PageProcessDefinitionResponse processDefinitionResponse = new PageProcessDefinitionResponse();
            BeanUtils.copyProperties(e, processDefinitionResponse);
            return processDefinitionResponse;
        });

        return PageResult.of(
                responsePage.getCurrent(), responsePage.getSize(), responsePage.getTotal(), responsePage.getRecords());
    }

    @Override
    public PublishProcessResponse publishProcessDefinition(Long id, PublishProcessRequest request) {
        Assert.notNull(id, "主键id不能为空.");
        ProcessDefinition processDefinition = baseMapper.selectById(id);
        if (processDefinition == null) {
            throw new CloudServiceException("流程不存在.");
        }

        if (processDefinition.getStatus() == DefinitionStatusEnum.DISABLED) {
            throw new CloudServiceException("流程已被禁用，发布失败.");
        }

        // xml特殊处理
        String xml = resolveXml(processDefinition.getProcessXml());
        Assert.hasText(xml, "请绘制流程图以后再发布.");

        // 部署流程
        repositoryService.createDeployment()
                .addString((processDefinition.getProcessKey() + FlowableConstants.BPMN_XML_SUFFIX), xml)
                .key(processDefinition.getProcessKey())
                .name(processDefinition.getProcessName())
                .category(processDefinition.getCategory())
                .deploy();

        // 更新流程定义为发布状态
        if (!ObjectUtils.isEmpty(request.getRemark())) {
            processDefinition.setRemark(request.getRemark());
        }
        processDefinition.setStatus(DefinitionStatusEnum.PUBLISH);
        baseMapper.updateById(processDefinition);

        // 响应
        return new PublishProcessResponse(processDefinition.getProcessKey(), processDefinition.getVersion());
    }

    @Override
    public void toggleDefinitionStatus(Long id, DefinitionStatusEnum statusEnum) {
        Assert.notNull(id, "主键id不能为空.");
        ProcessDefinition processDefinition = baseMapper.selectById(id);
        if (processDefinition == null) {
            throw new CloudServiceException("流程不存在.");
        }
        if (statusEnum == null) {
            DefinitionStatusEnum status = processDefinition.getStatus();
            processDefinition.setStatus(
                    status == DefinitionStatusEnum.DRAFT ? DefinitionStatusEnum.DISABLED : DefinitionStatusEnum.DRAFT);
        } else {
            processDefinition.setStatus(statusEnum);
        }

        baseMapper.updateById(processDefinition);
    }

    @Override
    public PageResult<ProcessDefinitionResponse> getProcessDefinitionHistory(FindDefinitionHistoryPageRequest request) {
        LambdaQueryWrapper<ProcessDefinition> wrapper = Wrappers.lambdaQuery(ProcessDefinition.class)
                .eq(ProcessDefinition::getProcessKey, request.getProcessKey())
                .orderByDesc(ProcessDefinition::getUpdateTime);

        // 分页查询
        Page<ProcessDefinition> paginated = baseMapper.selectPage(Page.of(request.getCurrent(), request.getSize()), wrapper);

        IPage<ProcessDefinitionResponse> responsePage = paginated.convert(e -> {
            ProcessDefinitionResponse processDefinitionResponse = new ProcessDefinitionResponse();
            BeanUtils.copyProperties(e, processDefinitionResponse);
            return processDefinitionResponse;
        });

        return PageResult.of(
                responsePage.getCurrent(), responsePage.getSize(), responsePage.getTotal(), responsePage.getRecords());
    }

    @Override
    public ProcessDefinitionResponse getByProcessKey(String processKey) {
        Assert.notNull(processKey, "流程定义key不能为空.");
        ProcessDefinition processDefinition = baseMapper.selectLatestDefinition(processKey);
        if (processDefinition != null) {
            ProcessDefinitionResponse processDefinitionResponse = new ProcessDefinitionResponse();
            BeanUtils.copyProperties(processDefinition, processDefinitionResponse);
            return processDefinitionResponse;
        }
        return null;
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




