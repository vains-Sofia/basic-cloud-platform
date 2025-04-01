package com.basic.framework.oauth2.storage.service.impl;

import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2AuthorizationConsent;
import com.basic.framework.oauth2.storage.domain.security.BasicAuthorizationConsent;
import com.basic.framework.oauth2.storage.repository.OAuth2AuthorizationConsentRepository;
import com.basic.framework.oauth2.storage.service.BasicAuthorizationConsentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.Set;

/**
 * 基于Jpa实现的oauth2授权确认信息存储实现
 *
 * @author vains
 */
@RequiredArgsConstructor
public class BasicAuthorizationConsentServiceImpl implements BasicAuthorizationConsentService {

    private final OAuth2AuthorizationConsentRepository authorizationConsentRepository;

    @Override
    public void save(BasicAuthorizationConsent basicAuthorizationConsent) {
        Assert.notNull(basicAuthorizationConsent, "authorizationConsent cannot be null");

        // 条件构造器
        SpecificationBuilder<JpaOAuth2AuthorizationConsent> builder = new SpecificationBuilder<>();
        builder.eq(JpaOAuth2AuthorizationConsent::getPrincipalName, basicAuthorizationConsent.getPrincipalName())
                .eq(JpaOAuth2AuthorizationConsent::getRegisteredClientId, basicAuthorizationConsent.getRegisteredClientId());

        Optional<JpaOAuth2AuthorizationConsent> existingConsent = this.authorizationConsentRepository.findOne(builder);
        existingConsent.ifPresent(jpaOAuth2AuthorizationConsent -> this.authorizationConsentRepository.deleteById(jpaOAuth2AuthorizationConsent.getId()));

        JpaOAuth2AuthorizationConsent authorizationConsent = new JpaOAuth2AuthorizationConsent();
        BeanUtils.copyProperties(basicAuthorizationConsent, authorizationConsent);
        authorizationConsent.setAuthorities(JsonUtils.toJson(basicAuthorizationConsent.getAuthorities()));
        this.authorizationConsentRepository.save(authorizationConsent);
    }

    @Override
    public void remove(BasicAuthorizationConsent basicAuthorizationConsent) {
        Assert.notNull(basicAuthorizationConsent, "authorizationConsent cannot be null");
        this.authorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
                basicAuthorizationConsent.getRegisteredClientId(), basicAuthorizationConsent.getPrincipalName());
    }

    @Override
    public BasicAuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Optional<JpaOAuth2AuthorizationConsent> authorizationConsent = authorizationConsentRepository
                .findByRegisteredClientIdAndPrincipalName(registeredClientId, principalName);

        // 转换并响应
        return authorizationConsent.map(existingConsent -> {
            BasicAuthorizationConsent basicAuthorizationConsent = new BasicAuthorizationConsent();
            BeanUtils.copyProperties(existingConsent, basicAuthorizationConsent);
            basicAuthorizationConsent.setAuthorities(JsonUtils.toObject(existingConsent.getAuthorities(), Set.class, String.class));
            return basicAuthorizationConsent;
        }).orElse(null);
    }
}
