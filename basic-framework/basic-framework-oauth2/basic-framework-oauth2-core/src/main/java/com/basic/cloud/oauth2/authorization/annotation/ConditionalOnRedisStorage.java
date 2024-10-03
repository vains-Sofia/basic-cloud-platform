package com.basic.cloud.oauth2.authorization.annotation;

import com.basic.cloud.oauth2.authorization.condition.RedisStorageCondition;
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
