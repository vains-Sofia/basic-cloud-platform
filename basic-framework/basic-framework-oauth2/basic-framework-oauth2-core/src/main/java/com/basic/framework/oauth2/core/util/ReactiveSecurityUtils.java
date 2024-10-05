package com.basic.framework.oauth2.core.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrorCodes;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.basic.framework.oauth2.core.util.SecurityUtils.computeWwwAuthenticateHeaderValue;

/**
 * webflux认证相关工具方法
 *
 * @author vains
 */
@UtilityClass
public class ReactiveSecurityUtils {

    /**
     * 鉴权失败回调
     *
     * @param exchange 请求、响应对象，包含请求与响应
     * @param e        具体的异常信息
     */
    public static Mono<Void> accessDeniedHandler(ServerWebExchange exchange, AccessDeniedException e) {
        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put("message", e.getMessage());
        return exchange.getPrincipal()
                .filter(AbstractOAuth2TokenAuthenticationToken.class::isInstance)
                .map((token) -> errorMessageParameters(parameters))
                .switchIfEmpty(Mono.just(parameters))
                .flatMap((params) -> respond(exchange, params));
    }

    /**
     * 认证失败回调
     *
     * @param exchange 请求、响应对象，包含请求与响应
     * @param e        具体的异常信息
     */
    public static Mono<Void> authenticationEntryPoint(ServerWebExchange exchange, AuthenticationException e) {
        return Mono.defer(() -> {
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            if (e instanceof OAuth2AuthenticationException authException) {
                if (authException.getError() instanceof BearerTokenError bearerTokenError) {
                    status = bearerTokenError.getHttpStatus();
                }
            }
            Map<String, String> parameters = createParameters(exchange, e);
            String wwwAuthenticate = computeWwwAuthenticateHeaderValue(parameters);
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().set(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
            response.setStatusCode(status);
            return ReactiveUtils.renderJson(response, parameters);
        });
    }

    /**
     * 组转权限不足异常
     *
     * @param parameters 异常信息map
     * @return 异常信息map
     */
    private static Map<String, String> errorMessageParameters(Map<String, String> parameters) {
        parameters.put("error", BearerTokenErrorCodes.INSUFFICIENT_SCOPE);
        parameters.put("error_description",
                "The request requires higher privileges than provided by the access token.");
        parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");
        return parameters;
    }

    /**
     * 将异常信息放入请求头并响应json
     *
     * @param exchange   请求、响应对象
     * @param parameters 异常信息
     * @return Mono<Void>
     */
    private static Mono<Void> respond(ServerWebExchange exchange, Map<String, String> parameters) {
        ServerHttpResponse response = exchange.getResponse();
        String wwwAuthenticate = computeWwwAuthenticateHeaderValue(parameters);
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().set(HttpHeaders.WWW_AUTHENTICATE, wwwAuthenticate);
        return ReactiveUtils.renderJson(response, parameters);
    }

    /**
     * 获取认证异常信息
     *
     * @param authException 认证异常
     * @return 认证异常信息
     */
    private Map<String, String> createParameters(ServerWebExchange exchange, AuthenticationException authException) {
        Map<String, String> parameters = new LinkedHashMap<>();
        if (authException instanceof OAuth2AuthenticationException authenticationException) {
            parameters.put("message", authException.getMessage());
            OAuth2Error error = authenticationException.getError();
            parameters.put("error", error.getErrorCode());
            if (StringUtils.hasText(error.getDescription())) {
                parameters.put("error_description", error.getDescription());
            }
            if (StringUtils.hasText(error.getUri())) {
                parameters.put("error_uri", error.getUri());
            }
            if (error instanceof BearerTokenError bearerTokenError) {
                if (StringUtils.hasText(bearerTokenError.getScope())) {
                    parameters.put("scope", bearerTokenError.getScope());
                }
            }
        }
        if (authException.getCause() instanceof InsufficientAuthenticationException insufficientAuthenticationException) {
            parameters.put("message", authException.getMessage());
            // 没有携带jwt访问接口，没有客户端认证信息
            parameters.put("error", BearerTokenErrorCodes.INVALID_TOKEN);
            parameters.put("error_description", insufficientAuthenticationException.getMessage());
            parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        }
        return parameters;
    }


}
