package com.basic.framework.data.jpa.configuration;

import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import jakarta.annotation.Nonnull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 审计信息配置类
 *
 * @author vains
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(AuditorAware.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class AuditorAwareConfiguration implements AuditorAware<Long> {

    @Nonnull
    @Override
    public Optional<Long> getCurrentAuditor() {
        // 获取当前登录用户
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        // 获取登录用户的id
        return Optional.ofNullable(authenticatedUser)
                .map(AuthenticatedUser::getId);
    }

}
