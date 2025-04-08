package com.basic.framework.oauth2.storage.jpa.autoconfigure;

import com.basic.framework.oauth2.core.domain.security.ScopePermissionModel;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.jpa.repository.*;
import com.basic.framework.oauth2.storage.jpa.service.JpaBasicApplicationService;
import com.basic.framework.oauth2.storage.jpa.service.JpaBasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.jpa.service.JpaBasicAuthorizationService;
import com.basic.framework.oauth2.storage.jpa.service.JpaOAuth2ScopeService;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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
        return new JpaOAuth2ScopeService(scopeRepository, redisOperator, scopePermissionRepository);
    }

    /**
     * 初始化scope的权限至缓存中
     */
    @PostConstruct
    public void initScopePermissionCache() {
        // 查询所有数据并转换
        List<ScopePermissionModel> permissionModelList = this.scopePermissionRepository.findAll()
                .stream()
                .map(e -> {
                    ScopePermissionModel model = new ScopePermissionModel();
                    BeanUtils.copyProperties(e, model);
                    return model;
                }).toList();

        // 删除缓存
        redisOperator.delete(AuthorizeConstants.SCOPE_PERMISSION_KEY);
        // 刷新缓存
        redisOperator.set(AuthorizeConstants.SCOPE_PERMISSION_KEY, new ArrayList<>(permissionModelList));
    }
}
