package com.basic.framework.oauth2.authorization.server.grant.password;

import com.basic.framework.oauth2.core.core.BasicAuthorizationGrantType;
import com.basic.framework.oauth2.authorization.server.util.OAuth2EndpointUtils;
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
 * 自定义grant_type之密码模式 converter
 *
 * @author vains
 */
@Setter
public class OAuth2PasswordAuthenticationConverter implements AuthenticationConverter {

    private String usernameParameter = OAuth2ParameterNames.USERNAME;

    private String passwordParameter = OAuth2ParameterNames.PASSWORD;

    @Override
    public Authentication convert(HttpServletRequest request) {
        // 获取请求中的参数
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getFormParameters(request);

        // grant_type (REQUIRED)
        String grantType = parameters.getFirst(OAuth2ParameterNames.GRANT_TYPE);
        if (!BasicAuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {
            return null;
        }

        // 这里目前是客户端认证信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        // scope (OPTIONAL)
        Set<String> requestedScopes = null;
        String scope = OAuth2EndpointUtils.getOptionalParameter(parameters, OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(
                    Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        // username (REQUIRED)
        OAuth2EndpointUtils.getRequiredParameter(parameters, usernameParameter);

        // password (REQUIRED)
        OAuth2EndpointUtils.getRequiredParameter(parameters, passwordParameter);

        // 提取附加参数
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.CLIENT_ID)) {
                additionalParameters.put(key, value.getFirst());
            }
        });

        return new OAuth2PasswordAuthenticationToken(BasicAuthorizationGrantType.PASSWORD, clientPrincipal, requestedScopes, additionalParameters);
    }
}
