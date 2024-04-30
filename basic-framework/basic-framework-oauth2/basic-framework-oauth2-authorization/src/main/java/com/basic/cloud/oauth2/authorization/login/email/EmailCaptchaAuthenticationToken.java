package com.basic.cloud.oauth2.authorization.login.email;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 邮件认证登录 token
 *
 * @author vains
 */
public class EmailCaptchaAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 邮箱地址
     */
    private final Object principal;

    /**
     * 邮件验证码
     */
    private Object credentials;

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     */
    public EmailCaptchaAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param principal   邮箱地址
     * @param credentials 邮件验证码
     * @param authorities 邮箱对应用户的权限
     */
    public EmailCaptchaAuthenticationToken(Object principal, Object credentials,
                                           Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
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
    public static EmailCaptchaAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new EmailCaptchaAuthenticationToken(principal, credentials);
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
    public static EmailCaptchaAuthenticationToken authenticated(Object principal, Object credentials,
                                                                Collection<? extends GrantedAuthority> authorities) {
        return new EmailCaptchaAuthenticationToken(principal, credentials, authorities);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

}
