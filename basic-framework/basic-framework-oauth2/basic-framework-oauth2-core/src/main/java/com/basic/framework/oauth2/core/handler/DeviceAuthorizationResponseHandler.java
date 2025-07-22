package com.basic.framework.oauth2.core.handler;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * 校验设备码成功响应类
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class DeviceAuthorizationResponseHandler implements AuthenticationSuccessHandler {

    /**
     * 授权确认页面
     */
    private final String consentPageUri;

    /**
     * 设备码验证成功后跳转地址
     */
    private final String deviceActivatedUri;

    /**
     * 设备码验证页面地址
     */
    private final String deviceVerificationUri;

    @Override
    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String generateDeviceActivatedUri = this.generateDeviceActivatedUri();
        if (UrlUtils.isAbsoluteUrl(this.deviceVerificationUri)) {
            log.debug("设备码验证成功，user code验证页面为前后端分离页面，响应JSON.");
            // 写回json数据
            renderActivatedUri(response, generateDeviceActivatedUri);
        } else if (UrlUtils.isAbsoluteUrl(this.consentPageUri)) {
            log.debug("设备码验证成功，授权确认页面为前后端分离页面，响应JSON.");
            // 写回json数据
            renderActivatedUri(response, generateDeviceActivatedUri);
        } else {
            log.debug("设备码验证成功，跳转至：{}.", this.deviceActivatedUri);
            response.sendRedirect(generateDeviceActivatedUri);
        }

    }

    /**
     * 渲染设备激活的URI
     *
     * @param response                   HttpServletResponse
     * @param generateDeviceActivatedUri 生成的设备激活URI
     */
    private void renderActivatedUri(HttpServletResponse response, String generateDeviceActivatedUri) {
        if (UrlUtils.isAbsoluteUrl(this.deviceActivatedUri)) {
            Result<Object> result = Result.success(this.deviceActivatedUri);
            ServletUtils.renderJson(response, result);
        } else {
            Result<Object> result = Result.success(generateDeviceActivatedUri);
            ServletUtils.renderJson(response, result);
        }
    }

    /**
     * 生成设备激活成功的URI
     *
     * @return 设备激活的URI
     */
    private String generateDeviceActivatedUri() {
        // 生成设备激活的URI逻辑
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(this.deviceActivatedUri)
                .build()
                .toUriString();
    }

}