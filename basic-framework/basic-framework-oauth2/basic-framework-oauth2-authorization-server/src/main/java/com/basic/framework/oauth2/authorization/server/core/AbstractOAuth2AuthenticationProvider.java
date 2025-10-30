package com.basic.framework.oauth2.authorization.server.core;

import com.basic.framework.oauth2.authorization.server.util.OAuth2AuthenticationProviderUtils;
import com.basic.framework.oauth2.authorization.server.util.OAuth2EndpointUtils;
import com.basic.framework.oauth2.core.token.generator.StandardOAuth2TokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象的认证provider类，提供一些可能会用到的公共方法
 *
 * @author vains
 * @see org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractOAuth2AuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationProvider authenticationProvider;

    private final OAuth2AuthorizationService authorizationService;

    private final StandardOAuth2TokenGenerator standardOAuth2TokenGenerator;

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    /**
     * 获取认证过的scope
     *
     * @param registeredClient 客户端
     * @param requestedScopes  请求中的scope
     * @return 认证过的scope
     */
    protected Set<String> getAuthorizedScopes(RegisteredClient registeredClient, Set<String> requestedScopes) {
        // Default to configured scopes
        Set<String> authorizedScopes;
        if (!ObjectUtils.isEmpty(requestedScopes)) {
            Set<String> unauthorizedScopes = requestedScopes.stream()
                    .filter(requestedScope -> !registeredClient.getScopes().contains(requestedScope))
                    .collect(Collectors.toSet());
            if (!ObjectUtils.isEmpty(unauthorizedScopes)) {
                OAuth2EndpointUtils.throwError(
                        OAuth2ErrorCodes.INVALID_REQUEST,
                        OAuth2ParameterNames.SCOPE,
                        ERROR_URI);
            }

            authorizedScopes = new LinkedHashSet<>(requestedScopes);
        } else {
            authorizedScopes = new LinkedHashSet<>();
        }

        if (log.isTraceEnabled()) {
            log.trace("Validated token request parameters");
        }
        return authorizedScopes;
    }

    /**
     * 对用户提交的认证信息进行认证
     *
     * @param authenticationToken oauth2请求时生成的令牌(oauth2请求时携带的数据)
     * @return 认证后的认证信息
     */
    protected Authentication authenticateAuthenticationToken(AbstractOAuth2AuthenticationToken authenticationToken) {
        Authentication unauthenticated = getUnauthenticatedToken(authenticationToken);
        try {
            // 交给 userDetailsAuthenticationProvider 进行认证
            return authenticationProvider.authenticate(unauthenticated);
        } catch (AuthenticationException e) {
            // 包装异常为OAuth2AuthenticationException，使OAuth2TokenEndpointFilter的异常处理可以正确捕获异常信息
            log.debug("Authentication failed", e);
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, e.getMessage(), ERROR_URI), e);
        } catch (Exception e) {
            // 包装异常为OAuth2AuthenticationException，使OAuth2TokenEndpointFilter的异常处理可以正确捕获异常信息
            log.debug("Authentication failed", e);
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, e.getMessage(), ERROR_URI), e);
        }
    }

    /**
     * 由子类实现该方法获取一个未认证的令牌，交由AuthorizationProvider认证
     *
     * @param authenticationToken oauth2认证登录生成的token
     * @return 未认证的token
     */
    protected abstract Authentication getUnauthenticatedToken(AbstractOAuth2AuthenticationToken authenticationToken);

    /**
     * 组合provider中通用逻辑，包括验证客户端、scopes、用户信息，生成access token
     *
     * @param receivedToken oauth2认证登录令牌
     * @return 返回oauth2 access token相关
     */
    protected Authentication authentication(AbstractOAuth2AuthenticationToken receivedToken) {
        // 获取认证过的客户端信息
        OAuth2ClientAuthenticationToken clientPrincipal =
                OAuth2AuthenticationProviderUtils.getAuthenticatedClientElseThrowInvalidClient(receivedToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        // Ensure the client is configured to use this authorization grant type
        if (registeredClient == null || !registeredClient.getAuthorizationGrantTypes().contains(receivedToken.getAuthorizationGrantType())) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNSUPPORTED_RESPONSE_TYPE);
        }

        if (log.isTraceEnabled()) {
            log.trace("Retrieved registered client");
        }

        // 验证scopes
        Set<String> authorizedScopes = this.getAuthorizedScopes(registeredClient, receivedToken.getScopes());

        // 验证用户认证信息
        Authentication principal = this.authenticateAuthenticationToken(receivedToken);

        // 以下内容摘抄自OAuth2AuthorizationCodeAuthenticationProvider
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(principal)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(receivedToken.getAuthorizationGrantType())
                .authorizationGrant(receivedToken);

        // Initialize the OAuth2Authorization
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                // 存入授权scope
                .authorizedScopes(authorizedScopes)
                // 当前授权用户名称
                .principalName(principal.getName())
                // 设置当前用户认证信息
                .attribute(Principal.class.getName(), principal)
                .authorizationGrantType(receivedToken.getAuthorizationGrantType());

        // 生成 access token
        OAuth2AccessToken accessToken = this.standardOAuth2TokenGenerator.generateAccessToken(tokenContextBuilder, authorizationBuilder);

        // 生成 refresh token
        OAuth2RefreshToken refreshToken = this.standardOAuth2TokenGenerator.generateRefreshToken(tokenContextBuilder, authorizationBuilder, registeredClient);

        // 生成oidcIdToken
        OidcIdToken oidcIdToken = this.standardOAuth2TokenGenerator.generateOidcIdToken(tokenContextBuilder, authorizationBuilder, receivedToken.getScopes(), principal);

        OAuth2Authorization authorization = authorizationBuilder.build();

        // Save the OAuth2Authorization
        this.authorizationService.save(authorization);

        Map<String, Object> additionalParameters = new HashMap<>(1);
        if (oidcIdToken != null) {
            // 放入idToken
            additionalParameters.put(OidcParameterNames.ID_TOKEN, oidcIdToken.getTokenValue());
        }

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
    }
}
