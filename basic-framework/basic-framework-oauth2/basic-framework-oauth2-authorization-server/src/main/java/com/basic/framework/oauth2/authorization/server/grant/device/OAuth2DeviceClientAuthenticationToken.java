package com.basic.framework.oauth2.authorization.server.grant.device;

import jakarta.annotation.Nullable;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Map;

/**
 * 自定义grant_type之设备码模式 token
 *
 * @author vains
 */
public class OAuth2DeviceClientAuthenticationToken extends OAuth2ClientAuthenticationToken {

    public OAuth2DeviceClientAuthenticationToken(String clientId, ClientAuthenticationMethod clientAuthenticationMethod,
                                                 @Nullable Object credentials, @Nullable Map<String, Object> additionalParameters) {
        super(clientId, clientAuthenticationMethod, credentials, additionalParameters);
    }

    public OAuth2DeviceClientAuthenticationToken(RegisteredClient registeredClient, ClientAuthenticationMethod clientAuthenticationMethod,
                                                 @Nullable Object credentials) {
        super(registeredClient, clientAuthenticationMethod, credentials);
    }

}
