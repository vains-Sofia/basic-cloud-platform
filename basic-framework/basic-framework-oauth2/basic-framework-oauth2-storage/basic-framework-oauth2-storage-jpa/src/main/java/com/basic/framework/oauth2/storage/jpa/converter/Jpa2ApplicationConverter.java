package com.basic.framework.oauth2.storage.jpa.converter;

import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.storage.core.domain.BasicApplication;
import com.basic.framework.oauth2.storage.core.domain.model.BasicClientSettings;
import com.basic.framework.oauth2.storage.core.domain.model.BasicTokenSettings;
import com.basic.framework.oauth2.storage.jpa.domain.JpaOAuth2Application;
import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

/**
 * 实体转jpa实体转换器
 *
 * @author vains
 */
public class Jpa2ApplicationConverter implements Converter<JpaOAuth2Application, BasicApplication> {

    @Override
    public BasicApplication convert(@Nullable JpaOAuth2Application source) {
        if (source == null) {
            return null;
        }
        BasicApplication application = new BasicApplication();
        application.setId(source.getId());
        application.setClientId(source.getClientId());
        application.setClientIdIssuedAt(source.getClientIdIssuedAt());
        application.setClientSecret(source.getClientSecret());
        application.setClientName(source.getClientName());
        application.setClientLogo(source.getClientLogo());
        application.setClientSecretExpiresAt(source.getClientSecretExpiresAt());
        application.setClientAuthenticationMethods(JsonUtils.toObject(source.getClientAuthenticationMethods(), Set.class, String.class));
        application.setAuthorizationGrantTypes(JsonUtils.toObject(source.getAuthorizationGrantTypes(), Set.class, String.class));
        application.setRedirectUris(JsonUtils.toObject(source.getRedirectUris(), Set.class, String.class));
        application.setPostLogoutRedirectUris(JsonUtils.toObject(source.getPostLogoutRedirectUris(), Set.class, String.class));
        application.setScopes(JsonUtils.toObject(source.getScopes(), Set.class, String.class));
        application.setClientSettings(JsonUtils.toObject(source.getClientSettings(), BasicClientSettings.class));
        application.setTokenSettings(JsonUtils.toObject(source.getTokenSettings(), BasicTokenSettings.class));
        application.setCreateBy(source.getCreateBy());
        application.setUpdateBy(source.getUpdateBy());
        application.setCreateTime(source.getCreateTime());
        application.setUpdateTime(source.getUpdateTime());
        return application;
    }
}
