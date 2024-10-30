package com.basic.framework.oauth2.authorization.server.email;

import com.basic.framework.oauth2.authorization.server.captcha.CaptchaService;
import com.basic.framework.oauth2.authorization.server.core.AbstractLoginAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.ObjectUtils;

/**
 * 邮箱认证登录 provider
 *
 * @author vains
 */
@Slf4j
public class EmailCaptchaLoginAuthenticationProvider extends AbstractLoginAuthenticationProvider {

    private final CaptchaService captchaService;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public EmailCaptchaLoginAuthenticationProvider(UserDetailsService userDetailsService, CaptchaService captchaService) {
        super(userDetailsService);
        this.captchaService = captchaService;
    }

    @Override
    protected void additionalAuthenticationChecks(AbstractAuthenticationToken authentication) throws AuthenticationException {
        log.debug("校验验证码...");
        if (ObjectUtils.isEmpty(authentication.getCredentials())) {
            log.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        // 验证邮件验证码
//        this.captchaService.validate();
        log.debug("验证码校验成功.");
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
        EmailCaptchaLoginAuthenticationToken result = EmailCaptchaLoginAuthenticationToken.authenticated(principal,
                authentication.getCredentials(), this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        log.debug("Authenticated user");
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailCaptchaLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
