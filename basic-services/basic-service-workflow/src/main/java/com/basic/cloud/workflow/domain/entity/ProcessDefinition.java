package com.basic.cloud.workflow.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.cloud.workflow.api.enums.DefinitionStatusEnum;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程定义表 process_definition
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "process_definition", autoResultMap = true)
public class ProcessDefinition extends BasicEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 模型 Key（流程定义 key）
     */
    private String processKey;

    /**
     * 模型名称
     */
    private String processName;

    /**
     * BPMN XML 内容
     */
    private String processXml;

    /**
     * 如果前端使用 bpmn-js JSON，可额外存储
     */
    private String processJson;

    /**
     * 分类（请假、采购等）
     */
    private String category;

    /**
     * 模型版本
     */
    private Integer version;

    /**
     * 状态：0=草稿，1=已发布，2=已禁用
     */
    private DefinitionStatusEnum status;

    /**
     * 说明
     */
    private String remark;

    /**
     * 逻辑删除
     */
    private Boolean deleted;
}