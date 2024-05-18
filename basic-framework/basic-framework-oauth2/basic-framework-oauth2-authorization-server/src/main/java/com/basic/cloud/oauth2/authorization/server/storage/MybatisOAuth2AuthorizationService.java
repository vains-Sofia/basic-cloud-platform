package com.basic.cloud.oauth2.authorization.server.storage;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.basic.cloud.oauth2.authorization.server.converter.Authorization2OAuth2AuthorizationConverter;
import com.basic.cloud.oauth2.authorization.server.converter.OAuth2Authorization2AuthorizationConverter;
import com.basic.cloud.oauth2.authorization.server.entity.Authorization;
import com.basic.cloud.oauth2.authorization.server.mapper.AuthorizationMapper;
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

    private final AuthorizationMapper authorizationMapper;

    private final Authorization2OAuth2AuthorizationConverter oAuth2AuthorizationConverter;

    private final OAuth2Authorization2AuthorizationConverter authorizationConverter = new OAuth2Authorization2AuthorizationConverter();

    public MybatisOAuth2AuthorizationService(AuthorizationMapper authorizationMapper, RegisteredClientRepository registeredClientRepository) {
        this.authorizationMapper = authorizationMapper;
        this.oAuth2AuthorizationConverter = new Authorization2OAuth2AuthorizationConverter(registeredClientRepository);
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        Authorization storageAuthorization = authorizationMapper.selectById(authorization.getId());
        if (storageAuthorization != null) {
            this.authorizationMapper.deleteById(authorization.getId());
        }
        Authorization convert = this.authorizationConverter.convert(authorization);
        this.authorizationMapper.insert(convert);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        this.authorizationMapper.deleteById(authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        Authorization authorization = this.authorizationMapper.selectById(id);
        return this.oAuth2AuthorizationConverter.convert(authorization);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        LambdaQueryWrapper<Authorization> wrapper = Wrappers.lambdaQuery(Authorization.class);
        if (tokenType == null) {
            wrapper.or(e -> e.eq(Authorization::getState, token)
                    .eq(Authorization::getUserCodeValue, token)
                    .eq(Authorization::getDeviceCodeValue, token)
                    .eq(Authorization::getAccessTokenValue, token)
                    .eq(Authorization::getOidcIdTokenValue, token)
                    .eq(Authorization::getRefreshTokenValue, token)
                    .eq(Authorization::getAuthorizationCodeValue, token)
            );
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            wrapper.eq(Authorization::getState, token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            wrapper.eq(Authorization::getAuthorizationCodeValue, token);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            wrapper.eq(Authorization::getAccessTokenValue, token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            wrapper.eq(Authorization::getOidcIdTokenValue, token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            wrapper.eq(Authorization::getRefreshTokenValue, token);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            wrapper.eq(Authorization::getUserCodeValue, token);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            wrapper.eq(Authorization::getDeviceCodeValue, token);
        } else {
            return null;
        }
        Authorization authorization = this.authorizationMapper.selectOne(wrapper);
        return this.oAuth2AuthorizationConverter.convert(authorization);
    }
}
