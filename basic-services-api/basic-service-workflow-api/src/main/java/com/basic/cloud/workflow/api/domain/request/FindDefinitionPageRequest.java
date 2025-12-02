package com.basic.cloud.workflow.api.domain.request;

import com.basic.cloud.workflow.api.enums.DefinitionStatusEnum;
import com.basic.framework.core.domain.BasicPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询流程定义入参
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(title = "FindProcessDefinitionPageRequest", description = "分页查询流程定义入参")
public class FindDefinitionPageRequest extends BasicPageable {

    @Schema(title = "模糊查询流程名称", description = "模糊查询流程名称")
    private String name;

    @Schema(title = "分类", description = "分类")
    private String category;

    @Schema(title = "状态", description = "状态")
    private DefinitionStatusEnum status;

}
