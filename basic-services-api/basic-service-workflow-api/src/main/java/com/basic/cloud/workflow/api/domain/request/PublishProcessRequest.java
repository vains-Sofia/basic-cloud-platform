package com.basic.cloud.workflow.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 发布流程定义入参
 *
 * @author vains
 */
@Data
@Schema(title = "PublishProcessRequest", description = "发布流程定义入参")
public class PublishProcessRequest {

    /**
     * 发布说明
     */
    @Schema(title = "发布说明", description = "发布说明")
    private String remark;

}
