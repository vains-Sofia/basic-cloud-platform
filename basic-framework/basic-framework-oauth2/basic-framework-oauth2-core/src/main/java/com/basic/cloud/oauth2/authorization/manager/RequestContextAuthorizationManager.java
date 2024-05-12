package com.basic.cloud.oauth2.authorization.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

/**
 * 针对请求的自定义认证、鉴权处理
 *  TODO 待完善鉴权
 *
 * @author vains
 */
@RequiredArgsConstructor
public class RequestContextAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {

        // 默认检查是否认证
        return AuthenticatedAuthorizationManager.authenticated().check(authentication, requestContext);
    }
}
