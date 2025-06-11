package com.basic.framework.oauth2.storage.service.impl;

import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.storage.converter.Authorization2JpaConverter;
import com.basic.framework.oauth2.storage.converter.Jpa2AuthorizationConverter;
import com.basic.framework.oauth2.storage.converter.Jpa2AuthorizationResponseConverter;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2Application;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2Authorization;
import com.basic.framework.oauth2.storage.domain.request.FindAuthorizationPageRequest;
import com.basic.framework.oauth2.storage.domain.response.FindAuthorizationResponse;
import com.basic.framework.oauth2.storage.domain.security.BasicAuthorization;
import com.basic.framework.oauth2.storage.repository.OAuth2ApplicationRepository;
import com.basic.framework.oauth2.storage.repository.OAuth2AuthorizationRepository;
import com.basic.framework.oauth2.storage.service.BasicAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于Jpa实现的认证信息存储服务实现
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class BasicAuthorizationServiceImpl implements BasicAuthorizationService {

    private final OAuth2ApplicationRepository applicationRepository;

    private final OAuth2AuthorizationRepository authorizationRepository;

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
        } else {
            jpaOAuth2Authorization.setCreateTime(LocalDateTime.now());
        }
        jpaOAuth2Authorization.setUpdateTime(LocalDateTime.now());
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
    public PageResult<FindAuthorizationResponse> findAuthorizationPage(FindAuthorizationPageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(JpaOAuth2Authorization::getUpdateTime));

        // 分页
        PageRequest pageQuery = PageRequest.of(request.current(), request.size(), sort);

        // 条件构造器
        SpecificationBuilder<JpaOAuth2Authorization> builder = new SpecificationBuilder<>();
        builder.eq(!ObjectUtils.isEmpty(request.getRegisteredClientId()),
                JpaOAuth2Authorization::getRegisteredClientId, request.getRegisteredClientId());
        builder.eq(!ObjectUtils.isEmpty(request.getAuthorizationGrantType()),
                JpaOAuth2Authorization::getAuthorizationGrantType, request.getAuthorizationGrantType());
        // access token签发时间是否为空
        boolean accessTokenIssuedTime = !ObjectUtils.isEmpty(request.getAccessTokenIssuedStart()) && !ObjectUtils.isEmpty(request.getAccessTokenIssuedEnd());
        // 授权码签发时间签发时间是否为空
        boolean authorizationCodeIssuedTime = !ObjectUtils.isEmpty(request.getAuthorizationCodeIssuedStart()) && !ObjectUtils.isEmpty(request.getAuthorizationCodeIssuedEnd());

        builder.between(accessTokenIssuedTime, JpaOAuth2Authorization::getAccessTokenIssuedAt,
                        request.getAccessTokenIssuedStart(), request.getAccessTokenIssuedEnd())
                .between(authorizationCodeIssuedTime, JpaOAuth2Authorization::getAuthorizationCodeIssuedAt,
                        request.getAuthorizationCodeIssuedStart(), request.getAuthorizationCodeIssuedEnd());

        // 查询
        Page<JpaOAuth2Authorization> authorizationPage = authorizationRepository.findAll(builder, pageQuery);

        // 转为响应bean
        List<FindAuthorizationResponse> authorizationList = authorizationPage.getContent()
                .stream()
                .map(jpa2AuthorizationResponseConverter::convert)
                .toList();

        // 提取授权时使用的客户端id
        Set<Long> applicationIdSet = authorizationList.stream()
                .map(FindAuthorizationResponse::getRegisteredClientId)
                .filter(id -> !ObjectUtils.isEmpty(id))
                .map(Long::valueOf)
                .collect(Collectors.toSet());

        // 查询对应的客户端信息
        List<JpaOAuth2Application> oAuth2Applications = applicationRepository.findAllById(applicationIdSet);
        // 构建id和客户端信息的映射
        var applicationMap = oAuth2Applications.stream()
                .collect(Collectors.toMap(JpaOAuth2Application::getId, Function.identity()));
        // 将客户端信息映射到授权响应中
        authorizationList.stream()
                .filter(e -> !ObjectUtils.isEmpty(e.getRegisteredClientId()))
                .forEach(authorization -> {
                    String applicationId = authorization.getRegisteredClientId();
                    JpaOAuth2Application application = applicationMap.get(Long.valueOf(applicationId));
                    if (application != null) {
                        authorization.setRegisteredClientName(application.getClientName());
                        authorization.setRegisteredClientLogo(application.getClientLogo());
                    }
                });

        return DataPageResult.of(authorizationPage.getNumber(), authorizationPage.getSize(), authorizationPage.getTotalElements(), authorizationList);
    }
}
