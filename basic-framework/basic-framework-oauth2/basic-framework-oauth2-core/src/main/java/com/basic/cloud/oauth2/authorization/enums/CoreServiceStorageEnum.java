package com.basic.cloud.oauth2.authorization.enums;

import com.basic.cloud.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 核心服务存储位置枚举
 *
 * @author YuJx
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
    IN_MEMORY(2, "InMemory");

    /**
     * 存储类型值
     */
    private final Integer value;

    /**
     * 存储类型名
     */
    private final String description;

}
