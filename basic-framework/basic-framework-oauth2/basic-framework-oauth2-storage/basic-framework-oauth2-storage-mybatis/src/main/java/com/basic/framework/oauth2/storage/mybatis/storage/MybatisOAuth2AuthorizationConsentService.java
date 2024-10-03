package com.basic.framework.oauth2.storage.mybatis.storage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.basic.framework.oauth2.storage.mybatis.converter.Entity2OAuth2AuthorizationConsentConverter;
import com.basic.framework.oauth2.storage.mybatis.converter.OAuth2AuthorizationConsent2EntityConverter;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2AuthorizationConsent;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2AuthorizationConsentMapper;
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

    private final MybatisOAuth2AuthorizationConsentMapper mybatisOAuth2AuthorizationConsentMapper;

    private final Entity2OAuth2AuthorizationConsentConverter authorizationConsentConverter;

    private final OAuth2AuthorizationConsent2EntityConverter entityConverter = new OAuth2AuthorizationConsent2EntityConverter();

    public MybatisOAuth2AuthorizationConsentService(MybatisOAuth2AuthorizationConsentMapper mybatisOAuth2AuthorizationConsentMapper, RegisteredClientRepository registeredClientRepository) {
        this.mybatisOAuth2AuthorizationConsentMapper = mybatisOAuth2AuthorizationConsentMapper;
        this.authorizationConsentConverter = new Entity2OAuth2AuthorizationConsentConverter(registeredClientRepository);
    }

    @Override
    public void save(OAuth2AuthorizationConsent OAuth2AuthorizationConsent) {
        Assert.notNull(OAuth2AuthorizationConsent, "authorizationConsent cannot be null");

        LambdaQueryWrapper<MybatisOAuth2AuthorizationConsent> wrapper = Wrappers.lambdaQuery(MybatisOAuth2AuthorizationConsent.class)
                .eq(MybatisOAuth2AuthorizationConsent::getPrincipalName, OAuth2AuthorizationConsent.getPrincipalName())
                .eq(MybatisOAuth2AuthorizationConsent::getRegisteredClientId, OAuth2AuthorizationConsent.getRegisteredClientId());

        MybatisOAuth2AuthorizationConsent existingConsent = this.mybatisOAuth2AuthorizationConsentMapper.selectOne(wrapper);
        if (existingConsent != null) {
            this.mybatisOAuth2AuthorizationConsentMapper.deleteById(existingConsent);
        }
        MybatisOAuth2AuthorizationConsent consent = this.entityConverter.convert(OAuth2AuthorizationConsent);
        this.mybatisOAuth2AuthorizationConsentMapper.insert(consent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent OAuth2AuthorizationConsent) {
        Assert.notNull(OAuth2AuthorizationConsent, "authorizationConsent cannot be null");
        // 如果存在就删除
        LambdaQueryWrapper<MybatisOAuth2AuthorizationConsent> wrapper = Wrappers.lambdaQuery(MybatisOAuth2AuthorizationConsent.class)
                .eq(MybatisOAuth2AuthorizationConsent::getPrincipalName, OAuth2AuthorizationConsent.getPrincipalName())
                .eq(MybatisOAuth2AuthorizationConsent::getRegisteredClientId, OAuth2AuthorizationConsent.getRegisteredClientId());

        MybatisOAuth2AuthorizationConsent existingConsent = this.mybatisOAuth2AuthorizationConsentMapper.selectOne(wrapper);
        if (existingConsent != null) {
            this.mybatisOAuth2AuthorizationConsentMapper.deleteById(existingConsent.getId());
        }
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        LambdaQueryWrapper<MybatisOAuth2AuthorizationConsent> wrapper = Wrappers.lambdaQuery(MybatisOAuth2AuthorizationConsent.class)
                .eq(MybatisOAuth2AuthorizationConsent::getPrincipalName, principalName)
                .eq(MybatisOAuth2AuthorizationConsent::getRegisteredClientId, registeredClientId);

        MybatisOAuth2AuthorizationConsent existingConsent = this.mybatisOAuth2AuthorizationConsentMapper.selectOne(wrapper);
        return this.authorizationConsentConverter.convert(existingConsent);
    }
}
