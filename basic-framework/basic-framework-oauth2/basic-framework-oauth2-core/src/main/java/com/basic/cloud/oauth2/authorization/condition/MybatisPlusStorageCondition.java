package com.basic.cloud.oauth2.authorization.condition;

import com.basic.cloud.oauth2.authorization.constant.AuthorizeConstants;
import com.basic.cloud.oauth2.authorization.enums.CoreServiceStorageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.Nullable;

/**
 * 基于MybatisPlus的存储实现条件
 *
 * @author vains
 */
@Slf4j
public class MybatisPlusStorageCondition implements Condition {

    @Override
    @Nullable
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        CoreServiceStorageEnum property = context.getEnvironment().getProperty(AuthorizeConstants.CORE_SERVICE_STORAGE, CoreServiceStorageEnum.class, CoreServiceStorageEnum.MYBATIS_PLUS);
        boolean result = CoreServiceStorageEnum.MYBATIS_PLUS.getValue().equals(property.getValue());
        log.debug("Condition [MybatisPlus] value is {}", result);
        return result;
    }
}
