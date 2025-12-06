package com.basic.cloud.workflow.api;

import com.basic.cloud.workflow.api.domain.request.FindDeploymentPageRequest;
import com.basic.cloud.workflow.api.domain.response.PageProcessDeployResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * 流程部署相关接口
 *
 * @author vains
 */
@RequestMapping("/process-deployment")
@Tag(name = "流程部署相关接口", description = "流程部署相关接口")
@FeignClient(name = FeignConstants.WORKFLOW_APPLICATION, path = FeignConstants.WORKFLOW_CONTEXT_PATH, contextId = "ProcessDeploymentClient")
public interface ProcessDeploymentClient {

    @GetMapping("/page")
    @Operation(summary = "分页查询流程部署数据", description = "分页查询流程部署数据")
    Result<PageResult<PageProcessDeployResponse>> findDeploymentPage(@Valid @SpringQueryMap FindDeploymentPageRequest request);

    @DeleteMapping("/{deploymentId}/undeploy")
    @Operation(summary = "删除部署的流程", description = "删除部署的流程，默认级联删除")
    @Parameters({
            @Parameter(name = "cascade", description = "级联删除数据"),
            @Parameter(name = "deploymentId", description = "流程部署 ID", required = true)
    })
    Result<String> undeploy(@PathVariable String deploymentId,
                            @RequestParam(required = false) Boolean cascade);

}
