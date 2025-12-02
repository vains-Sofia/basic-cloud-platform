package com.basic.cloud.workflow.api.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 流程定义状态枚举
 * 0=草稿，1=已发布，2=已禁用
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum DefinitionStatusEnum implements BasicEnum<Integer, DefinitionStatusEnum> {

    /**
     * 0-草稿
     */
    DRAFT(0, "草稿"),

    /**
     * 1-发布
     */
    PUBLISH(1, "发布"),

    /**
     * 2-禁用
     */
    DISABLED(2, "禁用");

    /**
     * 流程定义状态
     */
    private final Integer status;

    /**
     * 流程定义状态描述
     */
    private final String description;


    @Override
    public Integer getValue() {
        return this.status;
    }
}
