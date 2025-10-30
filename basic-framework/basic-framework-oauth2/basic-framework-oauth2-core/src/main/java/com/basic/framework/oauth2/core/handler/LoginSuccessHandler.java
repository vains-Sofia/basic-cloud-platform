package com.basic.framework.oauth2.core.handler;

import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.token.generator.StandardOAuth2TokenGenerator;
import com.basic.framework.oauth2.core.util.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * 登录成功处理类
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final String loginPageUri;

    private final StandardOAuth2TokenGenerator standardOAuth2TokenGenerator;

    private final AuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("登录成功，用户: {}.", authentication.getName());
        // 如果是绝对路径(前后端分离)
        if (UrlUtils.isAbsoluteUrl(this.loginPageUri)) {
            log.debug("登录页面为独立的前端服务页面，写回json.");
            if (Objects.equals(request.getHeader(AuthorizeConstants.ADMIN_PLATFORM_LOGIN_HEADER_KEY), AuthorizeConstants.ADMIN_PLATFORM_LOGIN_HEADER_VALUE)) {
                // 管理平台登录，返回Token
                OAuth2AccessTokenResponse authenticationToken = standardOAuth2TokenGenerator.generateByDefaultClient(authentication);
                ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
                accessTokenResponseConverter.write(authenticationToken, null, httpResponse);
                return;
            }
            Result<String> success = Result.success();
            ServletUtils.renderJson(response, success);
        } else {
            log.debug("登录页面为认证服务的相对路径，跳转至之前的页面.");
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        }
    }

}
