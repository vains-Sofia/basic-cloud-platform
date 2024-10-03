package com.basic.framework.oauth2.storage.mybatis.storage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.basic.framework.oauth2.storage.mybatis.converter.Authorization2OAuth2AuthorizationConverter;
import com.basic.framework.oauth2.storage.mybatis.converter.OAuth2Authorization2AuthorizationConverter;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2Authorization;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2AuthorizationMapper;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * 核心services——认证信息
 *
 * @author vains
 */
public class MybatisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final MybatisOAuth2AuthorizationMapper mybatisOAuth2AuthorizationMapper;

    private final Authorization2OAuth2AuthorizationConverter oAuth2AuthorizationConverter;

    private final OAuth2Authorization2AuthorizationConverter authorizationConverter = new OAuth2Authorization2AuthorizationConverter();

    public MybatisOAuth2AuthorizationService(MybatisOAuth2AuthorizationMapper mybatisOAuth2AuthorizationMapper, RegisteredClientRepository registeredClientRepository) {
        this.mybatisOAuth2AuthorizationMapper = mybatisOAuth2AuthorizationMapper;
        this.oAuth2AuthorizationConverter = new Authorization2OAuth2AuthorizationConverter(registeredClientRepository);
    }

    @Override
    public void save(OAuth2Authorization OAuth2Authorization) {
        Assert.notNull(OAuth2Authorization, "authorization cannot be null");
        MybatisOAuth2Authorization storageMybatisOAuth2Authorization = this.mybatisOAuth2AuthorizationMapper.selectById(OAuth2Authorization.getId());
        if (storageMybatisOAuth2Authorization != null) {
            this.mybatisOAuth2AuthorizationMapper.deleteById(OAuth2Authorization.getId());
        }
        MybatisOAuth2Authorization convert = this.authorizationConverter.convert(OAuth2Authorization);
        this.mybatisOAuth2AuthorizationMapper.insert(convert);
    }

    @Override
    public void remove(OAuth2Authorization OAuth2Authorization) {
        Assert.notNull(OAuth2Authorization, "authorization cannot be null");
        this.mybatisOAuth2AuthorizationMapper.deleteById(OAuth2Authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        MybatisOAuth2Authorization MybatisOAuth2Authorization = this.mybatisOAuth2AuthorizationMapper.selectById(id);
        return this.oAuth2AuthorizationConverter.convert(MybatisOAuth2Authorization);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        LambdaQueryWrapper<MybatisOAuth2Authorization> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Authorization.class);
        if (tokenType == null) {
            wrapper.or(e -> e.eq(MybatisOAuth2Authorization::getState, token)
                    .eq(MybatisOAuth2Authorization::getUserCodeValue, token)
                    .eq(MybatisOAuth2Authorization::getDeviceCodeValue, token)
                    .eq(MybatisOAuth2Authorization::getAccessTokenValue, token)
                    .eq(MybatisOAuth2Authorization::getOidcIdTokenValue, token)
                    .eq(MybatisOAuth2Authorization::getRefreshTokenValue, token)
                    .eq(MybatisOAuth2Authorization::getAuthorizationCodeValue, token)
            );
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            wrapper.eq(MybatisOAuth2Authorization::getState, token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            wrapper.eq(MybatisOAuth2Authorization::getAuthorizationCodeValue, token);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            wrapper.eq(MybatisOAuth2Authorization::getAccessTokenValue, token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            wrapper.eq(MybatisOAuth2Authorization::getOidcIdTokenValue, token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            wrapper.eq(MybatisOAuth2Authorization::getRefreshTokenValue, token);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            wrapper.eq(MybatisOAuth2Authorization::getUserCodeValue, token);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            wrapper.eq(MybatisOAuth2Authorization::getDeviceCodeValue, token);
        } else {
            return null;
        }
        MybatisOAuth2Authorization MybatisOAuth2Authorization = this.mybatisOAuth2AuthorizationMapper.selectOne(wrapper);
        return this.oAuth2AuthorizationConverter.convert(MybatisOAuth2Authorization);
    }
}
