package com.basic.framework.oauth2.core.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthenticatedReactiveAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

/**
 * 针对请求的自定义认证、鉴权处理(webflux)
 *  TODO 待完善鉴权
 *
 * @author vains
 */
@RequiredArgsConstructor
public class ReactiveContextAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        return AuthenticatedReactiveAuthorizationManager.authenticated().check(authentication, context);
    }
}
