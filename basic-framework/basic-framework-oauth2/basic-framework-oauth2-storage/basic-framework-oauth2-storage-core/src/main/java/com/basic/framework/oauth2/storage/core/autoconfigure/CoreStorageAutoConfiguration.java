package com.basic.framework.oauth2.storage.core.autoconfigure;

import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.core.storage.StorageAuthorizationConsentService;
import com.basic.framework.oauth2.storage.core.storage.StorageAuthorizationService;
import com.basic.framework.oauth2.storage.core.storage.StorageRegisteredClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * 核心service自动注入类
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class CoreStorageAutoConfiguration {

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
