package com.basic.framework.oauth2.authorization.server.grant.password;

import com.basic.framework.oauth2.authorization.server.core.AbstractOAuth2AuthenticationProvider;
import com.basic.framework.oauth2.authorization.server.core.AbstractOAuth2AuthenticationToken;
import com.basic.framework.oauth2.core.token.generator.StandardOAuth2TokenGenerator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

import java.util.Map;

/**
 * 自定义grant_type之密码模式 provider
 *
 * @author vains
 */
@Slf4j
@Setter
public class OAuth2PasswordAuthenticationProvider extends AbstractOAuth2AuthenticationProvider {

    private String usernameParameter = OAuth2ParameterNames.USERNAME;

    private String passwordParameter = OAuth2ParameterNames.PASSWORD;

    public OAuth2PasswordAuthenticationProvider(AuthenticationProvider authenticationProvider, OAuth2AuthorizationService authorizationService, StandardOAuth2TokenGenerator standardOAuth2TokenGenerator) {
        super(authenticationProvider, authorizationService, standardOAuth2TokenGenerator);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2PasswordAuthenticationToken receivedToken =
                (OAuth2PasswordAuthenticationToken) authentication;

        return super.authentication(receivedToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected Authentication getUnauthenticatedToken(AbstractOAuth2AuthenticationToken authenticationToken) {
        Map<String, Object> additionalParameters = authenticationToken.getAdditionalParameters();
        // 获取账号密码
        Object username = additionalParameters.get(this.usernameParameter);
        Object password = additionalParameters.get(this.passwordParameter);
        // 生成未认证的 UsernamePasswordAuthenticationToken
        return UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    }
}
