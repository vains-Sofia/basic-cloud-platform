package com.basic.framework.oauth2.storage.jpa.converter;

import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.storage.core.domain.BasicApplication;
import com.basic.framework.oauth2.storage.jpa.domain.JpaOAuth2Application;
import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;

/**
 * 实体转jpa实体转换器
 *
 * @author vains
 */
public class Application2JpaConverter implements Converter<BasicApplication, JpaOAuth2Application> {

    @Override
    public JpaOAuth2Application convert(@Nullable BasicApplication source) {
        if (source == null) {
            return null;
        }
        JpaOAuth2Application application = new JpaOAuth2Application();
        application.setId(source.getId());
        application.setClientId(source.getClientId());
        application.setClientIdIssuedAt(source.getClientIdIssuedAt());
        application.setClientSecret(source.getClientSecret());
        application.setClientSecretExpiresAt(source.getClientSecretExpiresAt());
        application.setClientName(source.getClientName());
        application.setClientLogo(source.getClientLogo());
        application.setClientAuthenticationMethods(JsonUtils.toJson(source.getClientAuthenticationMethods()));
        application.setAuthorizationGrantTypes(JsonUtils.toJson(source.getAuthorizationGrantTypes()));
        application.setRedirectUris(JsonUtils.toJson(source.getRedirectUris()));
        application.setPostLogoutRedirectUris(JsonUtils.toJson(source.getPostLogoutRedirectUris()));
        application.setScopes(JsonUtils.toJson(source.getScopes()));
        application.setClientSettings(JsonUtils.toJson(source.getClientSettings()));
        application.setTokenSettings(JsonUtils.toJson(source.getTokenSettings()));
        return application;
    }
}
