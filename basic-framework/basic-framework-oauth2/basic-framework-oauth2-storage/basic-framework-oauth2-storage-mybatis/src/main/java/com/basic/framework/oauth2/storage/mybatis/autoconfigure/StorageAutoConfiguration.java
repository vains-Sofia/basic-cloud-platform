package com.basic.framework.oauth2.storage.mybatis.autoconfigure;

import com.basic.framework.oauth2.core.annotation.ConditionalOnMybatisPlusStorage;
import com.basic.framework.oauth2.storage.core.service.OAuth2ApplicationService;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2ApplicationMapper;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2AuthorizationConsentMapper;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2AuthorizationMapper;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisOAuth2ApplicationService;
import com.basic.framework.oauth2.storage.mybatis.storage.MybatisOAuth2AuthorizationConsentService;
import com.basic.framework.oauth2.storage.mybatis.storage.MybatisOAuth2AuthorizationService;
import com.basic.framework.oauth2.storage.mybatis.storage.MybatisRegisteredClientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
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
    public OAuth2ApplicationService oAuth2ApplicationService(MybatisOAuth2ApplicationMapper mybatisOAuth2ApplicationMapper) {
        return new MybatisOAuth2ApplicationService(mybatisOAuth2ApplicationMapper);
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(OAuth2ApplicationService applicationService) {
        return new MybatisRegisteredClientRepository(applicationService);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(MybatisOAuth2AuthorizationMapper mybatisOAuth2AuthorizationMapper, RegisteredClientRepository registeredClientRepository) {
        return new MybatisOAuth2AuthorizationService(mybatisOAuth2AuthorizationMapper, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(MybatisOAuth2AuthorizationConsentMapper mybatisOAuth2AuthorizationConsentMapper, RegisteredClientRepository registeredClientRepository) {
        return new MybatisOAuth2AuthorizationConsentService(mybatisOAuth2AuthorizationConsentMapper, registeredClientRepository);
    }

}
