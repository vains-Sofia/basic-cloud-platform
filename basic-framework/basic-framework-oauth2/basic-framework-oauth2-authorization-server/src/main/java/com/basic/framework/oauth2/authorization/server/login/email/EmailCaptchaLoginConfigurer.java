package com.basic.framework.oauth2.authorization.server.login.email;

import com.basic.framework.oauth2.authorization.server.captcha.CaptchaService;
import com.basic.framework.oauth2.authorization.server.core.AbstractLoginFilterConfigurer;
import com.basic.framework.oauth2.authorization.server.util.OAuth2ConfigurerUtils;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.ObjectUtils;

/**
 * 邮箱认证登录配置
 *
 * @author vains
 */
@Setter
public class EmailCaptchaLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractLoginFilterConfigurer<H, EmailCaptchaLoginConfigurer<H>, EmailCaptchaLoginAuthenticationFilter> {

    private CaptchaService captchaService;

    private UserDetailsService userDetailsService;

    public EmailCaptchaLoginConfigurer() {
        super(new EmailCaptchaLoginAuthenticationFilter(), null);
        emailParameter(AuthorizeConstants.EMAIL_PARAMETER);
        captchaParameter(AuthorizeConstants.EMAIL_CAPTCHA_PARAMETER);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    /**
     * 设置获取邮箱参数名
     *
     * @param emailParameter 邮箱参数名
     * @return 返回当前对象，链式调用
     */
    public EmailCaptchaLoginConfigurer<H> emailParameter(String emailParameter) {
        getAuthenticationFilter().setEmailParameter(emailParameter);
        return this;
    }

    /**
     * 设置获取邮件验证码参数名
     *
     * @param captchaParameter 邮箱参数名
     * @return 返回当前对象，链式调用
     */
    public EmailCaptchaLoginConfigurer<H> captchaParameter(String captchaParameter) {
        getAuthenticationFilter().setCaptchaParameter(captchaParameter);
        return this;
    }

    @Override
    public void init(H http) throws Exception {
        if (ObjectUtils.isEmpty(super.getLoginProcessingUrl())) {
            super.loginProcessingUrl("/login/email");
        } else {
            super.loginProcessingUrl(super.getLoginProcessingUrl());
        }

        if (userDetailsService == null) {
            this.userDetailsService = OAuth2ConfigurerUtils.getBeanOrNull(http, UserDetailsService.class);
        }

        if (captchaService == null) {
            this.captchaService = OAuth2ConfigurerUtils.getBeanOrNull(http, CaptchaService.class);
        }

        super.init(http);
    }

    @Override
    protected AuthenticationProvider authenticationProvider(H http) {
        return new EmailCaptchaLoginAuthenticationProvider(userDetailsService, captchaService);
    }

    /**
     * 设置查询用户信息的service
     *
     * @param userDetailsService 获取用户信息的service
     * @return 当前对象实例
     */
    protected EmailCaptchaLoginConfigurer<H> userDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        return this;
    }
}
