package com.basic.cloud.authorization.server.converter;

import com.basic.cloud.authorization.server.entity.MybatisOAuth2Application;
import com.basic.cloud.oauth2.authorization.server.core.BasicCoreServiceConverter;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * 框架内实体转Mybatis实体转换器
 *
 * @author vains
 */
public class RegisteredClient2ClientConverter implements BasicCoreServiceConverter<RegisteredClient, MybatisOAuth2Application> {

    @Override
    public MybatisOAuth2Application convert(@Nullable RegisteredClient source) {
        if (source == null) {
            return null;
        }
        MybatisOAuth2Application MybatisOAuth2Application = new MybatisOAuth2Application();
        MybatisOAuth2Application.setId(Long.parseLong(source.getId()));
        MybatisOAuth2Application.setClientId(source.getClientId());
        MybatisOAuth2Application.setClientIdIssuedAt(instantToTime(source.getClientIdIssuedAt()));
        MybatisOAuth2Application.setClientSecret(source.getClientSecret());
        MybatisOAuth2Application.setClientSecretExpiresAt(instantToTime(source.getClientSecretExpiresAt()));
        MybatisOAuth2Application.setClientName(source.getClientName());
        MybatisOAuth2Application.setClientAuthenticationMethods(source.getClientAuthenticationMethods());
        MybatisOAuth2Application.setAuthorizationGrantTypes(source.getAuthorizationGrantTypes());
        MybatisOAuth2Application.setRedirectUris(source.getRedirectUris());
        MybatisOAuth2Application.setPostLogoutRedirectUris(source.getPostLogoutRedirectUris());
        MybatisOAuth2Application.setScopes(source.getScopes());
        MybatisOAuth2Application.setClientSettings(source.getClientSettings());
        MybatisOAuth2Application.setTokenSettings(source.getTokenSettings());
        return MybatisOAuth2Application;
    }
}
