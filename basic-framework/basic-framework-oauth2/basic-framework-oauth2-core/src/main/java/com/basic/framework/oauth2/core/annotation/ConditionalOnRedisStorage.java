package com.basic.framework.oauth2.core.annotation;

import com.basic.framework.oauth2.core.condition.RedisStorageCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 使用Redis实现的核心services的条件注解
 *
 * @author vains
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Conditional(RedisStorageCondition.class)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ConditionalOnRedisStorage {
}
