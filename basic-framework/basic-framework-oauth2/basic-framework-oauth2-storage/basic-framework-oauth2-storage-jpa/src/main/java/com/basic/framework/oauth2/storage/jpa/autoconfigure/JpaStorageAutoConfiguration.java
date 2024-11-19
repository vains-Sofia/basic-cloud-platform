package com.basic.framework.oauth2.storage.jpa.autoconfigure;

import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2ApplicationRepository;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2AuthorizationConsentRepository;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2AuthorizationRepository;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2ScopeRepository;
import com.basic.framework.oauth2.storage.jpa.service.JpaBasicApplicationService;
import com.basic.framework.oauth2.storage.jpa.service.JpaBasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.jpa.service.JpaBasicAuthorizationService;
import com.basic.framework.oauth2.storage.jpa.service.JpaOAuth2ScopeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自动注入Mybatis Plus的存储实现
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
@EntityScan("com.basic.framework.oauth2.storage.jpa.domain")
@EnableJpaRepositories(basePackages = "com.basic.framework.oauth2.storage.jpa.repository")
public class JpaStorageAutoConfiguration {

    private final OAuth2ScopeRepository scopeRepository;

    private final OAuth2ApplicationRepository applicationRepository;

    private final OAuth2AuthorizationRepository authorizationRepository;

    private final OAuth2AuthorizationConsentRepository authorizationConsentRepository;

    @PostConstruct
    public void postConstruct() {
        log.debug("Initializing the storage module for OAuth2 Server using Jpa implementation.");
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicApplicationService basicApplicationService(PasswordEncoder passwordEncoder) {
        return new JpaBasicApplicationService(passwordEncoder, applicationRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicAuthorizationService basicAuthorizationService() {
        return new JpaBasicAuthorizationService(authorizationRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicAuthorizationConsentService basicAuthorizationConsentService() {
        return new JpaBasicAuthorizationConsentService(authorizationConsentRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public JpaOAuth2ScopeService jpaOAuth2ScopeService() {
        return new JpaOAuth2ScopeService(scopeRepository);
    }
}
