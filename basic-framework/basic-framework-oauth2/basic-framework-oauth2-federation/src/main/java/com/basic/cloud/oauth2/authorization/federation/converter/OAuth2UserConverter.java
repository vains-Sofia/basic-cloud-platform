package com.basic.cloud.oauth2.authorization.federation.converter;

import com.basic.cloud.oauth2.authorization.domain.AuthenticatedUser;
import com.basic.cloud.oauth2.authorization.domain.DefaultAuthenticatedUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static com.basic.cloud.oauth2.authorization.core.BasicOAuth2ParameterNames.OAUTH2_ACCESS_TOKEN;

/**
 * oauth2登录获取的用户信息转为统一认证用户信息
 *
 * @author vains
 */
public interface OAuth2UserConverter extends Converter<OAuth2User, AuthenticatedUser> {

    /**
     * 设置三方token信息
     *
     * @param authenticatedUser 统一用户信息
     * @param attributes        三方登录获取的用户信息
     */
    default void setThirdAccessTokenInfo(DefaultAuthenticatedUser authenticatedUser, Map<String, Object> attributes) {
        // 获取三方accessToken信息
        Object accessTokenObj = attributes.get(OAUTH2_ACCESS_TOKEN);
        if (accessTokenObj instanceof OAuth2AccessToken accessToken) {
            // 设置token
            authenticatedUser.setCredentials(accessToken.getTokenValue());
            Instant expiresAt = accessToken.getExpiresAt();
            if (expiresAt != null) {
                LocalDateTime tokenExpiresAt = expiresAt.atZone(ZoneId.of("UTC")).toLocalDateTime();
                // token过期时间
                authenticatedUser.setCredentialsExpiresAt(tokenExpiresAt);
            }
        }
    }

}
