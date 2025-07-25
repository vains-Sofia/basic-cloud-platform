package com.basic.framework.oauth2.core.condition;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.enums.CoreServiceStorageEnum;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 基于内存的存储实现条件
 *
 * @author vains
 */
@Slf4j
public class InMemoryStorageCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, @Nullable AnnotatedTypeMetadata metadata) {
        CoreServiceStorageEnum property = context.getEnvironment().getProperty(AuthorizeConstants.CORE_SERVICE_STORAGE, CoreServiceStorageEnum.class);
        boolean result = property != null && CoreServiceStorageEnum.IN_MEMORY.getValue().equals(property.getValue());
        log.debug("Condition [InMemory] value is {}", result);
        return result;
    }
}
