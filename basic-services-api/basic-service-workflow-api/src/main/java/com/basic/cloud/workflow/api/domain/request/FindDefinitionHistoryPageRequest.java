package com.basic.cloud.workflow.api.domain.request;

import com.basic.framework.core.domain.BasicPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询流程定义历史版本入参
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FindDefinitionHistoryPageRequest extends BasicPageable {

    @NotBlank(message = "流程定义key不能为空")
    @Schema(title = "流程定义key", description = "流程定义key")
    private String processKey;

}
