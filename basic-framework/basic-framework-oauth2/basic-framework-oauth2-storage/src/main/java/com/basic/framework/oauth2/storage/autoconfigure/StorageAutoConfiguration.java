package com.basic.framework.oauth2.storage.autoconfigure;

import com.basic.framework.oauth2.core.domain.security.ScopePermissionModel;
import com.basic.framework.oauth2.storage.repository.*;
import com.basic.framework.oauth2.storage.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.service.BasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.service.OAuth2ScopeService;
import com.basic.framework.oauth2.storage.service.impl.BasicApplicationServiceImpl;
import com.basic.framework.oauth2.storage.service.impl.BasicAuthorizationConsentServiceImpl;
import com.basic.framework.oauth2.storage.service.impl.BasicAuthorizationServiceImpl;
import com.basic.framework.oauth2.storage.service.impl.OAuth2ScopeServiceImpl;
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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.util.List;

/**
 * 核心service自动注入类
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@EntityScan("com.basic.framework.oauth2.storage.domain.entity")
@EnableJpaRepositories(basePackages = "com.basic.framework.oauth2.storage.repository")
public class StorageAutoConfiguration {

    private final OAuth2ScopeRepository scopeRepository;

    private final OAuth2ApplicationRepository applicationRepository;

    private final OAuth2AuthorizationRepository authorizationRepository;

    private final RedisOperator<List<ScopePermissionModel>> redisOperator;

    private final OAuth2ScopePermissionRepository scopePermissionRepository;

    private final OAuth2AuthorizationConsentRepository authorizationConsentRepository;

    @PostConstruct
    public void postConstruct() {
        log.debug("Initializing the storage module for OAuth2 Server using Jpa implementation.");
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicApplicationService basicApplicationService(PasswordEncoder passwordEncoder) {
        return new BasicApplicationServiceImpl(passwordEncoder, applicationRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicAuthorizationService basicAuthorizationService() {
        return new BasicAuthorizationServiceImpl(authorizationRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicAuthorizationConsentService basicAuthorizationConsentService() {
        return new BasicAuthorizationConsentServiceImpl(authorizationConsentRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2ScopeService jpaOAuth2ScopeService() {
        return new OAuth2ScopeServiceImpl(scopeRepository, redisOperator, scopePermissionRepository);
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

}
