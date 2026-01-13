package com.basic.cloud.workflow.api.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 取消流程实例入参
 *
 * @author vains
 */
@Data
@Schema(title = "CancelProcessInstanceRequest", description = "取消流程实例入参")
public class CancelProcessInstanceRequest implements Serializable {

    @NotBlank(message = "流程实例 ID 不能为空")
    @Schema(title = "流程实例 ID", description = "流程实例 ID")
    private String processInstanceId;

    @Schema(title = "取消流程实例原因", description = "取消流程实例原因")
    private String reason;

}
