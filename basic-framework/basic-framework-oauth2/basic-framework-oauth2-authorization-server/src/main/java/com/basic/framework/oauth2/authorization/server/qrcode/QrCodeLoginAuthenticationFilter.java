package com.basic.framework.oauth2.authorization.server.qrcode;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 二维码认证登录
 *
 * @author vains
 */
@Setter
public class QrCodeLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 是否只允许POST请求
     */
    private boolean postOnly = true;

    /**
     * 代表二维码唯一标识参数的名称
     */
    private String qrCodeParameter = AuthorizeConstants.QR_CODE_PARAMETER;

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login/qr-code",
            "POST");

    protected QrCodeLoginAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    protected QrCodeLoginAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, HttpMethod.POST.name()));
    }

    protected QrCodeLoginAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    protected QrCodeLoginAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, HttpMethod.POST.name()), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String qrCodeData = this.obtainQrCode(request);
        String qrCode = (qrCodeData != null) ? qrCodeData.trim() : "";
        QrCodeLoginAuthenticationToken authRequest = QrCodeLoginAuthenticationToken.unauthenticated(qrCode);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 从当前请求中获取二维码唯一标识参数
     *
     * @param request 当前请求
     * @return 二维码唯一标识参数，如果不存在则返回 null
     */
    @Nullable
    protected String obtainQrCode(HttpServletRequest request) {
        return request.getParameter(this.qrCodeParameter);
    }

    /**
     * 将请求详情设置进登录认证令牌中
     *
     * @param request     当前请求
     * @param authRequest 登录认证令牌
     */
    protected void setDetails(HttpServletRequest request, QrCodeLoginAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
