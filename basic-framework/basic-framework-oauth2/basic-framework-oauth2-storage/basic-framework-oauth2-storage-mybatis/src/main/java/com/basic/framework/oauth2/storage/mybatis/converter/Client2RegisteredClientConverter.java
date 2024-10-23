package com.basic.framework.oauth2.storage.mybatis.converter;

import com.basic.framework.oauth2.authorization.server.core.BasicCoreServiceConverter;
import com.basic.framework.oauth2.storage.core.util.ClientUtils;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2Application;
import jakarta.annotation.Nullable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.stream.Collectors;

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
        client.clientAuthenticationMethods(methods ->
                methods.addAll(
                        source.getClientAuthenticationMethods()
                                .stream().map(ClientAuthenticationMethod::new)
                                .collect(Collectors.toSet())
                )
        );
        client.authorizationGrantTypes(grantTypes ->
                grantTypes.addAll(
                        source.getAuthorizationGrantTypes()
                                .stream().map(AuthorizationGrantType::new)
                                .collect(Collectors.toSet())
                )
        );
        client.redirectUris(uris -> uris.addAll(source.getRedirectUris()));
        client.postLogoutRedirectUris(uris -> uris.addAll(source.getPostLogoutRedirectUris()));
        client.scopes(scopes -> scopes.addAll(source.getScopes()));
        client.clientSettings(ClientUtils.resolveOAuth2ClientSettings(source.getClientSettings(), source.getClientAuthenticationMethods()));
        client.tokenSettings(ClientUtils.resolveOAuth2TokenSettings(source.getTokenSettings()));
        return client.build();
    }
}
