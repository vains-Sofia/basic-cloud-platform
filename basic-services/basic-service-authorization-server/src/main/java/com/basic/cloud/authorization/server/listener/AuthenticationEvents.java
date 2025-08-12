package com.basic.cloud.authorization.server.listener;

import com.basic.framework.oauth2.authorization.server.core.AbstractLoginAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEvents {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        if (!(success.getAuthentication() instanceof UsernamePasswordAuthenticationToken
                || success.getAuthentication() instanceof AbstractLoginAuthenticationToken
                || success.getAuthentication() instanceof OAuth2LoginAuthenticationToken)) {
            // 如果不是 UsernamePasswordAuthenticationToken 或 AbstractLoginAuthenticationToken 类型的认证事件，则不处理
            return;
        }
        log.info("Authentication Success Event");
        System.out.println("Authentication Success Event: " + success.getAuthentication());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failure) {
        if (!(failure.getAuthentication() instanceof UsernamePasswordAuthenticationToken
                || failure.getAuthentication() instanceof AbstractLoginAuthenticationToken)
            || failure.getException() instanceof ProviderNotFoundException) {
            // 如果不是 UsernamePasswordAuthenticationToken 或 AbstractLoginAuthenticationToken 类型的认证事件，则不处理
            return;
        }
        // ...
        log.info("Authentication Failure Event");
        System.out.println("Authentication Failure Event: " + failure.getAuthentication().getName() + ", Reason: " + failure.getException().getMessage());
    }
}