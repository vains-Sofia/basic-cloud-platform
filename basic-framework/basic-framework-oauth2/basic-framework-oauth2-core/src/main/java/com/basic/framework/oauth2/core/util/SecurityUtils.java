package com.basic.framework.oauth2.core.util;

import com.basic.framework.core.enums.BasicEnum;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.basic.framework.oauth2.core.constant.AuthorizeConstants.AUTHORITIES;
import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.OAUTH2_ACCOUNT_PLATFORM;
import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.TOKEN_UNIQUE_ID;

/**
 * 安全帮助类
 *
 * @author vains
 */
@UtilityClass
public class SecurityUtils {

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (!authentication.isAuthenticated()) {
            return null;
        }
        if (authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser;
        }
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            Map<String, Object> claims = jwt.getClaims();
            // 提取账号来源
            String accountPlatformValue = (String) claims.get(OAUTH2_ACCOUNT_PLATFORM);
            // 提取权限信息
            Object authoritiesValue = claims.get(AUTHORITIES);
            // 获取用户id
            Object userId = claims.get(TOKEN_UNIQUE_ID);
            Set<SimpleGrantedAuthority> authorities = null;

            // 解析权限
            if (authoritiesValue instanceof String authorityStr) {
                authorities = Arrays.stream(authorityStr.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            }
            if (authoritiesValue instanceof Collection<?> authorityArr) {
                authorities = authorityArr.stream().map(String::valueOf).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            }
            OAuth2AccountPlatformEnum accountPlatformEnum = BasicEnum.fromValue(accountPlatformValue, OAuth2AccountPlatformEnum.class);

            // 构建统一信息
            DefaultAuthenticatedUser authenticatedUser = new DefaultAuthenticatedUser(authentication.getName(), accountPlatformEnum, authorities);
            authenticatedUser.setId(Long.valueOf(userId.toString()));
            return authenticatedUser;
        }
        return null;
    }

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    public static Long getUserId() {
        if (getAuthenticatedUser() != null) {
            return getAuthenticatedUser().getId();
        }
        return null;
    }

    /**
     * 认证与鉴权失败回调
     *
     * @param request  当前请求
     * @param response 当前响应
     * @param e        具体的异常信息
     */
    public static void exceptionHandler(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        Map<String, String> parameters = getErrorParameter(request, response, e);
        String wwwAuthenticate = computeWwwAuthenticateHeaderValue(parameters);
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, URLEncoder.encode(wwwAuthenticate, StandardCharsets.UTF_8));
        ServletUtils.renderJson(response, parameters);
    }

    /**
     * 获取异常信息map
     *
     * @param request  当前请求
     * @param response 当前响应
     * @param e        本次异常具体的异常实例
     * @return 异常信息map
     */
    public static Map<String, String> getErrorParameter(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        Map<String, String> parameters = new LinkedHashMap<>();
        if (request.getUserPrincipal() instanceof AbstractOAuth2TokenAuthenticationToken) {
            // 权限不足
            parameters.put("error", BearerTokenErrorCodes.INSUFFICIENT_SCOPE);
            parameters.put("error_description",
                    "The request requires higher privileges than provided by the access token.");
            parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
        if (e instanceof OAuth2AuthenticationException authenticationException) {
            // jwt异常，e.g. jwt超过有效期、jwt无效等
            OAuth2Error error = authenticationException.getError();
            parameters.put("error", error.getErrorCode());
            if (StringUtils.hasText(error.getUri())) {
                parameters.put("error_uri", error.getUri());
            }
            if (StringUtils.hasText(error.getDescription())) {
                parameters.put("error_description", error.getDescription());
            }
            if (error instanceof BearerTokenError bearerTokenError) {
                if (StringUtils.hasText(bearerTokenError.getScope())) {
                    parameters.put("scope", bearerTokenError.getScope());
                }
                response.setStatus(bearerTokenError.getHttpStatus().value());
            }
        }
        if (e instanceof InsufficientAuthenticationException) {
            // 没有携带jwt访问接口，没有客户端认证信息
            parameters.put("error", BearerTokenErrorCodes.INVALID_TOKEN);
            parameters.put("error_description", "Not authorized.");
            parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        parameters.put("message", e.getMessage());
        return parameters;
    }

    /**
     * 解析授权错误页面URI
     *
     * @param authorizeErrorUri 授权错误页面URI
     * @param errorParameters   错误参数
     * @return 完整的授权错误页面URI
     */
    public static String resolveAuthorizeErrorUri(String authorizeErrorUri, Map<String, String> errorParameters) {
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>(errorParameters.size());
        errorParameters.forEach((k, v) -> valueMap.add(k, URLEncoder.encode(v, StandardCharsets.UTF_8)));
        // 如果授权错误页面路径是绝对路径，则拼接参数后直接返回
        if (UrlUtils.isAbsoluteUrl(authorizeErrorUri)) {
            return UriComponentsBuilder
                    .fromUriString(authorizeErrorUri)
                    .replaceQueryParams(valueMap)
                    .encode(StandardCharsets.UTF_8)
                    .build(Boolean.TRUE).toUriString();
        }
        // 否则，拼接成完整的URI
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(authorizeErrorUri)
                .replaceQueryParams(valueMap)
                .build()
                .toUriString();
    }

    /**
     * 生成放入请求头的错误信息
     *
     * @param parameters 参数
     * @return 字符串
     */
    public static String computeWwwAuthenticateHeaderValue(Map<String, String> parameters) {
        StringBuilder wwwAuthenticate = new StringBuilder();
        wwwAuthenticate.append("Bearer");
        if (!parameters.isEmpty()) {
            wwwAuthenticate.append(" ");
            int i = 0;
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                wwwAuthenticate.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
                if (i != parameters.size() - 1) {
                    wwwAuthenticate.append(", ");
                }
                i++;
            }
        }
        return wwwAuthenticate.toString();
    }

}
