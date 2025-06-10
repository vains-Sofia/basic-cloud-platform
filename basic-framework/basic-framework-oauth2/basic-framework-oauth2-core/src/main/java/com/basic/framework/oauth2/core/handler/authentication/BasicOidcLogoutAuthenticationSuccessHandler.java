package com.basic.framework.oauth2.core.handler.authentication;

import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.util.ServletUtils;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.oidc.web.authentication.OidcLogoutAuthenticationSuccessHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 提供给当前登录用户登出时使用，前端获取的access token一定会有id_token.
 * OIDC登出自定义处理，删除Redis中缓存的用户信息，并根据url中的post_logout_redirect_uri参数决定是跳转还是响应json。
 * 该处理器在OIDC登出时被调用。参考{@link OidcLogoutAuthenticationSuccessHandler}实现。
 *
 * @author vains
 * @see OidcLogoutAuthenticationSuccessHandler
 */
@Slf4j
@RequiredArgsConstructor
public class BasicOidcLogoutAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final RedisOperator<AuthenticatedUser> redisOperator;

    private final LogoutHandler logoutHandler = this::performLogout;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (!(authentication instanceof OidcLogoutAuthenticationToken oidcLogoutAuthentication)) {
            if (log.isErrorEnabled()) {
                log.error("{} must be of type {} but was {}", Authentication.class.getSimpleName(), OidcLogoutAuthenticationToken.class.getName(), authentication.getClass().getName());
            }
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "Unable to process the OpenID Connect 1.0 RP-Initiated Logout response.", null);
            throw new OAuth2AuthenticationException(error);
        }

        this.logoutHandler.logout(request, response, authentication);

        try {
            // 删除Redis中缓存的用户信息
            if (oidcLogoutAuthentication.getPrincipal() instanceof AbstractAuthenticationToken authenticationToken) {
                // 当前认证信息是AbstractAuthenticationToken类型
                if (authenticationToken.getPrincipal() instanceof AuthenticatedUser authenticatedUser) {
                    // 删除用户信息缓存
                    String userinfoCacheKey = AuthorizeConstants.USERINFO_PREFIX + authenticatedUser.getId();
                    redisOperator.delete(userinfoCacheKey);
                } else {
                    // 当前认证信息不是AuthenticatedUser类型，不做特殊处理
                    if (log.isWarnEnabled()) {
                        log.warn("Authorization principal is not an instance of AuthenticatedUser: {}", authenticationToken.getPrincipal());
                    }
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to delete user information from Redis during OIDC logout", e);
            }
        }

        if (StringUtils.hasText(oidcLogoutAuthentication.getPostLogoutRedirectUri())) {
            // 如果 post_logout_redirect_uri 存在，则重定向到该 URI
            sendLogoutRedirect(request, response, authentication);
            return;
        }
        // 如果 post_logout_redirect_uri 不存在，则返回 JSON 响应
        Result<String> success = Result.success();
        ServletUtils.renderJson(response, success);
        if (log.isDebugEnabled()) {
            log.debug("OIDC logout successful, no post_logout_redirect_uri provided, returning JSON response.");
        }
    }

    private void performLogout(HttpServletRequest request, HttpServletResponse response,
                               Authentication authentication) {
        OidcLogoutAuthenticationToken oidcLogoutAuthentication = (OidcLogoutAuthenticationToken) authentication;

        // Check for active user session
        if (oidcLogoutAuthentication.isPrincipalAuthenticated()) {
            this.securityContextLogoutHandler.logout(request, response,
                    (Authentication) oidcLogoutAuthentication.getPrincipal());
        }
    }

    private void sendLogoutRedirect(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException {
        OidcLogoutAuthenticationToken oidcLogoutAuthentication = (OidcLogoutAuthenticationToken) authentication;

        String redirectUri = "/";
        if (oidcLogoutAuthentication.isAuthenticated()
                && StringUtils.hasText(oidcLogoutAuthentication.getPostLogoutRedirectUri())) {
            // Use the `post_logout_redirect_uri` parameter
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUriString(oidcLogoutAuthentication.getPostLogoutRedirectUri());
            if (StringUtils.hasText(oidcLogoutAuthentication.getState())) {
                uriBuilder.queryParam(OAuth2ParameterNames.STATE,
                        UriUtils.encode(oidcLogoutAuthentication.getState(), StandardCharsets.UTF_8));
            }
            // build(true) -> Components are explicitly encoded
            redirectUri = uriBuilder.build(true).toUriString();
        }
        this.redirectStrategy.sendRedirect(request, response, redirectUri);
    }

}
