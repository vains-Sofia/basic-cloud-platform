package com.basic.cloud.workflow.service;

import com.basic.cloud.workflow.api.domain.request.FindDeploymentPageRequest;
import com.basic.cloud.workflow.api.domain.response.ProcessDeploymentResponse;
import com.basic.framework.core.domain.PageResult;

/**
 * 流程部署 Service 接口
 *
 * @author vains
 */
public interface ProcessDeploymentService {

    /**
     * 分页查询流程部署数据
     *
     * @param request 分页查询入参
     * @return 流程部署数据
     */
    PageResult<ProcessDeploymentResponse> findDeploymentPage(FindDeploymentPageRequest request);
}
