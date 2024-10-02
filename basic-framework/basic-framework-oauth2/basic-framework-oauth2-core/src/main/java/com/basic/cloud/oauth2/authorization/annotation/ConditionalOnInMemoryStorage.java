package com.basic.cloud.oauth2.authorization.annotation;

import com.basic.cloud.oauth2.authorization.condition.InMemoryStorageCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 使用基于内存实现的核心services的条件注解
 *
 * @author YuJx
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Conditional(InMemoryStorageCondition.class)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ConditionalOnInMemoryStorage {
}
