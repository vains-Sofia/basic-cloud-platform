package com.basic.cloud.workflow.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.basic.framework.mybatis.plus.domain.BasicEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表单设计配置表
 *
 * @author vains
 */
@Data
@TableName(value = "process_form")
@EqualsAndHashCode(callSuper = true)
public class ProcessForm extends BasicEntity {
    /**
     * 表单ID，主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 表单标题
     */
    private String title;

    /**
     * 表单描述
     */
    private String description;

    /**
     * VForm3设计器生成/使用的完整JSON配置[citation:6]
     */
    private String formContent;
}