package com.basic.framework.oauth2.storage.service.impl;

import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.authorization.server.util.OAuth2JsonUtils;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import com.basic.framework.oauth2.core.util.ServletUtils;
import com.basic.framework.oauth2.storage.converter.Authorization2JpaConverter;
import com.basic.framework.oauth2.storage.converter.Jpa2AuthorizationConverter;
import com.basic.framework.oauth2.storage.converter.Jpa2AuthorizationResponseConverter;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2Application;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2Authorization;
import com.basic.framework.oauth2.storage.domain.request.FindAuthorizationPageRequest;
import com.basic.framework.oauth2.storage.domain.request.OfflineAuthorizationRequest;
import com.basic.framework.oauth2.storage.domain.response.FindAuthorizationResponse;
import com.basic.framework.oauth2.storage.domain.security.BasicAuthorization;
import com.basic.framework.oauth2.storage.repository.OAuth2ApplicationRepository;
import com.basic.framework.oauth2.storage.repository.OAuth2AuthorizationRepository;
import com.basic.framework.oauth2.storage.service.BasicAuthorizationService;
import com.basic.framework.redis.support.RedisOperator;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.JTI;
import static org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Token.CLAIMS_METADATA_NAME;
import static org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Token.INVALIDATED_METADATA_NAME;

/**
 * 基于Jpa实现的认证信息存储服务实现
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class BasicAuthorizationServiceImpl implements BasicAuthorizationService {

    private final SessionRegistry sessionRegistry;

    private final RedisOperator<Long> redisHashOperator;

    private final OAuth2ApplicationRepository applicationRepository;

    private final OAuth2AuthorizationRepository authorizationRepository;

    private final SessionRepository<? extends Session> sessionRepository;

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
            // 设置授权用户id
            if (authorizationById.get().getPrincipalId() == null) {
                // 当前用户id
                Long userId = SecurityUtils.getUserId();
                // 如果没有用户信息，则设置为客户端id
                jpaOAuth2Authorization.setPrincipalId(Objects.requireNonNullElseGet(userId, () -> Long.valueOf(jpaOAuth2Authorization.getRegisteredClientId())));
            } else {
                // 如果已经存在授权信息，则保留原有的用户id
                jpaOAuth2Authorization.setPrincipalId(authorizationById.get().getPrincipalId());
            }
            jpaOAuth2Authorization.setCreateTime(authorizationById.get().getCreateTime());
        } else {
            jpaOAuth2Authorization.setCreateTime(LocalDateTime.now());
            // 当前用户id
            Long userId = SecurityUtils.getUserId();
            // 如果没有用户信息，则设置为客户端id
            jpaOAuth2Authorization.setPrincipalId(Objects.requireNonNullElseGet(userId, () -> Long.valueOf(jpaOAuth2Authorization.getRegisteredClientId())));
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

    @Override
    public void offline(OfflineAuthorizationRequest request) {
        String accessToken = request.getAccessToken();

        // 根据accessToken查询授权信息
        Optional<JpaOAuth2Authorization> authorizationOptional = this.authorizationRepository.findByAccessTokenValue(accessToken);
        if (authorizationOptional.isEmpty()) {
            log.warn("令牌不存在或已过期，请传入正确的access token。 access token: {}", accessToken);
            throw new CloudServiceException("令牌不存在或已过期，请传入正确的access token。");
        }

        // 修改授权信息的元数据为已撤销状态
        JpaOAuth2Authorization authorization = authorizationOptional.get();
        // access token元数据
        Map<String, Object> accessTokenMetadata = new HashMap<>(1);

        String metadata = authorization.getAccessTokenMetadata();
        if (!ObjectUtils.isEmpty(metadata)) {
            // 将元数据转换为Map
            Map<String, Object> metadataMap = OAuth2JsonUtils.toObject(metadata, typeRef.getType());
            if (metadataMap != null) {
                accessTokenMetadata.putAll(metadataMap);
                // 设置元数据中的无效化标志
                accessTokenMetadata.put(INVALIDATED_METADATA_NAME, Boolean.TRUE);
            }
        }

        if (accessTokenMetadata.isEmpty()) {
            // 如果没有元数据，则设置已失效状态为true
            accessTokenMetadata.put(INVALIDATED_METADATA_NAME, Boolean.TRUE);
        }

        // 更新授权信息
        authorization.setAccessTokenMetadata(OAuth2JsonUtils.toJson(accessTokenMetadata));
        this.authorizationRepository.save(authorization);

        if (log.isDebugEnabled()) {
            log.debug("Access token revoked successfully. Access token: {}", accessToken);
        }

        // 获取授权类型
        String authorizationGrantType = authorization.getAuthorizationGrantType();
        if (!Objects.equals(authorizationGrantType, AuthorizationGrantType.AUTHORIZATION_CODE.getValue())) {
            // 如果授权类型不是授权码模式，则直接返回
            return;
        }

        if (ObjectUtils.isEmpty(authorization.getAttributes())) {
            // 如果attributes为空，则直接返回
            return;
        }

        // 获取认证信息attributes
        Map<String, Object> attributes = OAuth2JsonUtils.toObject(authorization.getAttributes(), typeRef.getType());
        if (attributes == null || attributes.isEmpty()) {
            // 如果attributes为空，则直接返回
            return;
        }

        // 尝试删除access token与用户id的映射关系
        Map<String, Object> claims = OAuth2JsonUtils.objectToObject(accessTokenMetadata.get(CLAIMS_METADATA_NAME), Map.class, String.class, Object.class);
        String jti = claims.get(JTI) + "";
        Long l = redisHashOperator.deleteHashField(AuthorizeConstants.JTI_USER_HASH, jti);
        if (l > 0) {
            if (log.isDebugEnabled()) {
                log.debug("Access token jti: {} removed from Redis with result: {}", jti, l);
            }
        }

        // 获取Principal对象
        Object principalObject = attributes.get(Principal.class.getName());

        // 尝试清除Session
        if (!(principalObject instanceof AbstractAuthenticationToken authenticationToken)) {
            return;
        }
        // 如果Principal是AbstractAuthenticationToken类型，则获取认证用户信息
        if (!(authenticationToken.getPrincipal() instanceof AuthenticatedUser authenticatedUser)) {
            return;
        }

        // 获取access token对应的Session信息
        List<SessionInformation> authenticationSessions = this.sessionRegistry.getAllSessions(authenticatedUser, Boolean.TRUE);

        // access token对应的sessionId
        List<String> sessionIds = authenticationSessions.stream()
                .filter(Objects::nonNull)
                .map(SessionInformation::getSessionId)
                .filter(Objects::nonNull)
                .toList();

        for (SessionInformation authenticationSession : authenticationSessions) {
            // 检查Session是否存在
            if (authenticationSession != null) {
                // 使Session失效
                authenticationSession.expireNow();
            }
        }

        // 清除Session信息
        if (!ObjectUtils.isEmpty(sessionIds)) {
            if (log.isDebugEnabled()) {
                log.debug("Removing session with id list: {}", JsonUtils.toJson(sessionIds));
            }
            for (String sessionId : sessionIds) {
                HttpServletRequest httpRequest = ServletUtils.getRequest();
                if (httpRequest != null) {
                    // 获取当前Session
                    HttpSession currentSession = httpRequest.getSession(Boolean.FALSE);
                    if (currentSession != null && sessionId.equals(currentSession.getId())) {
                        // 如果当前Session与要清除的Session相同，则使其失效
                        currentSession.invalidate();
                        // 跳过Redis Session的清除
                        continue;
                    }
                }

                // 清除SessionRepository中的Session信息
                Session session = this.sessionRepository.findById(sessionId);
                if (session != null) {
                    // 销毁Session
                    this.sessionRepository.deleteById(sessionId);
                }
            }
        }
    }

}
