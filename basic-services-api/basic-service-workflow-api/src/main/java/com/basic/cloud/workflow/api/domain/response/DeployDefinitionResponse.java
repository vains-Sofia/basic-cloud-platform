package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 部署的流程定义详情
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(title = "DeployDefinitionResponse", description = "部署后的流程定义详情")
public class DeployDefinitionResponse extends PageDeployDefinitionResponse {

    @Schema(title = "资源名称", description = "xml资源名称")
    private String resourceName;

    @Schema(title = "图片资源名称", description = "图片资源名称")
    private String diagramResourceName;

    @Schema(title = "启动用户", description = "启动用户")
    private List<String> startUsers;

    @Schema(title = "启动用户组", description = "启动用户组")
    private List<String> startGroups;

    @Schema(title = "启动表单key", description = "启动表单key")
    private String startFormKey;

    @Schema(title = "任务表单列表", description = "任务表单列表")
    private List<TaskFormResponse> taskForms;

}
