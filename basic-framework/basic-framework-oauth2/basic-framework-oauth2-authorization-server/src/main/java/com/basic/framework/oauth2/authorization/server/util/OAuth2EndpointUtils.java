package com.basic.framework.oauth2.authorization.server.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * oauth2端点工具类
 * (该工具类在Spring authorization server中是包级可访问的，提取一些用得到的工具方法出来)
 *
 * @author vains
 */
public class OAuth2EndpointUtils {

    static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    /**
     * 当oauth2流程中参数出现异常时通过该工具方法抛出异常
     *
     * @param errorCode     错误码
     * @param parameterName 异常参数名
     * @param errorUri      错误原因参考地址
     */
    public static void throwError(String errorCode, String parameterName, String errorUri) {
        OAuth2Error error = new OAuth2Error(errorCode, ("OAuth 2.0 Parameter: " + parameterName), errorUri);
        throw new OAuth2AuthenticationException(error);
    }

    /**
     * 获取本次oauth2认证的请求参数（form-data表单参数）
     * 在新版本(Spring Authorization Server 1.2.1及以上版本)中获取token更新了参数校验逻辑，只能用form-data传递参数，使用url-params会失败，原因详见 <a href="https://github.com/spring-projects/spring-authorization-server/issues/1451">issue 1451<a/>
     *
     * @param request 当前请求对象
     * @return 本次oauth2认证的请求参数
     */
    public static MultiValueMap<String, String> getFormParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameterMap.forEach((key, values) -> {
            String queryString = StringUtils.hasText(request.getQueryString()) ? request.getQueryString() : "";
            // If not query parameter then it's a form parameter
            if (!queryString.contains(key)) {
                for (String value : values) {
                    parameters.add(key, value);
                }
            }
        });
        return parameters;
    }

    /**
     * 获取可选参数，本次请求中该参数只能有一个，多个情况下会抛出 {@link OAuth2AuthenticationException} {@link OAuth2ErrorCodes#INVALID_REQUEST} 异常
     *
     * @param parameters    当前oauth2认证请求的所有参数
     * @param parameterName 参数名
     * @return 参数值
     */
    public static String getOptionalParameter(MultiValueMap<String, String> parameters, String parameterName) {
        String parametersFirst = parameters.getFirst(parameterName);
        if (StringUtils.hasText(parametersFirst)
                && parameters.get(parameterName).size() != 1) {
            throwError(OAuth2ErrorCodes.INVALID_REQUEST, parameterName, ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        return parametersFirst;
    }

    /**
     * 获取必传参数，本次请求中该参数只能有一个，未传或多个情况下会抛出 {@link OAuth2AuthenticationException} {@link OAuth2ErrorCodes#INVALID_REQUEST} 异常
     *
     * @param parameters    当前oauth2认证请求的所有参数
     * @param parameterName 参数名
     * @return 参数值
     */
    public static String getRequiredParameter(MultiValueMap<String, String> parameters, String parameterName) {
        String parametersFirst = parameters.getFirst(parameterName);
        if (!StringUtils.hasText(parametersFirst)
                || parameters.get(parameterName).size() != 1) {
            throwError(OAuth2ErrorCodes.INVALID_REQUEST, parameterName, ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        return parametersFirst;
    }

    /**
     * 获取必传参数，本次请求中该参数只能有一个，未传或多个情况下会抛出 {@link OAuth2AuthenticationException} {@link OAuth2ErrorCodes#INVALID_REQUEST} 异常
     *
     * @param request       当前http请求
     * @param parameterName 参数名
     * @return 参数值
     */
    public static String getRequiredParameter(HttpServletRequest request, String parameterName) {
        String parameter = request.getParameter(parameterName);
        if (!StringUtils.hasText(parameter) ||
                request.getParameterValues(parameterName).length != 1) {
            throwError(OAuth2ErrorCodes.INVALID_REQUEST, parameterName, ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        return parameter;
    }

}
