package com.basic.framework.oauth2.authorization.server.grant.email;

import com.basic.framework.oauth2.authorization.server.core.AbstractOAuth2AuthenticationProvider;
import com.basic.framework.oauth2.authorization.server.core.AbstractOAuth2AuthenticationToken;
import com.basic.framework.oauth2.core.constant.BasicOAuth2ParameterNames;
import com.basic.framework.oauth2.authorization.server.login.email.EmailCaptchaLoginAuthenticationToken;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * 自定义grant_type之邮件模式 provider
 *
 * @author vains
 */
@Slf4j
@Setter
public class OAuth2EmailCaptchaAuthenticationProvider extends AbstractOAuth2AuthenticationProvider {

    private String emailParameter = BasicOAuth2ParameterNames.EMAIL;

    private String emailCaptchaParameter = BasicOAuth2ParameterNames.EMAIL_CAPTCHA;

    public OAuth2EmailCaptchaAuthenticationProvider(SessionRegistry sessionRegistry, OAuth2TokenGenerator<?> tokenGenerator, AuthenticationProvider authenticationProvider, OAuth2AuthorizationService authorizationService) {
        super(sessionRegistry, tokenGenerator, authenticationProvider, authorizationService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2EmailCaptchaAuthenticationToken receivedToken = (OAuth2EmailCaptchaAuthenticationToken) authentication;

        return super.authentication(receivedToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2EmailCaptchaAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected Authentication getUnauthenticatedToken(AbstractOAuth2AuthenticationToken authenticationToken) {
        Map<String, Object> additionalParameters = authenticationToken.getAdditionalParameters();
        // 获取账号密码
        Object email = additionalParameters.get(this.emailParameter);
        Object emailCaptcha = additionalParameters.get(this.emailCaptchaParameter);
        // 生成未认证的 UsernamePasswordAuthenticationToken
        return EmailCaptchaLoginAuthenticationToken.unauthenticated(email, emailCaptcha);
    }

}
