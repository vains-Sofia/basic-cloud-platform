package com.basic.framework.oauth2.core.handler.authentication;

import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.core.util.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 重定向至登录处理
 *
 * @author vains
 */
@Slf4j
public class LoginTargetAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    /**
     * 设备码认证页面
     */
    private final String deviceActivateUri;

    /**
     * 设备码认证端点
     */
    private final String deviceVerificationEndpoint;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * @param loginFormUrl      URL where the login page can be found. Should either be
     *                          relative to the web-app context path (include a leading {@code /}) or an absolute
     *                          URL.
     * @param deviceActivateUri 设备码验证页面地址
     */
    public LoginTargetAuthenticationEntryPoint(String loginFormUrl, String deviceActivateUri, String deviceVerificationEndpoint) {
        super(loginFormUrl);
        this.deviceActivateUri = deviceActivateUri;
        this.deviceVerificationEndpoint = deviceVerificationEndpoint;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 兼容设备码前后端分离
        if (request.getRequestURI().equals(deviceVerificationEndpoint)
                && request.getMethod().equals(HttpMethod.POST.name())
                && UrlUtils.isAbsoluteUrl(deviceActivateUri)) {
            // 如果是请求验证设备激活码(user_code)时未登录并且设备码验证页面是前后端分离的那种则写回json
            Result<String> success = Result.error(HttpStatus.UNAUTHORIZED.value(), ("登录已失效，请重新打开设备提供的验证地址"));
            ServletUtils.renderJson(response, success);
            return;
        }

        // 获取登录表单的地址
        String loginForm = determineUrlToUseForThisRequest(request, response, authException);
        if (!UrlUtils.isAbsoluteUrl(loginForm)) {
            // 不是绝对路径调用父类方法处理
            super.commence(request, response, authException);
            return;
        }

        StringBuffer requestUrl = request.getRequestURL();
        if (!ObjectUtils.isEmpty(request.getQueryString())) {
            requestUrl.append("?").append(request.getQueryString());
        }

        // 绝对路径在重定向前添加target参数
        String targetParameter = URLEncoder.encode(requestUrl.toString(), StandardCharsets.UTF_8);
        String targetUrl = loginForm + "?target=" + targetParameter;
        log.debug("重定向至前后端分离的登录页面：{}", targetUrl);
        this.redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}
