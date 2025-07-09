package com.basic.framework.oauth2.storage.autoconfigure;

import com.basic.cloud.system.api.SysPermissionClient;
import com.basic.framework.oauth2.core.domain.security.QrCodeStatus;
import com.basic.framework.oauth2.core.domain.security.ScopePermissionModel;
import com.basic.framework.oauth2.storage.repository.*;
import com.basic.framework.oauth2.storage.service.*;
import com.basic.framework.oauth2.storage.service.impl.*;
import com.basic.framework.oauth2.storage.sse.SseEmitterManager;
import com.basic.framework.oauth2.storage.storage.StorageAuthorizationConsentService;
import com.basic.framework.oauth2.storage.storage.StorageAuthorizationService;
import com.basic.framework.oauth2.storage.storage.StorageRegisteredClientRepository;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

import java.util.List;

/**
 * 核心service自动注入类
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
@Import(SseEmitterManager.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@EntityScan("com.basic.framework.oauth2.storage.domain.entity")
@EnableJpaRepositories(basePackages = "com.basic.framework.oauth2.storage.repository")
public class StorageAutoConfiguration {

    private final OAuth2ScopeRepository scopeRepository;

    private final RedisOperator<Long> redisHashOperator;

    private final SysPermissionClient sysPermissionClient;

    private final OAuth2ApplicationRepository applicationRepository;

    private final OAuth2AuthorizationRepository authorizationRepository;

    private final RedisOperator<List<ScopePermissionModel>> redisOperator;

    private final OAuth2ScopePermissionRepository scopePermissionRepository;

    private final OAuth2AuthorizationConsentRepository authorizationConsentRepository;

    @PostConstruct
    public void postConstruct() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing the storage module for OAuth2 Server using Jpa implementation.");
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicApplicationService basicApplicationService(PasswordEncoder passwordEncoder) {
        return new BasicApplicationServiceImpl(passwordEncoder, applicationRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicAuthorizationService basicAuthorizationService(SessionRegistry sessionRegistry,
                                                               SessionRepository<? extends Session> sessionRepository) {
        return new BasicAuthorizationServiceImpl(sessionRegistry, redisHashOperator, applicationRepository, authorizationRepository, sessionRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicAuthorizationConsentService basicAuthorizationConsentService() {
        return new BasicAuthorizationConsentServiceImpl(authorizationConsentRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2ScopeService jpaOAuth2ScopeService() {
        return new OAuth2ScopeServiceImpl(scopeRepository, sysPermissionClient, redisOperator, scopePermissionRepository);
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(
            BasicApplicationService applicationService) {
        return new StorageRegisteredClientRepository(applicationService);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(
            BasicAuthorizationService basicAuthorizationService, RegisteredClientRepository registeredClientRepository) {
        return new StorageAuthorizationService(basicAuthorizationService, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(
            BasicAuthorizationConsentService basicAuthorizationConsentService, RegisteredClientRepository registeredClientRepository) {
        return new StorageAuthorizationConsentService(basicAuthorizationConsentService, registeredClientRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionRegistry setSessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public QrCodeLoginService qrCodeLoginService(SseEmitterManager sseEmitterManager,
                                                 RedisTemplate<String, ?> redisTemplate,
                                                 RedisOperator<String> stringRedisOperator,
                                                 RedisOperator<QrCodeStatus> redisOperator) {
        return new QrCodeLoginServiceImpl(sseEmitterManager, redisTemplate, stringRedisOperator, redisOperator);
    }

}
