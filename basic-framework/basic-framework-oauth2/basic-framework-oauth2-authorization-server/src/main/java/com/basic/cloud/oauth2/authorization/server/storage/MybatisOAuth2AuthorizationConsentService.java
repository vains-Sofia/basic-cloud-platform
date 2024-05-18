package com.basic.cloud.oauth2.authorization.server.storage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.basic.cloud.oauth2.authorization.server.converter.Entity2OAuth2AuthorizationConsentConverter;
import com.basic.cloud.oauth2.authorization.server.converter.OAuth2AuthorizationConsent2EntityConverter;
import com.basic.cloud.oauth2.authorization.server.entity.AuthorizationConsent;
import com.basic.cloud.oauth2.authorization.server.mapper.AuthorizationConsentMapper;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * 核心services——授权确认信息
 *
 * @author vains
 */
public class MybatisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final AuthorizationConsentMapper authorizationConsentMapper;

    private final Entity2OAuth2AuthorizationConsentConverter authorizationConsentConverter;

    private final OAuth2AuthorizationConsent2EntityConverter entityConverter = new OAuth2AuthorizationConsent2EntityConverter();

    public MybatisOAuth2AuthorizationConsentService(AuthorizationConsentMapper authorizationConsentMapper, RegisteredClientRepository registeredClientRepository) {
        this.authorizationConsentMapper = authorizationConsentMapper;
        this.authorizationConsentConverter = new Entity2OAuth2AuthorizationConsentConverter(registeredClientRepository);
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");

        LambdaQueryWrapper<AuthorizationConsent> wrapper = Wrappers.lambdaQuery(AuthorizationConsent.class)
                .eq(AuthorizationConsent::getPrincipalName, authorizationConsent.getPrincipalName())
                .eq(AuthorizationConsent::getRegisteredClientId, authorizationConsent.getRegisteredClientId());

        AuthorizationConsent existingConsent = this.authorizationConsentMapper.selectOne(wrapper);
        if (existingConsent != null) {
            this.authorizationConsentMapper.deleteById(existingConsent);
        }
        AuthorizationConsent consent = this.entityConverter.convert(authorizationConsent);
        this.authorizationConsentMapper.insert(consent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        // 如果存在就删除
        LambdaQueryWrapper<AuthorizationConsent> wrapper = Wrappers.lambdaQuery(AuthorizationConsent.class)
                .eq(AuthorizationConsent::getPrincipalName, authorizationConsent.getPrincipalName())
                .eq(AuthorizationConsent::getRegisteredClientId, authorizationConsent.getRegisteredClientId());

        AuthorizationConsent existingConsent = this.authorizationConsentMapper.selectOne(wrapper);
        if (existingConsent != null) {
            this.authorizationConsentMapper.deleteById(existingConsent.getId());
        }
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        LambdaQueryWrapper<AuthorizationConsent> wrapper = Wrappers.lambdaQuery(AuthorizationConsent.class)
                .eq(AuthorizationConsent::getPrincipalName, principalName)
                .eq(AuthorizationConsent::getRegisteredClientId, registeredClientId);

        AuthorizationConsent existingConsent = this.authorizationConsentMapper.selectOne(wrapper);
        return this.authorizationConsentConverter.convert(existingConsent);
    }
}
