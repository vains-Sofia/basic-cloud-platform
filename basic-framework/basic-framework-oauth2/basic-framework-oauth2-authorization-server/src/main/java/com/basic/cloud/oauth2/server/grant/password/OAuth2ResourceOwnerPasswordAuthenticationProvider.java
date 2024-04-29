package com.basic.cloud.oauth2.server.grant.password;

import com.basic.cloud.oauth2.server.core.AbstractAuthenticationProvider;
import com.basic.cloud.oauth2.server.util.OAuth2AuthenticationProviderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 自定义grant_type之密码模式 provider
 *
 * @author vains
 */
@Slf4j
public class OAuth2ResourceOwnerPasswordAuthenticationProvider extends AbstractAuthenticationProvider {

    private final OAuth2AuthorizationService authorizationService;

    public OAuth2ResourceOwnerPasswordAuthenticationProvider(SessionRegistry sessionRegistry, OAuth2TokenGenerator<?> tokenGenerator, OAuth2AuthorizationService authorizationService, AbstractUserDetailsAuthenticationProvider userDetailsAuthenticationProvider) {
        super(sessionRegistry, tokenGenerator, userDetailsAuthenticationProvider);
        this.authorizationService = authorizationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2ResourceOwnerPasswordAuthenticationToken receivedToken =
                (OAuth2ResourceOwnerPasswordAuthenticationToken) authentication;

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
        Set<String> authorizedScopes = super.getAuthorizedScopes(registeredClient, receivedToken.getScopes());

        // 验证用户认证信息
        Authentication principal = super.authenticateAuthenticationToken(receivedToken);

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
        OAuth2AccessToken accessToken = super.generateAccessToken(tokenContextBuilder, authorizationBuilder);

        // 生成 refresh token
        OAuth2RefreshToken refreshToken = super.generateRefreshToken(tokenContextBuilder, authorizationBuilder, registeredClient);

        // 生成oidcIdToken
        OidcIdToken oidcIdToken = super.generateOidcIdToken(tokenContextBuilder, authorizationBuilder, receivedToken.getScopes(), principal);

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

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2ResourceOwnerPasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
