package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程部署响应bean
 *
 * @author vains
 */
@Data
@Schema(title = "PageProcessDeployResponse", description = "流程部署响应bean")
public class PageProcessDeployResponse implements Serializable {

    @Schema(title = "部署 ID", description = "流程实例部署id")
    protected String id;

    @Schema(title = "流程实例名称", description = "流程实例名称")
    protected String name;

    @Schema(title = "流程实例分类", description = "流程实例分类")
    protected String category;

    @Schema(title = "流程实例key", description = "流程实例key")
    protected String processKey;

    @Schema(title = "部署时间", description = "部署时间")
    protected Date deploymentTime;

}
