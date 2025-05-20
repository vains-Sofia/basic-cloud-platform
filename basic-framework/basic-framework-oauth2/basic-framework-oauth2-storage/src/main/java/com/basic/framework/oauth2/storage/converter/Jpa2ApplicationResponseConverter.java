package com.basic.framework.oauth2.storage.converter;

import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2Application;
import com.basic.framework.oauth2.storage.domain.model.BasicClientSettings;
import com.basic.framework.oauth2.storage.domain.model.BasicTokenSettings;
import com.basic.framework.oauth2.storage.domain.response.BasicApplicationResponse;
import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

/**
 * jpa实体转实体转换器
 *
 * @author vains
 */
public class Jpa2ApplicationResponseConverter implements Converter<JpaOAuth2Application, BasicApplicationResponse> {

    @Override
    public BasicApplicationResponse convert(@Nullable JpaOAuth2Application source) {
        if (source == null) {
            return null;
        }
        BasicApplicationResponse application = new BasicApplicationResponse();
        application.setCreateBy(source.getCreateBy());
        application.setUpdateBy(source.getUpdateBy());
        application.setCreateName(source.getCreateName());
        application.setUpdateName(source.getUpdateName());
        application.setCreateTime(source.getCreateTime());
        application.setUpdateTime(source.getUpdateTime());
        application.setId(source.getId());
        application.setClientId(source.getClientId());
        application.setClientName(source.getClientName());
        application.setClientLogo(source.getClientLogo());
        application.setDescription(source.getDescription());
        application.setClientIdIssuedAt(source.getClientIdIssuedAt());
        application.setClientSecretExpiresAt(source.getClientSecretExpiresAt());
        application.setScopes(JsonUtils.toObject(source.getScopes(), Set.class, String.class));
        application.setRedirectUris(JsonUtils.toObject(source.getRedirectUris(), Set.class, String.class));
        application.setClientAuthenticationMethods(JsonUtils.toObject(source.getClientAuthenticationMethods(), Set.class, String.class));
        application.setAuthorizationGrantTypes(JsonUtils.toObject(source.getAuthorizationGrantTypes(), Set.class, String.class));
        application.setPostLogoutRedirectUris(JsonUtils.toObject(source.getPostLogoutRedirectUris(), Set.class, String.class));
        application.setClientSettings(JsonUtils.toObject(source.getClientSettings(), BasicClientSettings.class));
        application.setTokenSettings(JsonUtils.toObject(source.getTokenSettings(), BasicTokenSettings.class));
        return application;
    }
}
