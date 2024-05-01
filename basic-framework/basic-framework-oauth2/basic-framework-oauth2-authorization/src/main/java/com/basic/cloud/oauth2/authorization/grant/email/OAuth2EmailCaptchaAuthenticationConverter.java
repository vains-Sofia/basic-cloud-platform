package com.basic.cloud.oauth2.authorization.grant.email;

import com.basic.cloud.oauth2.authorization.core.BasicAuthorizationGrantType;
import com.basic.cloud.oauth2.authorization.core.BasicOAuth2ParameterNames;
import com.basic.cloud.oauth2.authorization.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 自定义grant_type之邮件模式 converter
 *
 * @author vains
 */
@Setter
public class OAuth2EmailCaptchaAuthenticationConverter implements AuthenticationConverter {

    private String emailParameter = BasicOAuth2ParameterNames.EMAIL;

    private String emailCaptchaParameter = BasicOAuth2ParameterNames.EMAIL_CAPTCHA;

    @Override
    public Authentication convert(HttpServletRequest request) {
        // 获取请求中的参数
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getFormParameters(request);

        // grant_type (REQUIRED)
        String grantType = parameters.getFirst(OAuth2ParameterNames.GRANT_TYPE);
        if (!BasicAuthorizationGrantType.EMAIL.getValue().equals(grantType)) {
            return null;
        }

        // scope (OPTIONAL)
        Set<String> requestedScopes = null;
        String scope = OAuth2EndpointUtils.getOptionalParameter(parameters, OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(
                    Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        // 邮箱地址 (REQUIRED)
        OAuth2EndpointUtils.getRequiredParameter(parameters, emailParameter);

        // 邮件验证码 (REQUIRED)
        OAuth2EndpointUtils.getRequiredParameter(parameters, emailCaptchaParameter);

        // 这里目前是客户端认证信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        // 提取附加参数
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID)) {
                additionalParameters.put(key, value.getFirst());
            }
        });
        return new OAuth2EmailCaptchaAuthenticationToken(BasicAuthorizationGrantType.EMAIL, clientPrincipal, requestedScopes, additionalParameters);
    }
}
