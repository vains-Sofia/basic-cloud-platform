package com.basic.cloud.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.basic.cloud.workflow.api.domain.request.FindProcessFormPageRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessFormRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessFormResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessFormResponse;
import com.basic.cloud.workflow.domain.entity.ProcessForm;
import com.basic.cloud.workflow.mapper.ProcessFormMapper;
import com.basic.cloud.workflow.service.ProcessFormService;
import com.basic.framework.core.domain.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 针对表【process_form(表单设计配置表)】的数据库操作Service实现
 *
 * @author vains
 */
@Service
public class ProcessFormServiceImpl extends ServiceImpl<ProcessFormMapper, ProcessForm>
        implements ProcessFormService {

    @Override
    public String saveProcessForm(SaveProcessFormRequest request) {
        ProcessForm processForm = new ProcessForm();
        BeanUtils.copyProperties(request, processForm);
        baseMapper.insert(processForm);
        return processForm.getId() + "";
    }

    @Override
    public String updateProcessForm(Long id, SaveProcessFormRequest request) {
        ProcessForm processForm = new ProcessForm();
        BeanUtils.copyProperties(request, processForm);
        processForm.setId(id);
        baseMapper.updateById(processForm);
        return processForm.getId() + "";
    }

    @Override
    public void deleteProcessForm(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public ProcessFormResponse getProcessFormById(String id) {
        ProcessForm processForm = baseMapper.selectById(id);
        ProcessFormResponse formResponse = new ProcessFormResponse();
        BeanUtils.copyProperties(processForm, formResponse);
        return formResponse;
    }

    @Override
    public PageResult<PageProcessFormResponse> pageProcessForm(FindProcessFormPageRequest request) {
        Page<ProcessForm> formPage = Page.of(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<ProcessForm> wrapper = Wrappers.lambdaQuery(ProcessForm.class)
                .like(!ObjectUtils.isEmpty(request.getTitle()), ProcessForm::getTitle, request.getTitle())
                .like(!ObjectUtils.isEmpty(request.getDescription()), ProcessForm::getDescription, request.getDescription())
                .orderByDesc(ProcessForm::getCreateTime);
        Page<ProcessForm> selectedPage = baseMapper.selectPage(formPage, wrapper);
        IPage<PageProcessFormResponse> convert = selectedPage.convert(e -> {
            PageProcessFormResponse formResponse = new PageProcessFormResponse();
            BeanUtils.copyProperties(e, formResponse);
            return formResponse;
        });
        return PageResult.of(convert.getCurrent(), convert.getSize(), convert.getTotal(), convert.getRecords());
    }
}




