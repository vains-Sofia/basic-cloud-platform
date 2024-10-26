package com.basic.framework.oauth2.storage.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.basic.framework.oauth2.storage.core.domain.BasicAuthorizationConsent;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationConsentService;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2AuthorizationConsent;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2AuthorizationConsentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * 基于MybatisPlus实现的oauth2授权确认信息存储实现
 *
 * @author vains
 */
@RequiredArgsConstructor
public class MybatisBasicAuthorizationConsentService implements BasicAuthorizationConsentService {

    private final MybatisOAuth2AuthorizationConsentMapper mybatisOAuth2AuthorizationConsentMapper;

    @Override
    public void save(BasicAuthorizationConsent basicAuthorizationConsent) {
        Assert.notNull(basicAuthorizationConsent, "authorizationConsent cannot be null");

        LambdaQueryWrapper<MybatisOAuth2AuthorizationConsent> wrapper = Wrappers.lambdaQuery(MybatisOAuth2AuthorizationConsent.class)
                .eq(MybatisOAuth2AuthorizationConsent::getPrincipalName, basicAuthorizationConsent.getPrincipalName())
                .eq(MybatisOAuth2AuthorizationConsent::getRegisteredClientId, basicAuthorizationConsent.getRegisteredClientId());

        MybatisOAuth2AuthorizationConsent existingConsent = this.mybatisOAuth2AuthorizationConsentMapper.selectOne(wrapper);
        if (existingConsent != null) {
            this.mybatisOAuth2AuthorizationConsentMapper.deleteById(existingConsent);
        }
        MybatisOAuth2AuthorizationConsent authorizationConsent = new MybatisOAuth2AuthorizationConsent();
        BeanUtils.copyProperties(basicAuthorizationConsent, authorizationConsent);
        this.mybatisOAuth2AuthorizationConsentMapper.insert(authorizationConsent);
    }

    @Override
    public void remove(BasicAuthorizationConsent basicAuthorizationConsent) {
        Assert.notNull(basicAuthorizationConsent, "authorizationConsent cannot be null");
        // 如果存在就删除
        LambdaQueryWrapper<MybatisOAuth2AuthorizationConsent> wrapper = Wrappers.lambdaQuery(MybatisOAuth2AuthorizationConsent.class)
                .eq(MybatisOAuth2AuthorizationConsent::getPrincipalName, basicAuthorizationConsent.getPrincipalName())
                .eq(MybatisOAuth2AuthorizationConsent::getRegisteredClientId, basicAuthorizationConsent.getRegisteredClientId());

        MybatisOAuth2AuthorizationConsent existingConsent = this.mybatisOAuth2AuthorizationConsentMapper.selectOne(wrapper);
        if (existingConsent != null) {
            this.mybatisOAuth2AuthorizationConsentMapper.deleteById(existingConsent.getId());
        }
    }

    @Override
    public BasicAuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        LambdaQueryWrapper<MybatisOAuth2AuthorizationConsent> wrapper = Wrappers.lambdaQuery(MybatisOAuth2AuthorizationConsent.class)
                .eq(MybatisOAuth2AuthorizationConsent::getPrincipalName, principalName)
                .eq(MybatisOAuth2AuthorizationConsent::getRegisteredClientId, registeredClientId);

        MybatisOAuth2AuthorizationConsent existingConsent = this.mybatisOAuth2AuthorizationConsentMapper.selectOne(wrapper);

        if (existingConsent == null) {
            return null;
        }

        BasicAuthorizationConsent basicAuthorizationConsent = new BasicAuthorizationConsent();
        BeanUtils.copyProperties(existingConsent, basicAuthorizationConsent);
        return basicAuthorizationConsent;
    }
}
