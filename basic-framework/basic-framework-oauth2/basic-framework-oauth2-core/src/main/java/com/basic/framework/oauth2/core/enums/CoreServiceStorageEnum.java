package com.basic.framework.oauth2.core.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 核心服务存储位置枚举
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum CoreServiceStorageEnum implements BasicEnum<Integer, CoreServiceStorageEnum> {

    /**
     * TODO: 基于Redis的实现
     */
    REDIS(0, "Redis"),

    /**
     * 基于DB的Mybatis Plus实现
      */
    MYBATIS_PLUS(1, "MybatisPlus"),

    /**
     * 基于内存的实现
     */
    IN_MEMORY(2, "InMemory"),

    /**
     * 基于DB的Jpa实现
     */
    JPA(3, "Jpa");

    /**
     * 存储类型值
     */
    private final Integer value;

    /**
     * 存储类型名
     */
    private final String description;

}
