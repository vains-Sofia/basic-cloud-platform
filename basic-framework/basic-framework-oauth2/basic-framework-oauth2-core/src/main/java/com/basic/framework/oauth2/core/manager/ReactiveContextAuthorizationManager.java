package com.basic.framework.oauth2.core.manager;

import com.basic.framework.core.constants.BasicConstants;
import com.basic.framework.oauth2.core.property.ResourceServerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthenticatedReactiveAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * 针对请求的自定义认证、鉴权处理(webflux)
 *  TODO 待完善鉴权
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class ReactiveContextAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final ResourceServerProperties resourceServer;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        // 内部调用忽略认证
        // 取出当前路径和ContextPath，如果有ContextPath则替换为空
        ServerHttpRequest request = context.getExchange().getRequest();
        List<String> ignoreHeaders = request.getHeaders().get(BasicConstants.IGNORE_AUTH_HEADER_KEY);
        if (!ObjectUtils.isEmpty(ignoreHeaders)) {
            // 如果有忽略认证请求头则忽略认证
            if (ignoreHeaders.contains(BasicConstants.IGNORE_AUTH_HEADER_VALUE)) {
                return Mono.just(new AuthorizationDecision(Boolean.TRUE));
            }
        }

        // 配置文件忽略认证
        Set<String> ignoreUriPaths = resourceServer.getIgnoreUriPaths();
        if (ObjectUtils.isEmpty(ignoreUriPaths)) {
            // 默认检查是否认证过
            return AuthenticatedReactiveAuthorizationManager.authenticated().check(authentication, context);
        }

        // 取出当前路径和ContextPath，如果有ContextPath则替换为空
        String path = request.getURI().getPath();
        String contextPath = request.getPath().contextPath().value();

        // 替换ContextPath
        String requestPath;
        if (!ObjectUtils.isEmpty(contextPath)) {
            requestPath = path.replaceFirst(contextPath, "");
        } else {
            requestPath = path;
        }

        // 比较是否需要忽略鉴权
        boolean needIgnore = ignoreUriPaths.contains(requestPath);
        if (needIgnore) {
            log.debug("Ignoring authentication request URI: {}", requestPath);
            // 忽略鉴权
            return Mono.just(new AuthorizationDecision(Boolean.TRUE));
        }

        // 默认检查是否认证过
        return AuthenticatedReactiveAuthorizationManager.authenticated().check(authentication, context);
    }
}
