package com.basic.framework.oauth2.core.annotation;

import com.basic.framework.oauth2.core.condition.MybatisPlusStorageCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 使用MybatisPlus实现的核心services的条件注解
 *
 * @author vains
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(MybatisPlusStorageCondition.class)
public @interface ConditionalOnMybatisPlusStorage {
}
