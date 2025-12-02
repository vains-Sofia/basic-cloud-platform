package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发布流程定义响应
 *
 * @author vains
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "PublishProcessResponse", description = "发布流程定义响应")
public class PublishProcessResponse {

    /**
     * 流程定义key
     */
    @NotBlank(message = "流程定义key不能为空")
    @Schema(title = "流程定义key", description = "流程定义key")
    private String processKey;

    /**
     * 模型版本
     */
    @Schema(title = "模型版本", description = "模型版本，默认使用最新版本")
    private Integer version;

}
