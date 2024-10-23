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
public class RegisteredClient2ClientConverter implements BasicCoreServiceConverter<RegisteredClient, MybatisOAuth2Application> {

    @Override
    public MybatisOAuth2Application convert(@Nullable RegisteredClient source) {
        if (source == null) {
            return null;
        }
        MybatisOAuth2Application oAuth2Application = new MybatisOAuth2Application();
        oAuth2Application.setId(Long.parseLong(source.getId()));
        oAuth2Application.setClientId(source.getClientId());
        oAuth2Application.setClientIdIssuedAt(instantToTime(source.getClientIdIssuedAt()));
        oAuth2Application.setClientSecret(source.getClientSecret());
        oAuth2Application.setClientSecretExpiresAt(instantToTime(source.getClientSecretExpiresAt()));
        oAuth2Application.setClientName(source.getClientName());
        oAuth2Application.setClientAuthenticationMethods(
                source.getClientAuthenticationMethods()
                        .stream().map(ClientAuthenticationMethod::getValue)
                        .collect(Collectors.toSet())
        );
        oAuth2Application.setAuthorizationGrantTypes(
                source.getAuthorizationGrantTypes()
                        .stream().map(AuthorizationGrantType::getValue)
                        .collect(Collectors.toSet())
        );
        oAuth2Application.setRedirectUris(source.getRedirectUris());
        oAuth2Application.setPostLogoutRedirectUris(source.getPostLogoutRedirectUris());
        oAuth2Application.setScopes(source.getScopes());
        oAuth2Application.setClientSettings(ClientUtils.resolveClientSettings(source.getClientSettings()));
        oAuth2Application.setTokenSettings(ClientUtils.resolveTokenSettings(source.getTokenSettings()));
        return oAuth2Application;
    }
}
