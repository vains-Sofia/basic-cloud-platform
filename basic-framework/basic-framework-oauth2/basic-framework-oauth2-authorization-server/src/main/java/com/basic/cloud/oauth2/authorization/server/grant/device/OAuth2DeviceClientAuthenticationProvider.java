package com.basic.cloud.oauth2.authorization.server.grant.device;

import com.basic.cloud.oauth2.authorization.server.util.OAuth2AuthenticationProviderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.ObjectUtils;

/**
 * 自定义grant_type之设备码模式 provider
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class OAuth2DeviceClientAuthenticationProvider implements AuthenticationProvider {

    private final RegisteredClientRepository registeredClientRepository;

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2DeviceClientAuthenticationToken deviceClientAuthentication =
                (OAuth2DeviceClientAuthenticationToken) authentication;

        if (!ClientAuthenticationMethod.NONE.equals(deviceClientAuthentication.getClientAuthenticationMethod())) {
            return null;
        }

        String clientId = deviceClientAuthentication.getPrincipal().toString();
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            OAuth2AuthenticationProviderUtils.throwInvalidClient(OAuth2ParameterNames.CLIENT_ID, ERROR_URI);
        }

        if (log.isTraceEnabled()) {
            log.trace("Retrieved registered client");
        }

        if (registeredClient == null
                || ObjectUtils.isEmpty(registeredClient.getClientAuthenticationMethods())
                || !registeredClient.getClientAuthenticationMethods().contains(
                deviceClientAuthentication.getClientAuthenticationMethod())) {
            OAuth2AuthenticationProviderUtils.throwInvalidClient("authentication_method", ERROR_URI);
        }

        if (log.isTraceEnabled()) {
            log.trace("Validated device client authentication parameters");
        }

        if (log.isTraceEnabled()) {
            log.trace("Authenticated device client");
        }

        return new OAuth2DeviceClientAuthenticationToken(registeredClient,
                deviceClientAuthentication.getClientAuthenticationMethod(), null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2DeviceClientAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
