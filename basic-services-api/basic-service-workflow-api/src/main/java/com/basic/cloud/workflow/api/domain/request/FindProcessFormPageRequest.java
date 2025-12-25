package com.basic.cloud.workflow.api.domain.request;

import com.basic.framework.core.domain.BasicPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询流程表单设计入参
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FindProcessFormPageRequest extends BasicPageable {

    /**
     * 表单标题
     */
    @Schema(title = "表单标题", description = "表单标题")
    private String title;

    /**
     * 表单描述
     */
    @Schema(title = "表单描述", description = "表单描述")
    private String description;

}
