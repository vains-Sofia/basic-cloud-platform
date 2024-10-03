package com.basic.framework.oauth2.core.annotation;

import com.basic.framework.oauth2.core.condition.InMemoryStorageCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 使用基于内存实现的核心services的条件注解
 *
 * @author vains
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Conditional(InMemoryStorageCondition.class)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ConditionalOnInMemoryStorage {
}
