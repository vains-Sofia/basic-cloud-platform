package com.basic.framework.data.jpa.configuration;

import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import jakarta.annotation.Nonnull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

/**
 * webflux审计信息配置类
 *
 * @author vains
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(ReactiveAuditorAware.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveAuditorAwareConfiguration implements ReactiveAuditorAware<Long> {

    @Nonnull
    @Override
    public Mono<Long> getCurrentAuditor() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(AuthenticatedUser.class::isInstance)
                .cast(DefaultAuthenticatedUser.class)
                .map(AuthenticatedUser::getId)
                .defaultIfEmpty(0L);
    }
}
