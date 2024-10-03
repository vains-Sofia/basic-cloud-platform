package com.basic.framework.oauth2.authorization.server.email;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 邮件认证登录
 *
 * @author vains
 */
@Setter
public class EmailCaptchaLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 是否只允许POST请求
     */
    private boolean postOnly = true;

    /**
     * 邮箱参数名
     */
    private String emailParameter = "email";

    /**
     * 邮箱验证码参数名
     */
    private String captchaParameter = "captcha";

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login/email",
            "POST");

    protected EmailCaptchaLoginAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    protected EmailCaptchaLoginAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, "POST"));
    }

    protected EmailCaptchaLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    protected EmailCaptchaLoginAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, "POST"), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String email = this.obtainEmail(request);
        email = (email != null) ? email.trim() : "";
        String captcha = obtainCaptcha(request);
        captcha = (captcha != null) ? captcha : "";
        EmailCaptchaLoginAuthenticationToken authRequest = EmailCaptchaLoginAuthenticationToken.unauthenticated(email,
                captcha);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 从当前请求中获取验证码
     *
     * @param request 当前请求
     * @return 获取到的验证码会给 Authentication 然后传给 AuthenticationManager 中
     */
    @Nullable
    protected String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter(this.captchaParameter);
    }

    /**
     * 从当前请求中获取邮箱
     *
     * @param request 当前请求
     * @return 获取到的邮箱会给 Authentication 然后传给 AuthenticationManager 中
     */
    @Nullable
    protected String obtainEmail(HttpServletRequest request) {
        return request.getParameter(this.emailParameter);
    }

    /**
     * 将请求详情设置进登录认证令牌中
     *
     * @param request     当前请求
     * @param authRequest 登录认证令牌
     */
    protected void setDetails(HttpServletRequest request, EmailCaptchaLoginAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
