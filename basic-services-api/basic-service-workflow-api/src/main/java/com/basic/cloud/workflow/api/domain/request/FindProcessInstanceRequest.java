package com.basic.cloud.workflow.api.domain.request;

import com.basic.framework.core.domain.BasicPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询流程实例入参
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(title = "FindProcessInstanceRequest", description = "查询流程实例入参")
public class FindProcessInstanceRequest extends BasicPageable {

    @Schema(title = "流程定义 key", description = "流程定义 key")
    private String processDefinitionKey;

}
