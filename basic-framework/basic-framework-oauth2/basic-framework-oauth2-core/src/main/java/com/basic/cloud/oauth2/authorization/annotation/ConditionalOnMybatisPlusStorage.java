package com.basic.cloud.oauth2.authorization.annotation;

import com.basic.cloud.oauth2.authorization.condition.MybatisPlusStorageCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 使用MybatisPlus实现的核心services的条件注解
 *
 * @author YuJx
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(MybatisPlusStorageCondition.class)
public @interface ConditionalOnMybatisPlusStorage {
}
