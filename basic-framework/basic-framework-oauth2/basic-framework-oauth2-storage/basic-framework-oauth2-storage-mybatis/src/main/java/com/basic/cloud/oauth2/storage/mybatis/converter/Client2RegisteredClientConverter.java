package com.basic.cloud.oauth2.storage.mybatis.converter;

import com.basic.cloud.oauth2.storage.mybatis.entity.MybatisOAuth2Application;
import com.basic.cloud.oauth2.authorization.server.core.BasicCoreServiceConverter;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * 框架内实体转Mybatis实体转换器
 *
 * @author vains
 */
public class Client2RegisteredClientConverter implements BasicCoreServiceConverter<MybatisOAuth2Application, RegisteredClient> {

    @Override
    public RegisteredClient convert(@Nullable MybatisOAuth2Application source) {
        if (source == null) {
            return null;
        }
        RegisteredClient.Builder client = RegisteredClient.withId(String.valueOf(source.getId()));
        client.clientId(source.getClientId());
        client.clientIdIssuedAt(timeToInstant(source.getClientIdIssuedAt()));
        client.clientSecret(source.getClientSecret());
        client.clientSecretExpiresAt(timeToInstant(source.getClientSecretExpiresAt()));
        client.clientName(source.getClientName());
        client.clientAuthenticationMethods(methods -> methods.addAll(source.getClientAuthenticationMethods()));
        client.authorizationGrantTypes(grantTypes -> grantTypes.addAll(source.getAuthorizationGrantTypes()));
        client.redirectUris(uris -> uris.addAll(source.getRedirectUris()));
        client.postLogoutRedirectUris(uris -> uris.addAll(source.getPostLogoutRedirectUris()));
        client.scopes(scopes -> scopes.addAll(source.getScopes()));
        client.clientSettings(source.getClientSettings());
        client.tokenSettings(source.getTokenSettings());
        return client.build();
    }
}
