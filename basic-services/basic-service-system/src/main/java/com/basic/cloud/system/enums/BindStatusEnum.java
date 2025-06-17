package com.basic.cloud.system.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 绑定状态枚举
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum BindStatusEnum implements BasicEnum<Integer, BindStatusEnum> {

    /**
     * 待确认
     */
    PENDING_CONFIRMATION(0, "待确认"),

    /**
     * 已绑定
     */
    BOUND(1, "已绑定");

    /**
     * 绑定状态
     */
    private final Integer value;

    /**
     * 绑定状态描述
     */
    private final String description;



}
