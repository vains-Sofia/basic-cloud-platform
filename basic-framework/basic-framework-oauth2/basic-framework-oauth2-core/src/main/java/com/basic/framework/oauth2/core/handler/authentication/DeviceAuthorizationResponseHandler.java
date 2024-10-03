package com.basic.framework.oauth2.core.handler.authentication;

import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.core.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;

/**
 * 校验设备码成功响应类
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class DeviceAuthorizationResponseHandler implements AuthenticationSuccessHandler {

    /**
     * 设备码验证成功后跳转地址
     */
    private final String deviceActivatedUri;

    @Override
    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (UrlUtils.isAbsoluteUrl(this.deviceActivatedUri)) {
            log.debug("设备码验证成功，响应JSON.");
            // 写回json数据
            Result<Object> result = Result.success(this.deviceActivatedUri);
            ServletUtils.renderJson(response, result);
        } else {
            log.debug("设备码验证成功，跳转至：{}.", this.deviceActivatedUri);
            response.sendRedirect(this.deviceActivatedUri);
        }

    }
}