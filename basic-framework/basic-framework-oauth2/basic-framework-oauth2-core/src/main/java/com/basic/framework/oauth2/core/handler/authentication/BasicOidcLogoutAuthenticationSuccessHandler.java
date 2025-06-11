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
 * OIDC登出认证成功处理器。
 * 处理OIDC登出请求，执行登出操作，删除用户信息缓存，并根据情况进行重定向或返回JSON响应。
 * <p>
 * 该类实现了AuthenticationSuccessHandler接口，用于在OIDC登出认证成功后执行特定操作。
 * </p>
 * 参考{@link OidcLogoutAuthenticationSuccessHandler}实现。
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
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OidcLogoutAuthenticationToken oidcLogoutAuthentication = validateAuthentication(authentication);

        // 执行登出操作
        this.logoutHandler.logout(request, response, authentication);

        // 删除Redis中缓存的用户信息
        deleteUserInfoFromCache(oidcLogoutAuthentication);

        // 处理重定向或返回JSON响应
        handleLogoutResponse(request, response, oidcLogoutAuthentication);
    }

    /**
     * 验证传入的Authentication对象是否为OidcLogoutAuthenticationToken类型。
     * 如果不是，则抛出OAuth2AuthenticationException异常。
     *
     * @param authentication Authentication对象
     * @return OidcLogoutAuthenticationToken类型的认证对象
     */
    private OidcLogoutAuthenticationToken validateAuthentication(Authentication authentication) {
        if (!(authentication instanceof OidcLogoutAuthenticationToken oidcLogoutAuthentication)) {
            if (log.isErrorEnabled()) {
                log.error("{} must be of type {} but was {}",
                        Authentication.class.getSimpleName(),
                        OidcLogoutAuthenticationToken.class.getName(),
                        authentication.getClass().getName());
            }
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "Unable to process the OpenID Connect 1.0 RP-Initiated Logout response.", null);
            throw new OAuth2AuthenticationException(error);
        }
        return oidcLogoutAuthentication;
    }

    /**
     * 根据OIDC登出认证信息处理登出响应。
     * 如果提供了post_logout_redirect_uri，则重定向到该URI，否则返回JSON响应。
     *
     * @param request                  HttpServletRequest对象
     * @param response                 HttpServletResponse对象
     * @param oidcLogoutAuthentication OidcLogoutAuthenticationToken对象
     * @throws IOException 如果发生IO异常
     */
    private void handleLogoutResponse(HttpServletRequest request, HttpServletResponse response,
                                      OidcLogoutAuthenticationToken oidcLogoutAuthentication) throws IOException {
        if (StringUtils.hasText(oidcLogoutAuthentication.getPostLogoutRedirectUri())) {
            sendLogoutRedirect(request, response, oidcLogoutAuthentication);
        } else {
            sendJsonResponse(response);
        }
    }

    /**
     * 发送JSON响应，表示OIDC登出成功。
     * 如果没有提供post_logout_redirect_uri，则返回一个包含成功状态的JSON响应。
     *
     * @param response HttpServletResponse对象
     */
    private void sendJsonResponse(HttpServletResponse response) {
        Result<String> success = Result.success();
        ServletUtils.renderJson(response, success);
        if (log.isDebugEnabled()) {
            log.debug("OIDC logout successful, no post_logout_redirect_uri provided, returning JSON response.");
        }
    }

    /**
     * 执行OIDC登出操作。
     * 如果用户会话处于活动状态，则调用SecurityContextLogoutHandler进行登出。
     *
     * @param request        HttpServletRequest对象
     * @param response       HttpServletResponse对象
     * @param authentication OidcLogoutAuthenticationToken对象
     */
    private void performLogout(HttpServletRequest request, HttpServletResponse response,
                               Authentication authentication) {
        OidcLogoutAuthenticationToken oidcLogoutAuthentication = (OidcLogoutAuthenticationToken) authentication;

        // Check for active user session
        if (oidcLogoutAuthentication.isPrincipalAuthenticated()) {
            this.securityContextLogoutHandler.logout(request, response,
                    (Authentication) oidcLogoutAuthentication.getPrincipal());
        }
    }

    /**
     * 从Redis缓存中删除用户信息。
     * 使用用户ID作为键，删除对应的用户信息缓存。
     * 如果删除操作失败，将记录错误日志。
     *
     * @param oidcLogoutAuthentication OidcLogoutAuthenticationToken对象
     */
    private void deleteUserInfoFromCache(OidcLogoutAuthenticationToken oidcLogoutAuthentication) {
        if (!(oidcLogoutAuthentication.getPrincipal() instanceof AbstractAuthenticationToken authToken)) {
            return;
        }

        if (!(authToken.getPrincipal() instanceof AuthenticatedUser authenticatedUser)) {
            if (log.isWarnEnabled()) {
                log.warn("Authorization principal is not an instance of AuthenticatedUser: {}",
                        authToken.getPrincipal());
            }
            return;
        }

        try {
            String userinfoCacheKey = AuthorizeConstants.USERINFO_PREFIX + authenticatedUser.getId();
            redisOperator.delete(userinfoCacheKey);
            if (log.isDebugEnabled()) {
                log.debug("Successfully deleted user cache for user: {}", authenticatedUser.getId());
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to delete user information from Redis during OIDC logout", e);
            }
        }
    }

    /**
     * 根据OIDC登出认证信息发送重定向。
     * 如果提供了post_logout_redirect_uri参数，则重定向到该URI，否则默认重定向到根路径。
     *
     * @param request                  HttpServletRequest对象
     * @param response                 HttpServletResponse对象
     * @param oidcLogoutAuthentication OidcLogoutAuthenticationToken对象
     * @throws IOException 如果发生IO异常
     */
    private void sendLogoutRedirect(HttpServletRequest request, HttpServletResponse response,
                                    OidcLogoutAuthenticationToken oidcLogoutAuthentication) throws IOException {
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