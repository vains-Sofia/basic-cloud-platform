package com.basic.cloud.workflow.controller;

import com.basic.cloud.workflow.api.ProcessDeploymentClient;
import com.basic.cloud.workflow.api.domain.request.FindDeploymentPageRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessDeployResponse;
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
    public Result<PageResult<PageProcessDeployResponse>> findDeploymentPage(FindDeploymentPageRequest request) {
        PageResult<PageProcessDeployResponse> resultPage = processDeploymentService.findDeploymentPage(request);
        return Result.success(resultPage);
    }

    @Override
    public Result<String> undeploy(String deploymentId, Boolean cascade) {
        processDeploymentService.undeploy(deploymentId, cascade);
        return Result.success();
    }
}
