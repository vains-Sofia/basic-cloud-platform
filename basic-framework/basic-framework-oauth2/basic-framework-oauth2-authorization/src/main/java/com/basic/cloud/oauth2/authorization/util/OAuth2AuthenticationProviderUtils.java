package com.basic.cloud.oauth2.authorization.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

/**
 * OAuth2 provider工具类
 * (该工具类在Spring authorization server中是包级可访问的，提取一些用得到的工具方法出来)
 *
 * @author vains
 */
public class OAuth2AuthenticationProviderUtils {

    /**
     * 获取客户端认证信息，如果没有认证信息抛出 {@link OAuth2ErrorCodes#INVALID_CLIENT } oauth2异常
     *
     * @param authentication Converter中封装的AuthenticationToken的实例
     * @return 本次oauth2认证的客户端认证信息
     */
    public static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    /**
     * 当客户端异常时抛出异常
     *
     * @param parameterName 客户端异常属性
     * @param errorUri      出现异常可能的原因
     */
    public static void throwInvalidClient(String parameterName, String errorUri) {
        OAuth2Error error = new OAuth2Error(
                OAuth2ErrorCodes.INVALID_CLIENT,
                "Device client authentication failed: " + parameterName,
                errorUri
        );
        throw new OAuth2AuthenticationException(error);
    }

}
