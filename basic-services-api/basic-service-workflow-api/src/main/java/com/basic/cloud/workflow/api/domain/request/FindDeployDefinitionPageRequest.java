package com.basic.cloud.workflow.api.domain.request;

import com.basic.framework.core.domain.BasicPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部署后的流程定义分页查询入参
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(title = "FindDeployDefinitionPageRequest", description = "部署后的流程定义分页查询入参")
public class FindDeployDefinitionPageRequest extends BasicPageable {

    @Schema(title = "模糊查询流程名称", description = "模糊查询流程名称")
    private String name;

    @Schema(title = "分类", description = "分类")
    private String category;

    @Schema(title = "流程定义key", description = "流程定义key")
    private String processKey;

    @Schema(title = "状态", description = "激活或挂起")
    private Boolean active;

}
