package com.basic.framework.oauth2.storage.mybatis.autoconfigure;

import com.basic.framework.oauth2.core.domain.security.ScopePermissionModel;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.mybatis.mapper.*;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisBasicApplicationService;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisBasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisBasicAuthorizationService;
import com.basic.framework.oauth2.storage.mybatis.service.MybatisOAuth2ScopeService;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
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
@MapperScan("com.basic.framework.oauth2.storage.mybatis.mapper")
public class MybatisPlusStorageAutoConfiguration {

    private final RedisOperator<List<ScopePermissionModel>> redisOperator;

    private final MybatisOAuth2ScopePermissionMapper scopePermissionMapper;

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
        return new MybatisOAuth2ScopeService(scopeMapper, redisOperator, scopePermissionMapper);
    }

    /**
     * 初始化scope的权限至缓存中
     */
    @PostConstruct
    public void initScopePermissionCache() {
        // 查询所有数据并转换
        List<ScopePermissionModel> permissionModelList = this.scopePermissionMapper.selectList(null)
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
