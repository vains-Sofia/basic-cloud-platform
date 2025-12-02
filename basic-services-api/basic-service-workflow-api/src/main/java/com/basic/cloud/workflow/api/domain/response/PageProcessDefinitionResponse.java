package com.basic.cloud.workflow.api.domain.response;

import com.basic.cloud.workflow.api.enums.DefinitionStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询流程定义响应
 *
 * @author vains
 */
@Data
public class PageProcessDefinitionResponse implements Serializable {

    /**
     * 主键id
     */
    @Schema(title = "主键id", description = "主键id")
    private Long id;

    /**
     * 流程定义key
     */
    @Schema(title = "流程定义key", description = "流程定义key")
    private String processKey;

    /**
     * 流程定义名称
     */
    @Schema(title = "流程定义名称", description = "流程定义名称")
    private String processName;

    /**
     * 分类（请假、采购等）
     */
    @Schema(title = "分类", description = "请假、采购等")
    private String category;

    /**
     * 模型版本
     */
    @Schema(title = "模型版本", description = "模型版本")
    private Integer version;

    /**
     * 状态：0=草稿，1=已发布，2=已禁用
     */
    @Schema(title = "状态", description = "状态")
    private DefinitionStatusEnum status;

    /**
     * 说明
     */
    @Schema(title = "流程说明", description = "流程说明")
    private String remark;

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
