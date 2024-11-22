package com.basic.framework.oauth2.core.manager;

import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.oauth2.core.property.ResourceServerProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.ObjectUtils;

import java.util.Enumeration;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 针对请求的自定义认证、鉴权处理
 *  TODO 待完善鉴权
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class RequestContextAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final ResourceServerProperties resourceServer;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
        // 取出当前路径和ContextPath，如果有ContextPath则替换为空
        HttpServletRequest request = requestContext.getRequest();
        Enumeration<String> ignoreHeaders = request.getHeaders(FeignConstants.IGNORE_AUTH_HEADER_KEY);
        if (!ObjectUtils.isEmpty(ignoreHeaders)) {
            // 如果有忽略认证请求头则忽略认证
            while (ignoreHeaders.hasMoreElements()) {
                String ignoreHeader = ignoreHeaders.nextElement();
                if (Objects.equals(FeignConstants.IGNORE_AUTH_HEADER_VALUE, ignoreHeader)) {
                    // 忽略鉴权
                    return new AuthorizationDecision(Boolean.TRUE);
                }
            }
        }

        // 配置文件忽略认证
        Set<String> ignoreUriPaths = resourceServer.getIgnoreUriPaths();
        if (ObjectUtils.isEmpty(ignoreUriPaths)) {
            // 默认检查是否认证过
            return AuthenticatedAuthorizationManager.authenticated().check(authentication, requestContext);
        }

        // 取出当前路径和ContextPath，如果有ContextPath则替换为空
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        // 替换ContextPath
        String requestPath;
        if (!ObjectUtils.isEmpty(contextPath)) {
            requestPath = requestURI.replaceFirst(contextPath, "");
        } else {
            requestPath = requestURI;
        }

        // 比较是否需要忽略鉴权
        boolean needIgnore = ignoreUriPaths.contains(requestPath);
        if (needIgnore) {
            log.debug("Ignoring authentication request URI: {}", requestPath);
            // 忽略鉴权
            return new AuthorizationDecision(Boolean.TRUE);
        }
        // 默认检查是否认证过
        return AuthenticatedAuthorizationManager.authenticated().check(authentication, requestContext);
    }
}
