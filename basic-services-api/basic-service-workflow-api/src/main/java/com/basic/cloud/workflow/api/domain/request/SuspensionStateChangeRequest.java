package com.basic.cloud.workflow.api.domain.request;

import com.basic.cloud.workflow.api.enums.SuspensionStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 改变流程定义状态入参
 *
 * @author vains
 */
@Data
@Schema(title = "SuspensionStateChangeRequest", description = "改变流程定义状态入参")
public class SuspensionStateChangeRequest implements Serializable {

    @Schema(title = "流程定义状态")
    @NotNull(message = "流程定义状态不能为空")
    private SuspensionStateEnum state;

    @Schema(title = "是否包含关联的流程实例", description = "是否包含关联的流程实例")
    private Boolean includeProcessInstances;

}
