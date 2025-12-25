package com.basic.cloud.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.basic.cloud.workflow.api.domain.request.FindProcessFormPageRequest;
import com.basic.cloud.workflow.api.domain.request.SaveProcessFormRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessFormResponse;
import com.basic.cloud.workflow.api.domain.response.ProcessFormResponse;
import com.basic.cloud.workflow.domain.entity.ProcessForm;
import com.basic.framework.core.domain.PageResult;

/**
 * 针对表【process_form(表单设计配置表)】的数据库操作Service
 *
 * @author vains
 */
public interface ProcessFormService extends IService<ProcessForm> {

    /**
     * 保存流程表单设计接口
     *
     * @param request 流程表单
     * @return 主键id
     */
    String saveProcessForm(SaveProcessFormRequest request);

    /**
     * 修改流程表单设计接口
     *
     * @param id      主键ID
     * @param request 表单设计
     * @return 主键
     */
    String updateProcessForm(Long id, SaveProcessFormRequest request);

    /**
     * 根据主键ID删除表单设计
     *
     * @param id 主键ID
     */
    void deleteProcessForm(String id);

    /**
     * 获取主键id详情
     *
     * @param id 主键ID
     * @return 表单设计详情
     */
    ProcessFormResponse getProcessFormById(String id);

    /**
     * 分页查询流程表单设计
     *
     * @param request 请求入参
     * @return 分页数据
     */
    PageResult<PageProcessFormResponse> pageProcessForm(FindProcessFormPageRequest request);

}
