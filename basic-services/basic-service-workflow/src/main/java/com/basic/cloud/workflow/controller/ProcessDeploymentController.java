package com.basic.cloud.workflow.controller;

import com.basic.cloud.workflow.api.ProcessDeploymentClient;
import com.basic.cloud.workflow.api.domain.request.FindDeploymentPageRequest;
import com.basic.cloud.workflow.api.domain.response.ProcessDeploymentResponse;
import com.basic.cloud.workflow.service.ProcessDeploymentService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程部署接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class ProcessDeploymentController implements ProcessDeploymentClient {

    private final ProcessDeploymentService processDeploymentService;

    @Override
    public Result<PageResult<ProcessDeploymentResponse>> findDeploymentPage(FindDeploymentPageRequest request) {
        PageResult<ProcessDeploymentResponse> resultPage = processDeploymentService.findDeploymentPage(request);
        return Result.success(resultPage);
    }
}
