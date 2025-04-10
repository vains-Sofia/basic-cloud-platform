package com.basic.cloud.system.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 权限类型枚举
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum PermissionTypeEnum implements BasicEnum<Integer, PermissionTypeEnum> {

    /**;
     * 菜单
     */
    MENU(0, "菜单"),

    /**
     * 接口
     */
    REST(1, "接口"),

    /**
     * 其它
     */
    OTHER(2, "其它");

    /**
     * 权限类型值
     */
    private final Integer type;

    /**
     * 权限类型名
     */
    private final String name;

    @Override
    public Integer getValue() {
        return this.type;
    }
}
