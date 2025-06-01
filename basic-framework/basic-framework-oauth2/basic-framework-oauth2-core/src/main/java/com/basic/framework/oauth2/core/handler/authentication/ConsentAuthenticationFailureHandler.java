package com.basic.framework.oauth2.core.handler.authentication;

import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import com.basic.framework.oauth2.core.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 授权确认失败处理
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class ConsentAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * 授权确认页面路径
     */
    private final String consentPageUri;

    /**
     * 授权错误页面路径
     */
    private final String authorizeErrorUri;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        if (request.getMethod().equals(HttpMethod.GET.name())
                && !ObjectUtils.isEmpty(authorizeErrorUri)) {
            // 重定向到授权错误页面
            String resolvedErrorUri = resolveErrorUri(request, response, exception);
            this.redirectStrategy.sendRedirect(request, response, resolvedErrorUri);
            return;
        }

        // 授权确认页面提交的请求
        if (request.getMethod().equals(HttpMethod.POST.name())
                && UrlUtils.isAbsoluteUrl(consentPageUri)) {
            // 重定向到授权错误页面
            String resolvedErrorUri = resolveErrorUri(request, response, exception);
            // 写回json(前端会自动跳转到返回的URI)
            Result<Object> result = Result.success(resolvedErrorUri);
            ServletUtils.renderJson(response, result);
        } else {
            if (request.getMethod().equals(HttpMethod.GET.name())
                    && !ObjectUtils.isEmpty(authorizeErrorUri)) {
                // 重定向到授权错误页面
                String resolvedErrorUri = resolveErrorUri(request, response, exception);
                this.redirectStrategy.sendRedirect(request, response, resolvedErrorUri);
                return;
            }

            // 获取具体的异常
            OAuth2AuthenticationException authenticationException = (OAuth2AuthenticationException) exception;
            OAuth2Error error = authenticationException.getError();
            // 在地址栏输入授权申请地址或设备码流程的验证地址错误(user_code错误)
            response.sendError(HttpStatus.BAD_REQUEST.value(), error.toString());
        }

    }

    /**
     * 根据当前请求及异常信息生成授权错误页面的URI
     *
     * @param request   当前请求
     * @param response  当前响应
     * @param exception 认证异常
     * @return 授权错误页面的URI
     */
    private String resolveErrorUri(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        Map<String, String> errorParameter = SecurityUtils.getErrorParameter(request, response, exception);
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>(errorParameter.size());
        errorParameter.forEach((k, v) -> valueMap.add(k, URLEncoder.encode(v, StandardCharsets.UTF_8)));
        // 如果授权错误页面路径是绝对路径，则拼接参数后直接返回
        if (UrlUtils.isAbsoluteUrl(authorizeErrorUri)) {
            return UriComponentsBuilder
                    .fromUriString(authorizeErrorUri)
                    .replaceQueryParams(valueMap)
                    .encode(StandardCharsets.UTF_8)
                    .build(Boolean.TRUE).toUriString();
        }
        // 否则，拼接成完整的URI
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(authorizeErrorUri)
                .replaceQueryParams(valueMap)
                .build()
                .toUriString();
    }

}
