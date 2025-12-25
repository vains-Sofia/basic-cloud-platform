package com.basic.cloud.workflow.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表单设计配置表
 *
 * @author vains
 */
@TableName(value = "process_form")
@Data
public class ProcessForm {
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

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 修改人
     */
    private Long updateBy;

    /**
     * 创建人名称
     */
    private String createName;

    /**
     * 修改人名称
     */
    private String updateName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}