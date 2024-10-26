package com.basic.framework.oauth2.storage.mybatis.converter;

import com.basic.framework.oauth2.authorization.server.core.BasicCoreServiceConverter;
import com.basic.framework.oauth2.storage.core.domain.BasicApplication;
import com.basic.framework.oauth2.storage.core.util.ClientUtils;
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
public class RegisteredClient2ClientConverter implements BasicCoreServiceConverter<RegisteredClient, BasicApplication> {

    @Override
    public BasicApplication convert(@Nullable RegisteredClient source) {
        if (source == null) {
            return null;
        }
        BasicApplication basicApplication = new BasicApplication();
        basicApplication.setId(Long.parseLong(source.getId()));
        basicApplication.setClientId(source.getClientId());
        basicApplication.setClientIdIssuedAt(instantToTime(source.getClientIdIssuedAt()));
        basicApplication.setClientSecret(source.getClientSecret());
        basicApplication.setClientSecretExpiresAt(instantToTime(source.getClientSecretExpiresAt()));
        basicApplication.setClientName(source.getClientName());
        basicApplication.setClientAuthenticationMethods(
                source.getClientAuthenticationMethods()
                        .stream().map(ClientAuthenticationMethod::getValue)
                        .collect(Collectors.toSet())
        );
        basicApplication.setAuthorizationGrantTypes(
                source.getAuthorizationGrantTypes()
                        .stream().map(AuthorizationGrantType::getValue)
                        .collect(Collectors.toSet())
        );
        basicApplication.setRedirectUris(source.getRedirectUris());
        basicApplication.setPostLogoutRedirectUris(source.getPostLogoutRedirectUris());
        basicApplication.setScopes(source.getScopes());
        basicApplication.setClientSettings(ClientUtils.resolveClientSettings(source.getClientSettings()));
        basicApplication.setTokenSettings(ClientUtils.resolveTokenSettings(source.getTokenSettings()));
        return basicApplication;
    }
}
