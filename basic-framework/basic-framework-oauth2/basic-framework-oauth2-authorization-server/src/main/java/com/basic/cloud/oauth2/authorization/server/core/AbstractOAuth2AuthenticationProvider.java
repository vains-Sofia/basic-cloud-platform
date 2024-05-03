package com.basic.cloud.oauth2.authorization.server.core;

import com.basic.cloud.oauth2.authorization.server.util.OAuth2AuthenticationProviderUtils;
import com.basic.cloud.oauth2.authorization.server.util.OAuth2EndpointUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.*;
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

    private final SessionRegistry sessionRegistry;

    private final OAuth2TokenGenerator<?> tokenGenerator;

    private final AuthenticationProvider authenticationProvider;

    private final OAuth2AuthorizationService authorizationService;

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

    /**
     * 根据oauth2认证信息生成access token，参考{@link org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider}中的实现
     *
     * @param tokenContextBuilder  保存与 OAuth 2.0 令牌关联的信息（待定）并由 {@link OAuth2TokenGenerator } 和 {@link OAuth2TokenCustomizer } 使用的上下文。
     * @param authorizationBuilder OAuth 2.0 授权的表示形式，它保存与 resource owner 或其自身(在client_credentials授予类型的情况下)授予客户端的授权相关的状态。
     * @return access token
     */
    protected OAuth2AccessToken generateAccessToken(DefaultOAuth2TokenContext.Builder tokenContextBuilder, OAuth2Authorization.Builder authorizationBuilder) {
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        if (log.isTraceEnabled()) {
            log.trace("Generated access token");
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor claimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }
        return accessToken;
    }


    /**
     * 根据oauth2认证信息生成refresh token，参考{@link org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider}中的实现
     *
     * @param tokenContextBuilder  保存与 OAuth 2.0 令牌关联的信息（待定）并由 {@link OAuth2TokenGenerator } 和 {@link OAuth2TokenCustomizer } 使用的上下文。
     * @param authorizationBuilder OAuth 2.0 授权的表示形式，它保存与 resource owner 或其自身(在client_credentials授予类型的情况下)授予客户端的授权相关的状态。
     * @param registeredClient     oauth2认证中使用的客户端信息
     * @return refresh token
     */
    protected OAuth2RefreshToken generateRefreshToken(DefaultOAuth2TokenContext.Builder tokenContextBuilder, OAuth2Authorization.Builder authorizationBuilder, RegisteredClient registeredClient) {
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (generatedRefreshToken != null) {
                if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                            "The token generator failed to generate a valid refresh token.", ERROR_URI);
                    throw new OAuth2AuthenticationException(error);
                }

                if (log.isTraceEnabled()) {
                    log.trace("Generated refresh token");
                }

                refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
                authorizationBuilder.refreshToken(refreshToken);
            }
        }
        return refreshToken;
    }

    /**
     * 生成 Openid Connection 的Id Token
     *
     * @param tokenContextBuilder  保存与 OAuth 2.0 令牌关联的信息（待定）并由 {@link OAuth2TokenGenerator } 和 {@link OAuth2TokenCustomizer } 使用的上下文。
     * @param authorizationBuilder OAuth 2.0 授权的表示形式，它保存与 resource owner 或其自身(在client_credentials授予类型的情况下)授予客户端的授权相关的状态。
     * @param requestedScopes      当前请求的scopes
     * @param principal            当前oauth2的认证用户信息
     * @return Id Token
     */
    protected OidcIdToken generateOidcIdToken(DefaultOAuth2TokenContext.Builder tokenContextBuilder, OAuth2Authorization.Builder authorizationBuilder, Set<String> requestedScopes, Authentication principal) {
        OidcIdToken idToken;
        if (requestedScopes.contains(OidcScopes.OPENID)) {
            SessionInformation sessionInformation = getSessionInformation(principal);
            if (sessionInformation != null) {
                try {
                    // Compute (and use) hash for Session ID
                    sessionInformation = new SessionInformation(sessionInformation.getPrincipal(),
                            createHash(sessionInformation.getSessionId()), sessionInformation.getLastRequest());
                } catch (NoSuchAlgorithmException ex) {
                    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                            "Failed to compute hash for Session ID.", ERROR_URI);
                    throw new OAuth2AuthenticationException(error);
                }
                tokenContextBuilder.put(SessionInformation.class, sessionInformation);
            }

            OAuth2TokenContext tokenContext = tokenContextBuilder
                    .tokenType(ID_TOKEN_TOKEN_TYPE)
                    // ID token customizer may need access to the access token and/or refresh token
                    .authorization(authorizationBuilder.build())
                    .build();

            OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedIdToken instanceof Jwt)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the ID token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            if (log.isTraceEnabled()) {
                log.trace("Generated id token");
            }

            idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
                    generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
            authorizationBuilder.token(idToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
        } else {
            idToken = null;
        }
        return idToken;
    }

    /**
     * 获取认证过的scope
     *
     * @param registeredClient 客户端
     * @param requestedScopes  请求中的scope
     * @return 认证过的scope
     */
    protected Set<String> getAuthorizedScopes(RegisteredClient registeredClient, Set<String> requestedScopes) {
        // Default to configured scopes
        Set<String> authorizedScopes = registeredClient.getScopes();
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
        // 交给 userDetailsAuthenticationProvider 进行认证
        return authenticationProvider.authenticate(unauthenticated);
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
        OAuth2AccessToken accessToken = this.generateAccessToken(tokenContextBuilder, authorizationBuilder);

        // 生成 refresh token
        OAuth2RefreshToken refreshToken = this.generateRefreshToken(tokenContextBuilder, authorizationBuilder, registeredClient);

        // 生成oidcIdToken
        OidcIdToken oidcIdToken = this.generateOidcIdToken(tokenContextBuilder, authorizationBuilder, receivedToken.getScopes(), principal);

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

    /**
     * 从认证用户信息中获取 session信息
     *
     * @param principal 认证用户信息
     * @return session信息
     */
    private SessionInformation getSessionInformation(Authentication principal) {
        SessionInformation sessionInformation = null;
        if (this.sessionRegistry != null) {
            List<SessionInformation> sessions = this.sessionRegistry.getAllSessions(principal.getPrincipal(), false);
            if (!CollectionUtils.isEmpty(sessions)) {
                sessionInformation = sessions.getFirst();
                if (sessions.size() > 1) {
                    // Get the most recent session
                    sessions = new ArrayList<>(sessions);
                    sessions.sort(Comparator.comparing(SessionInformation::getLastRequest));
                    sessionInformation = sessions.getLast();
                }
            }
        }
        return sessionInformation;
    }

    /**
     * 对传入数据进行 SHA-256 加密
     *
     * @param value 需加密内容
     * @return 加密后的数据
     * @throws NoSuchAlgorithmException 当加密算法在特定环境中不可用时抛出
     */
    private static String createHash(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
