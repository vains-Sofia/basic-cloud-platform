package com.basic.framework.oauth2.storage.jpa.autoconfigure;

import com.basic.framework.oauth2.core.annotation.ConditionalOnJpaStorage;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2ApplicationRepository;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2AuthorizationConsentRepository;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2AuthorizationRepository;
import com.basic.framework.oauth2.storage.jpa.service.JpaBasicApplicationService;
import com.basic.framework.oauth2.storage.jpa.service.JpaBasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.jpa.service.JpaBasicAuthorizationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@ConditionalOnJpaStorage
@EntityScan("com.basic.framework.oauth2.storage.jpa.domain")
@EnableJpaRepositories(basePackages = "com.basic.framework.oauth2.storage.jpa.repository")
public class JpaStorageAutoConfiguration {

    private final OAuth2ApplicationRepository applicationRepository;

    private final OAuth2AuthorizationRepository authorizationRepository;

    private final OAuth2AuthorizationConsentRepository authorizationConsentRepository;

    @PostConstruct
    public void postConstruct() {
        log.debug("Initializing the storage module for OAuth2 Server using Jpa implementation.");
    }

    @Bean
    public BasicApplicationService basicApplicationService(PasswordEncoder passwordEncoder) {
        return new JpaBasicApplicationService(passwordEncoder, applicationRepository);
    }

    @Bean
    public BasicAuthorizationService basicAuthorizationService() {
        return new JpaBasicAuthorizationService(authorizationRepository);
    }

    @Bean
    public BasicAuthorizationConsentService basicAuthorizationConsentService() {
        return new JpaBasicAuthorizationConsentService(authorizationConsentRepository);
    }
}
