package com.basic.framework.oauth2.storage.jpa.service;

import com.basic.framework.core.domain.PageResult;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.authorization.server.util.OAuth2JsonUtils;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.storage.core.domain.BasicAuthorization;
import com.basic.framework.oauth2.storage.core.domain.request.FindAuthorizationPageRequest;
import com.basic.framework.oauth2.storage.core.domain.response.FindAuthorizationPageResponse;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.jpa.converter.Authorization2JpaConverter;
import com.basic.framework.oauth2.storage.jpa.converter.Jpa2AuthorizationConverter;
import com.basic.framework.oauth2.storage.jpa.converter.Jpa2AuthorizationResponseConverter;
import com.basic.framework.oauth2.storage.jpa.domain.JpaOAuth2Authorization;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2AuthorizationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 基于Jpa实现的认证信息存储服务实现
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class JpaBasicAuthorizationService implements BasicAuthorizationService {

    private final OAuth2AuthorizationRepository authorizationRepository;

    private final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };

    private final Authorization2JpaConverter authorization2JpaConverter = new Authorization2JpaConverter();

    private final Jpa2AuthorizationConverter jpa2AuthorizationConverter = new Jpa2AuthorizationConverter();

    private final Jpa2AuthorizationResponseConverter jpa2AuthorizationResponseConverter =
            new Jpa2AuthorizationResponseConverter();

    @Override
    public void save(BasicAuthorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        Optional<JpaOAuth2Authorization> authorizationById = this.authorizationRepository.findById(authorization.getId());

        JpaOAuth2Authorization jpaOAuth2Authorization = authorization2JpaConverter.convert(authorization);
        if (jpaOAuth2Authorization == null) {
            if (log.isDebugEnabled()) {
                log.debug("authorization convert failed. Interrupt OAuth2Authorization save.");
            }
            return;
        }

        if (authorizationById.isPresent()) {
            this.authorizationRepository.deleteById(authorization.getId());
            jpaOAuth2Authorization.setCreateTime(authorizationById.get().getCreateTime());
        }
        if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2ClientAuthenticationToken) {
            Map<String, Object> attributes = OAuth2JsonUtils.toObject(authorization.getAttributes(), typeRef.getType());
            if (attributes != null) {
                // 现在拿不到当前登录用户，直接从认证对象中拿
                Object attribute = attributes.get(Principal.class.getName());
                if (attribute instanceof AbstractAuthenticationToken authenticationToken) {
                    if (authenticationToken.getPrincipal() instanceof AuthenticatedUser user) {
                        jpaOAuth2Authorization.setUpdateBy(user.getId());
                        jpaOAuth2Authorization.setCreateBy(user.getId());
                    }
                }
            }
        }
        this.authorizationRepository.save(jpaOAuth2Authorization);
    }

    @Override
    public void remove(String id) {
        Assert.notNull(id, "Authorization id cannot be null");
        this.authorizationRepository.deleteById(id);
    }

    @Override
    public BasicAuthorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.authorizationRepository.findById(id)
                .map(jpa2AuthorizationConverter::convert)
                .orElse(null);
    }

    @Override
    public BasicAuthorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");

        Optional<JpaOAuth2Authorization> authorization;
        if (tokenType == null) {
            authorization = this.authorizationRepository.findByToken(token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            authorization = this.authorizationRepository.findByState(token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            authorization = this.authorizationRepository.findByAuthorizationCodeValue(token);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            authorization = this.authorizationRepository.findByAccessTokenValue(token);
        } else if (OidcParameterNames.ID_TOKEN.equals(tokenType.getValue())) {
            authorization = this.authorizationRepository.findByOidcIdTokenValue(token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            authorization = this.authorizationRepository.findByRefreshTokenValue(token);
        } else if (OAuth2ParameterNames.USER_CODE.equals(tokenType.getValue())) {
            authorization = this.authorizationRepository.findByUserCodeValue(token);
        } else if (OAuth2ParameterNames.DEVICE_CODE.equals(tokenType.getValue())) {
            authorization = this.authorizationRepository.findByDeviceCodeValue(token);
        } else {
            authorization = Optional.empty();
        }

        return authorization.map(jpa2AuthorizationConverter::convert).orElse(null);
    }

    @Override
    public PageResult<FindAuthorizationPageResponse> findAuthorizationPage(FindAuthorizationPageRequest request) {

        // 分页
        PageRequest pageQuery = PageRequest.of(request.getCurrent().intValue(), request.getSize().intValue());

        // 条件构造器
        SpecificationBuilder<JpaOAuth2Authorization> builder = new SpecificationBuilder<>();
        builder.eq(!ObjectUtils.isEmpty(request.getRegisteredClientId()),
                JpaOAuth2Authorization::getRegisteredClientId, request.getRegisteredClientId());
        builder.eq(!ObjectUtils.isEmpty(request.getAuthorizationGrantType()),
                JpaOAuth2Authorization::getAuthorizationGrantType, request.getAuthorizationGrantType());
        // access token签发时间是否为空
        boolean accessTokenIssuedTime = !ObjectUtils.isEmpty(request.getAccessTokenIssuedStart()) && !ObjectUtils.isEmpty(request.getAccessTokenIssuedEnd());
        builder.between(accessTokenIssuedTime, JpaOAuth2Authorization::getAccessTokenIssuedAt,
                request.getAccessTokenIssuedStart(), request.getAccessTokenIssuedEnd());
        // 授权码签发时间签发时间是否为空
        boolean authorizationCodeIssuedTime = !ObjectUtils.isEmpty(request.getAuthorizationCodeIssuedStart()) && !ObjectUtils.isEmpty(request.getAuthorizationCodeIssuedEnd());
        builder.between(authorizationCodeIssuedTime, JpaOAuth2Authorization::getAuthorizationCodeIssuedAt,
                request.getAuthorizationCodeIssuedStart(), request.getAuthorizationCodeIssuedEnd());

        // 查询
        Page<JpaOAuth2Authorization> authorizationPage = authorizationRepository.findAll(builder, pageQuery);

        // 转为响应bean
        List<FindAuthorizationPageResponse> authorizationList = authorizationPage.getContent()
                .stream()
                .map(jpa2AuthorizationResponseConverter::convert)
                .toList();

        return PageResult.of((long) authorizationPage.getNumber(), (long) authorizationPage.getSize(), authorizationPage.getTotalElements(), authorizationList);
    }
}
