package com.basic.framework.oauth2.authorization.server.grant.email;

import com.basic.framework.oauth2.authorization.server.core.AbstractOAuth2AuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * 自定义grant_type之邮件模式 token
 *
 * @author vains
 */
public class OAuth2EmailCaptchaAuthenticationToken extends AbstractOAuth2AuthenticationToken {

    public OAuth2EmailCaptchaAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }
}
