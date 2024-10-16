package com.basic.framework.oauth2.authorization.server.grant.device;

import com.basic.framework.oauth2.authorization.server.util.OAuth2EndpointUtils;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 自定义grant_type之设备码模式 converter
 * 
 * @author vains
 */
public class OAuth2DeviceClientAuthenticationConverter implements AuthenticationConverter {

    private final RequestMatcher deviceAuthorizationRequestMatcher;
    private final RequestMatcher deviceAccessTokenRequestMatcher;

    public OAuth2DeviceClientAuthenticationConverter(String deviceAuthorizationEndpointUri) {
        RequestMatcher clientIdParameterMatcher = request ->
                request.getParameter(OAuth2ParameterNames.CLIENT_ID) != null;
        this.deviceAuthorizationRequestMatcher = new AndRequestMatcher(
                new AntPathRequestMatcher(
                        deviceAuthorizationEndpointUri, HttpMethod.POST.name()),
                clientIdParameterMatcher);
        this.deviceAccessTokenRequestMatcher = request ->
                AuthorizationGrantType.DEVICE_CODE.getValue().equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE)) &&
                        request.getParameter(OAuth2ParameterNames.DEVICE_CODE) != null &&
                        request.getParameter(OAuth2ParameterNames.CLIENT_ID) != null;
    }

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        if (!this.deviceAuthorizationRequestMatcher.matches(request) &&
                !this.deviceAccessTokenRequestMatcher.matches(request)) {
            return null;
        }

        // client_id (REQUIRED)
        String clientId = OAuth2EndpointUtils.getRequiredParameter(request, OAuth2ParameterNames.CLIENT_ID);

        return new OAuth2DeviceClientAuthenticationToken(clientId, ClientAuthenticationMethod.NONE, null, null);
    }
}
