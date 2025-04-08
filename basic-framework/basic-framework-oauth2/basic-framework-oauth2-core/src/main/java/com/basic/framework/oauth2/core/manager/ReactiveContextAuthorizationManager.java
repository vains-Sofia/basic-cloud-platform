package com.basic.framework.oauth2.core.manager;

import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.security.BasicGrantedAuthority;
import com.basic.framework.oauth2.core.property.ResourceServerProperties;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthenticatedReactiveAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 针对请求的自定义认证、鉴权处理(webflux)
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class ReactiveContextAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final ResourceServerProperties resourceServer;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final RedisOperator<Map<String, List<BasicGrantedAuthority>>> redisOperator;

    private final AuthenticationTrustResolver authTrustResolver = new AuthenticationTrustResolverImpl();

    /**
     * check方法已被弃用，后续版本会被删除，新版本中请使用authorize
     *
     * @param authentication the {@link Supplier} of the {@link Authentication} to
     *                       authorize
     * @param context        the {@link AuthorizationContext} object to authorize
     * @return AuthorizationDecision，属性granted为true验证通过，否则验证失败
     */
    @Override
    @Deprecated
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        return this.authorize(authentication, context)
                .map(e -> new AuthorizationDecision(e.isGranted()));
    }

    public Mono<AuthorizationResult> authorize(Mono<Authentication> authentication, AuthorizationContext context) {
        // 内部调用忽略认证
        // 取出当前路径和ContextPath，如果有ContextPath则替换为空
        ServerHttpRequest request = context.getExchange().getRequest();
        List<String> ignoreHeaders = request.getHeaders().get(FeignConstants.IGNORE_AUTH_HEADER_KEY);
        if (!ObjectUtils.isEmpty(ignoreHeaders)) {
            // 如果有忽略认证请求头则忽略认证
            if (ignoreHeaders.contains(FeignConstants.IGNORE_AUTH_HEADER_VALUE)) {
                return Mono.just(new AuthorizationDecision(Boolean.TRUE));
            }
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

        // 配置文件忽略认证
        Set<String> ignoreUriPaths = resourceServer.getIgnoreUriPaths();
        if (!ObjectUtils.isEmpty(ignoreUriPaths)) {
            for (String ignoreUriPath : ignoreUriPaths) {
                // 比较是否需要忽略鉴权
                if (pathMatcher.match(ignoreUriPath, requestPath)) {
                    log.debug("Ignoring authentication request URI: {}", requestPath);
                    // 忽略鉴权
                    return Mono.just(new AuthorizationDecision(Boolean.TRUE));
                }
            }
        }

        // 获取缓存中管理的权限信息，判断当前路径是否需要鉴权
        Map<String, List<BasicGrantedAuthority>> permissionsMap = redisOperator.get(AuthorizeConstants.ALL_PERMISSIONS);
        if (ObjectUtils.isEmpty(permissionsMap)) {
            // 默认检查是否认证过
            return AuthenticatedReactiveAuthorizationManager.authenticated()
                    .check(authentication, context)
                    .cast(AuthorizationResult.class);
        }

        // 判断是有符合当前路径的权限，如果有说明需要鉴权，否则不用
        boolean pathNeedAuthorization = permissionsMap.values()
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(e ->
                        (request.getMethod().name().equalsIgnoreCase(e.getRequestMethod())
                                || ObjectUtils.isEmpty(e.getRequestMethod()))
                                && pathMatcher.match(e.getPath(), requestPath)
                );
        if (!pathNeedAuthorization) {
            // 当前请求不需要鉴权，只做认证
            return AuthenticatedReactiveAuthorizationManager.authenticated()
                    .check(authentication, context)
                    .cast(AuthorizationResult.class);
        }

        // 检查是否认证过(不为空、认证状态为true、不是匿名用户)
        return authentication.filter(authTrustResolver::isAuthenticated)
                .map(Authentication::getAuthorities)
                .filter(BasicGrantedAuthority.class::isInstance)
                .cast(BasicGrantedAuthority.class)
                // 筛选出需要鉴权的
                .filter(BasicGrantedAuthority::getNeedAuthentication)
                // 根据当前请求的请求方式和请求路径过滤
                .filter(grantedAuthority ->
                        pathMatcher.match(grantedAuthority.getPath(), requestPath)
                                && (request.getMethod().name().equalsIgnoreCase(grantedAuthority.getRequestMethod())
                                || ObjectUtils.isEmpty(grantedAuthority.getRequestMethod()))
                )
                .map(grantedAuthority -> {
                    // 过滤后有匹配请求方式和请求路径的权限时放行
                    log.debug("WebFlux请求[{}]鉴权通过.", requestPath);
                    return new AuthorizationDecision(Boolean.TRUE);
                })
                // 鉴权失败
                .defaultIfEmpty(new AuthorizationDecision(Boolean.FALSE))
                .cast(AuthorizationResult.class);
    }
}
