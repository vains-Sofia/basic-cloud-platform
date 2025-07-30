package com.basic.cloud.system.api.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 扫描状态枚举
 * 扫描状态枚举定义了系统中资源扫描的不同状态。
 * - NEW: 新发现的资源。
 * - EXISTING: 已存在的资源。
 * - MISSING: 缺少注释的资源。
 * - IGNORE: 忽略的资源。
 *
 * @author vains
 */
@Getter
@AllArgsConstructor
public enum ScanStatusEnum implements BasicEnum<Integer, ScanStatusEnum> {

    /**
     * 1-新发现
     */
    NEW(1, "新发现"),

    /**
     * 2-已存在
     */
    EXISTING(2, "已存在"),

    /**
     * 3-缺少注释
     */
    MISSING(3, "缺少注释"),

    /**
     * 4-忽略
     */
    IGNORE(4, "忽略");

    /**
     * 枚举值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String description;

}