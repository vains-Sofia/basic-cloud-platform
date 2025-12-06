package com.basic.cloud.workflow.api.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 流程定义状态枚举
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum SuspensionStateEnum implements BasicEnum<Integer, SuspensionStateEnum> {

    /**
     * 1：激活
     */
    ACTIVE(1, "激活"),

    /**
     * 2：挂起
     */
    SUSPENDED(2, "挂起");

    /**
     * 流程定义状态值
     */
    private final Integer state;

    /**
     * 流程定义状态描述
     */
    private final String description;

    @Override
    public Integer getValue() {
        return this.state;
    }
}
