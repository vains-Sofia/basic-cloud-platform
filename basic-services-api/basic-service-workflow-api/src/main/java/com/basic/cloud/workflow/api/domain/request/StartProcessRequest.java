package com.basic.cloud.workflow.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 发起流程请求入参
 *
 * @author vains
 */
@Data
@Schema(title = "StartProcessRequest", description = "发起流程请求入参")
public class StartProcessRequest implements Serializable {

    /**
     * 流程定义key
     */
    @NotBlank(message = "流程定义key不能为空")
    @Schema(title = "流程定义key", description = "流程定义key")
    private String processDefinitionKey;

    /**
     * 业务唯一id
     */
    @Schema(title = "业务唯一ID", description = "业务唯一ID")
    private String businessKey;

    /**
     * 流程变量
     */
    @Schema(title = "流程变量", description = "流程变量")
    private Map<String, Object> variables;

}
