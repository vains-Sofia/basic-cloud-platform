package com.basic.framework.oauth2.core.handler;

import com.basic.framework.oauth2.core.util.SecurityUtils;
import com.basic.framework.oauth2.core.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * OIDC登出失败自定义处理器
 * 当OIDC登出过程中发生异常时，返回JSON格式的错误响应
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class BasicOidcLogoutAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * 授权错误页面路径
     */
    private final String authorizeErrorUri;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        if (log.isErrorEnabled()) {
            log.error("OIDC logout authentication failed", exception);
        }

        // 设置响应状态码
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        // 构建错误结果
        Map<String, String> errorParameter = SecurityUtils.getErrorParameter(request, response, exception);

        if (log.isDebugEnabled()) {
            log.debug("OIDC logout failed, returning JSON error response: {}", errorParameter);
        }

        if (StringUtils.hasText(authorizeErrorUri)) {
            String resolvedAuthorizeErrorUri = SecurityUtils.resolveAuthorizeErrorUri(authorizeErrorUri, errorParameter);
            // 如果有指定的错误页面URI，则重定向到该URI
            this.redirectStrategy.sendRedirect(request, response, resolvedAuthorizeErrorUri);
        } else {
            // 返回JSON响应
            ServletUtils.renderJson(response, errorParameter);
        }
    }
}