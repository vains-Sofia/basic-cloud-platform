package com.basic.framework.oauth2.authorization.server.email;

import com.basic.framework.oauth2.authorization.server.core.AbstractLoginAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 邮件认证登录 token
 *
 * @author vains
 */
public class EmailCaptchaLoginAuthenticationToken extends AbstractLoginAuthenticationToken {

    public EmailCaptchaLoginAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public EmailCaptchaLoginAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    /**
     * This factory method can be safely used by any code that wishes to create a
     * unauthenticated <code>UsernamePasswordAuthenticationToken</code>.
     *
     * @param principal   邮箱地址
     * @param credentials 验证码
     * @return UsernamePasswordAuthenticationToken with false isAuthenticated() result
     * @since 5.7
     */
    public static EmailCaptchaLoginAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new EmailCaptchaLoginAuthenticationToken(principal, credentials);
    }

    /**
     * This factory method can be safely used by any code that wishes to create a
     * authenticated <code>UsernamePasswordAuthenticationToken</code>.
     *
     * @param principal   邮箱地址
     * @param credentials 邮件验证码
     * @return UsernamePasswordAuthenticationToken with true isAuthenticated() result
     * @since 5.7
     */
    public static EmailCaptchaLoginAuthenticationToken authenticated(Object principal, Object credentials,
                                                                     Collection<? extends GrantedAuthority> authorities) {
        return new EmailCaptchaLoginAuthenticationToken(principal, credentials, authorities);
    }

}
