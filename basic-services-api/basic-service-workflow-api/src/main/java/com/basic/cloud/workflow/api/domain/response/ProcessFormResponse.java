package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 流程表单设计详情
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessFormResponse extends PageProcessFormResponse implements Serializable {

    /**
     * 表单设计器生成、使用的完整JSON配置
     */
    @NotBlank(message = "表单内容不能为空")
    @Schema(title = "表单内容", description = "表单设计器生成、使用的完整JSON配置")
    private String formContent;

}
