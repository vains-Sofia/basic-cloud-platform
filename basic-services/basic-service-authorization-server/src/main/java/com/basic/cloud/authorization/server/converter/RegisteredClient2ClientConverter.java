package com.basic.cloud.authorization.server.converter;

import com.basic.cloud.oauth2.authorization.server.core.BasicCoreServiceConverter;
import com.basic.cloud.authorization.server.entity.Client;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * 框架内实体转Mybatis实体转换器
 *
 * @author vains
 */
public class RegisteredClient2ClientConverter implements BasicCoreServiceConverter<RegisteredClient, Client> {

    @Override
    public Client convert(@Nullable RegisteredClient source) {
        if (source == null) {
            return null;
        }
        Client client = new Client();
        client.setId(Long.parseLong(source.getId()));
        client.setClientId(source.getClientId());
        client.setClientIdIssuedAt(instantToTime(source.getClientIdIssuedAt()));
        client.setClientSecret(source.getClientSecret());
        client.setClientSecretExpiresAt(instantToTime(source.getClientSecretExpiresAt()));
        client.setClientName(source.getClientName());
        client.setClientAuthenticationMethods(source.getClientAuthenticationMethods());
        client.setAuthorizationGrantTypes(source.getAuthorizationGrantTypes());
        client.setRedirectUris(source.getRedirectUris());
        client.setPostLogoutRedirectUris(source.getPostLogoutRedirectUris());
        client.setScopes(source.getScopes());
        client.setClientSettings(source.getClientSettings());
        client.setTokenSettings(source.getTokenSettings());
        return client;
    }
}
