package com.basic.framework.oauth2.storage.mybatis.autoconfigure;

import com.basic.cloud.authorization.server.service.impl.MybatisOAuth2ScopeService;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2ApplicationMapper;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2AuthorizationConsentMapper;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2AuthorizationMapper;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2ScopeMapper;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisBasicApplicationService;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisBasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisBasicAuthorizationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自动注入Mybatis Plus的存储实现
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
@MapperScan("com.basic.framework.oauth2.storage.mybatis.mapper")
public class MybatisPlusStorageAutoConfiguration {

    @PostConstruct
    public void postConstruct() {
        log.debug("Initializing the storage module for OAuth2 Server using MyBatis Plus implementation.");
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicApplicationService basicApplicationService(
            PasswordEncoder passwordEncoder,
            MybatisOAuth2ApplicationMapper mybatisOAuth2ApplicationMapper) {
        return new MybatisBasicApplicationService(passwordEncoder, mybatisOAuth2ApplicationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicAuthorizationService basicAuthorizationService(
            MybatisOAuth2AuthorizationMapper oAuth2AuthorizationMapper) {
        return new MybatisBasicAuthorizationService(oAuth2AuthorizationMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicAuthorizationConsentService basicAuthorizationConsentService(
            MybatisOAuth2AuthorizationConsentMapper oAuth2AuthorizationConsentMapper) {
        return new MybatisBasicAuthorizationConsentService(oAuth2AuthorizationConsentMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisOAuth2ScopeService jpaOAuth2ScopeService(MybatisOAuth2ScopeMapper scopeMapper) {
        return new MybatisOAuth2ScopeService(scopeMapper);
    }
}
