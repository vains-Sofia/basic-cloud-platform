package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部署后的流程定义响应bean
 *
 * @author vains
 */
@Data
@Schema(title = "PageDeployDefinitionResponse", description = "部署后的流程定义响应bean")
public class PageDeployDefinitionResponse implements Serializable {

    @Schema(title = "流程定义 ID", description = "流程定义 ID")
    protected String id;

    @Schema(title = "流程名称", description = "流程名称")
    protected String name;

    @Schema(title = "流程key", description = "流程key")
    protected String key;

    @Schema(title = "流程分类", description = "流程分类")
    protected String category;

    @Schema(title = "状态", description = "挂起 激活")
    private Boolean suspended;

    @Schema(title = "流程版本", description = "流程版本")
    private Integer version;

    @Schema(title = "部署 ID", description = "流程实例部署id")
    private String deploymentId;

    @Schema(title = "部署时间", description = "部署时间")
    private Date deploymentTime;

}
