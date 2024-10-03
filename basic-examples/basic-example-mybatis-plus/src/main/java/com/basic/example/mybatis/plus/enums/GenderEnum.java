package com.basic.example.mybatis.plus.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 性别注解
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum GenderEnum implements BasicEnum<Integer, GenderEnum> {

    /**
     * 1-男
     */
    MAN(1, "男"),

    /**
     * 2-女
     */
    WOMAN(2, "女"),

    /**
     * 3-其它
     */
    OTHER(3, "其它");

    /**
     * 性别代码
     */
    private final Integer gender;

    /**
     * 性别名
     */
    private final String name;

    @Override
    public Integer getValue() {
        return this.gender;
    }

}
