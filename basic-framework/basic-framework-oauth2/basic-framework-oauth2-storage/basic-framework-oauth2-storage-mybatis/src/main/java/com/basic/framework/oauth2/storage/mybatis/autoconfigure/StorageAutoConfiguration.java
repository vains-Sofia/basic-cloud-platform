package com.basic.framework.oauth2.storage.mybatis.autoconfigure;

import com.basic.framework.oauth2.core.annotation.ConditionalOnMybatisPlusStorage;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2ApplicationMapper;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2AuthorizationConsentMapper;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2AuthorizationMapper;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisBasicApplicationService;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisBasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisBasicAuthorizationService;
import com.basic.framework.oauth2.storage.mybatis.storage.MybatisOAuth2AuthorizationConsentService;
import com.basic.framework.oauth2.storage.mybatis.storage.MybatisOAuth2AuthorizationService;
import com.basic.framework.oauth2.storage.mybatis.storage.MybatisRegisteredClientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * 自动注入Mybatis Plus的存储实现
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnMybatisPlusStorage
@MapperScan("com.basic.framework.oauth2.storage.mybatis.mapper")
public class StorageAutoConfiguration {

    @PostConstruct
    public void postConstruct() {
        log.debug("Initializing the storage module for OAuth2 Server using MyBatis Plus implementation.");
    }

    @Bean
    public BasicApplicationService basicApplicationService(
            PasswordEncoder passwordEncoder,
            MybatisOAuth2ApplicationMapper mybatisOAuth2ApplicationMapper) {
        return new MybatisBasicApplicationService(passwordEncoder, mybatisOAuth2ApplicationMapper);
    }

    @Bean
    public BasicAuthorizationService basicAuthorizationService(
            MybatisOAuth2AuthorizationMapper oAuth2AuthorizationMapper) {
        return new MybatisBasicAuthorizationService(oAuth2AuthorizationMapper);
    }

    @Bean
    public BasicAuthorizationConsentService basicAuthorizationConsentService(
            MybatisOAuth2AuthorizationConsentMapper oAuth2AuthorizationConsentMapper) {
        return new MybatisBasicAuthorizationConsentService(oAuth2AuthorizationConsentMapper);
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(
            BasicApplicationService applicationService) {
        return new MybatisRegisteredClientRepository(applicationService);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(
            BasicAuthorizationService basicAuthorizationService, RegisteredClientRepository registeredClientRepository) {
        return new MybatisOAuth2AuthorizationService(basicAuthorizationService, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(
            BasicAuthorizationConsentService basicAuthorizationConsentService, RegisteredClientRepository registeredClientRepository) {
        return new MybatisOAuth2AuthorizationConsentService(basicAuthorizationConsentService, registeredClientRepository);
    }

}
