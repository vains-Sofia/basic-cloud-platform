package com.basic.cloud.workflow.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 流程表单分页数据
 *
 * @author vains
 */
@Data
@Schema(title = "PageProcessFormResponse", description = "流程表单分页数据")
public class PageProcessFormResponse implements Serializable {

    /**
     * 表单id，主键
     */
    @Schema(title = "表单id", description = "主键ID")
    private Long id;

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

    /**
     * 创建人名称
     */
    @Schema(title = "创建人名称")
    private String createName;

    /**
     * 修改人名称
     */
    @Schema(title = "修改人名称")
    private String updateName;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(title = "修改时间")
    private LocalDateTime updateTime;

}
