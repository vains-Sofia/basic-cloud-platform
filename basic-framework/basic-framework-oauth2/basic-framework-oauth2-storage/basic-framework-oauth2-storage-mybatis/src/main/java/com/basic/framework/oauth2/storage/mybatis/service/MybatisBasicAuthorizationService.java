package com.basic.framework.oauth2.storage.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.basic.framework.oauth2.authorization.server.util.OAuth2JsonUtils;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.storage.core.domain.BasicAuthorization;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2Authorization;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2AuthorizationMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.Map;

/**
 * 基于MybatisPlus实现的认证信息存储服务实现
 *
 * @author vains
 */
@RequiredArgsConstructor
public class MybatisBasicAuthorizationService implements BasicAuthorizationService {

    private final MybatisOAuth2AuthorizationMapper mybatisOAuth2AuthorizationMapper;

    private final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };

    @Override
    public void save(BasicAuthorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        MybatisOAuth2Authorization storageMybatisOAuth2Authorization = this.mybatisOAuth2AuthorizationMapper.selectById(authorization.getId());

        MybatisOAuth2Authorization convert = new MybatisOAuth2Authorization();
        BeanUtils.copyProperties(authorization, convert);

        if (storageMybatisOAuth2Authorization != null) {
            this.mybatisOAuth2AuthorizationMapper.deleteById(authorization.getId());
            convert.setCreateTime(storageMybatisOAuth2Authorization.getCreateTime());
        }
        if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2ClientAuthenticationToken) {
            Map<String, Object> attributes = OAuth2JsonUtils.toObject(authorization.getAttributes(), typeRef.getType());
            if (attributes != null) {
                // 现在拿不到当前登录用户，直接从认证对象中拿
                Object attribute = attributes.get(Principal.class.getName());
                if (attribute instanceof UsernamePasswordAuthenticationToken authenticationToken) {
                    if (authenticationToken.getPrincipal() instanceof AuthenticatedUser user) {
                        convert.setUpdateBy(user.getId());
                        convert.setCreateBy(user.getId());
                    }
                }
            }
        }
        this.mybatisOAuth2AuthorizationMapper.insert(convert);
    }

    @Override
    public void remove(String id) {
        Assert.notNull(id, "Authorization id cannot be null");
        this.mybatisOAuth2AuthorizationMapper.deleteById(id);
    }

    @Override
    public BasicAuthorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        MybatisOAuth2Authorization storageAuthorization = this.mybatisOAuth2AuthorizationMapper.selectById(id);
        BasicAuthorization authorization = new BasicAuthorization();
        BeanUtils.copyProperties(storageAuthorization, authorization);
        return authorization;
    }

    @Override
    public BasicAuthorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        LambdaQueryWrapper<MybatisOAuth2Authorization> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Authorization.class);
        if (tokenType == null) {
            wrapper.eq(MybatisOAuth2Authorization::getState, token)
                    .or().eq(MybatisOAuth2Authorization::getUserCodeValue, token)
                    .or().eq(MybatisOAuth2Authorization::getDeviceCodeValue, token)
                    .or().eq(MybatisOAuth2Authorization::getAccessTokenValue, token)
                    .or().eq(MybatisOAuth2Authorization::getOidcIdTokenValue, token)
                    .or().eq(MybatisOAuth2Authorization::getRefreshTokenValue, token)
                    .or().eq(MybatisOAuth2Authorization::getAuthorizationCodeValue, token);
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
        MybatisOAuth2Authorization mybatisOAuth2Authorization = this.mybatisOAuth2AuthorizationMapper.selectOne(wrapper);
        BasicAuthorization authorization = new BasicAuthorization();
        BeanUtils.copyProperties(mybatisOAuth2Authorization, authorization);
        return authorization;
    }
}
