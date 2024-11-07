package com.basic.framework.oauth2.storage.mybatis.storage;

import com.basic.framework.oauth2.storage.core.domain.BasicAuthorization;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import com.basic.framework.oauth2.storage.core.converter.Authorization2OAuth2AuthorizationConverter;
import com.basic.framework.oauth2.storage.core.converter.OAuth2Authorization2AuthorizationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * 核心services——认证信息
 *
 * @author vains
 */
@Slf4j
public class MybatisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final BasicAuthorizationService basicAuthorizationService;

    private final Authorization2OAuth2AuthorizationConverter oAuth2AuthorizationConverter;

    private final OAuth2Authorization2AuthorizationConverter authorizationConverter = new OAuth2Authorization2AuthorizationConverter();

    public MybatisOAuth2AuthorizationService(BasicAuthorizationService basicAuthorizationService, RegisteredClientRepository registeredClientRepository) {
        this.basicAuthorizationService = basicAuthorizationService;
        this.oAuth2AuthorizationConverter = new Authorization2OAuth2AuthorizationConverter(registeredClientRepository);
    }

    @Override
    public void save(OAuth2Authorization oauth2Authorization) {
        Assert.notNull(oauth2Authorization, "authorization cannot be null");
        BasicAuthorization convert = this.authorizationConverter.convert(oauth2Authorization);
        if (convert == null) {
            if (log.isDebugEnabled()) {
                log.debug("authorization convert failed. Interrupt OAuth2Authorization save.");
            }
            return;
        }

        this.basicAuthorizationService.save(convert);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        this.basicAuthorizationService.remove(authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        BasicAuthorization authorization = this.basicAuthorizationService.findById(id);
        return this.oAuth2AuthorizationConverter.convert(authorization);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        BasicAuthorization authorization = this.basicAuthorizationService.findByToken(token, tokenType);
        return this.oAuth2AuthorizationConverter.convert(authorization);
    }
}
