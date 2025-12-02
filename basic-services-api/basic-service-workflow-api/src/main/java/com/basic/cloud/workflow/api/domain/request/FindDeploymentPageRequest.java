package com.basic.cloud.workflow.api.domain.request;

import com.basic.framework.core.domain.BasicPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询流程部署入参
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(title = "FindDeploymentPageRequest", description = "分页查询流程部署入参")
public class FindDeploymentPageRequest extends BasicPageable {

    @Schema(title = "模糊查询流程名称", description = "模糊查询流程名称")
    private String name;

    @Schema(title = "分类", description = "分类")
    private String category;

    @Schema(title = "流程定义key", description = "流程定义key")
    private String processDefinitionKey;

}
