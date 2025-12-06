package com.basic.cloud.workflow.service;

import com.basic.cloud.workflow.api.domain.request.FindDeploymentPageRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessDeployResponse;
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
    PageResult<PageProcessDeployResponse> findDeploymentPage(FindDeploymentPageRequest request);

    /**
     * 删除部署的流程
     *
     * @param deploymentId 流程部署 ID
     * @param cascade      是否级联删除
     */
    void undeploy(String deploymentId, Boolean cascade);
}
