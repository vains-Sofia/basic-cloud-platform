package com.basic.cloud.workflow.api.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 重定向信息(前端使用)
 * TASK_FORM - 任务表单,
 * TASK_LIST - 多任务,
 * NONE - 不跳转
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum RedirectTypeEnum implements BasicEnum<String, RedirectTypeEnum> {

    /**
     * TASK_FORM - 跳转填写表单
     */
    TASK_FORM("TASK_FORM", "跳转填写表单"),

    /**
     * TASK_LIST - 多任务
     */
    TASK_LIST("TASK_LIST", "跳到待办列表"),

    /**
     * NONE - 不跳转
     */
    NONE("NONE", "什么都不做");

    /**
     * 重定向类型值
     */
    private final String type;

    /**
     * 重定向类型描述
     */
    private final String desc;

    @Override
    public String getValue() {
        return this.type;
    }

}
