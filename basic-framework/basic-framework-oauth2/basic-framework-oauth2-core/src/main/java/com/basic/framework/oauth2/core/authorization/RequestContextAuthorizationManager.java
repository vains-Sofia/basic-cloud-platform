package com.basic.framework.oauth2.core.authorization;

import com.basic.framework.oauth2.core.enums.PermissionTypeEnum;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.security.BasicGrantedAuthority;
import com.basic.framework.oauth2.core.property.ResourceServerProperties;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Supplier;

/**
 * 针对请求的自定义认证、鉴权处理
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class RequestContextAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final ResourceServerProperties resourceServer;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final RedisOperator<Map<String, List<BasicGrantedAuthority>>> redisOperator;

    /**
     * check方法已被弃用，后续版本会被删除，新版本中请使用authorize
     *
     * @param authentication the {@link Supplier} of the {@link Authentication} to
     *                       authorize
     * @param requestContext the {@link RequestAuthorizationContext} object to authorize
     * @return AuthorizationDecision，属性granted为true验证通过，否则验证失败
     */
    @Override
    @Deprecated
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
        AuthorizationResult authorize = this.authorize(authentication, requestContext);
        return new AuthorizationDecision(authorize == null ? Boolean.FALSE : authorize.isGranted());
    }

    @Override
    public AuthorizationResult authorize(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {
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

        // 配置文件忽略认证
        Set<String> ignoreUriPaths = resourceServer.getIgnoreUriPaths();
        if (!ObjectUtils.isEmpty(ignoreUriPaths)) {
            for (String ignoreUriPath : ignoreUriPaths) {
                // 比较是否需要忽略鉴权
                if (pathMatcher.match(ignoreUriPath, requestPath)) {
                    log.debug("Ignoring authentication request URI: {}", requestPath);
                    // 忽略鉴权
                    return new AuthorizationDecision(Boolean.TRUE);
                }
            }
        }

        // 获取缓存中管理的权限信息，判断当前路径是否需要鉴权
        Map<String, List<BasicGrantedAuthority>> permissionsMap = redisOperator.get(AuthorizeConstants.ALL_PERMISSIONS);
        if (ObjectUtils.isEmpty(permissionsMap)) {
            // 默认检查是否认证过
            return AuthenticatedAuthorizationManager.authenticated().check(authentication, requestContext);
        }

        // 判断是有符合当前路径的权限，如果有说明需要鉴权，否则不用
        boolean pathNeedAuthorization = permissionsMap.values()
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(e ->
                        (request.getMethod().equalsIgnoreCase(e.getRequestMethod())
                                || ObjectUtils.isEmpty(e.getRequestMethod()))
                                && pathMatcher.match(e.getPath(), requestPath)
                );
        if (pathNeedAuthorization) {
            // 用户拥有的权限
            Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();
            if (!ObjectUtils.isEmpty(authorities)) {
                // 提取用户拥有权限中的关于请求路径部分权限
                List<BasicGrantedAuthority> grantedAuthorities = authorities.stream()
                        .filter(BasicGrantedAuthority.class::isInstance)
                        .map(BasicGrantedAuthority.class::cast)
                        // 过滤出接口权限
                        .filter(e -> Objects.equals(e.getPermissionType(), PermissionTypeEnum.REST))
                        .toList();
                for (BasicGrantedAuthority grantedAuthority : grantedAuthorities) {
                    // 请求方式和请求路径匹配放行
                    boolean urlMatch = pathMatcher.match(grantedAuthority.getPath(), requestPath)
                            && (request.getMethod().equalsIgnoreCase(grantedAuthority.getRequestMethod())
                            || ObjectUtils.isEmpty(grantedAuthority.getRequestMethod()));
                    if (urlMatch) {
                        log.debug("请求[{}]根据url鉴权通过.", requestPath);
                        return new AuthorizationDecision(Boolean.TRUE);
                    }
                }
                log.warn("请求[{}]失败，权限不足", requestPath);
                // 没有权限
                return new AuthorizationDecision(Boolean.FALSE);
            }
        }

        // 默认检查是否认证过
        return AuthenticatedAuthorizationManager.authenticated().check(authentication, requestContext);
    }
}
