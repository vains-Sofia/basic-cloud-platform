package com.basic.framework.oauth2.storage.storage;

import com.basic.framework.core.util.Sequence;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import com.basic.framework.oauth2.storage.converter.Basic2ConsentConverter;
import com.basic.framework.oauth2.storage.converter.Consent2BasicConverter;
import com.basic.framework.oauth2.storage.domain.security.BasicAuthorizationConsent;
import com.basic.framework.oauth2.storage.service.BasicAuthorizationConsentService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * 核心services——授权确认信息
 *
 * @author vains
 */
public class StorageAuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final Sequence sequence = new Sequence(null);

    private final BasicAuthorizationConsentService basicAuthorizationConsentService;

    private final Basic2ConsentConverter basic2ConsentConverter;

    private final Consent2BasicConverter entityConverter = new Consent2BasicConverter();

    public StorageAuthorizationConsentService(BasicAuthorizationConsentService basicAuthorizationConsentService,
                                              RegisteredClientRepository registeredClientRepository) {
        this.basicAuthorizationConsentService = basicAuthorizationConsentService;
        this.basic2ConsentConverter = new Basic2ConsentConverter(registeredClientRepository);
    }

    @Override
    public void save(OAuth2AuthorizationConsent OAuth2AuthorizationConsent) {
        BasicAuthorizationConsent consent = this.entityConverter.convert(OAuth2AuthorizationConsent);
        if (consent == null) {
            return;
        }
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new OAuth2AuthenticationException("用户未登录，无法保存授权确认信息");
        }
        // 设置授权确认信息的id和用户信息
        consent.setId(sequence.nextId());
        consent.setPrincipalName(authenticatedUser.getId() + "");
        this.basicAuthorizationConsentService.save(consent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent OAuth2AuthorizationConsent) {
        BasicAuthorizationConsent consent = this.entityConverter.convert(OAuth2AuthorizationConsent);
        this.basicAuthorizationConsentService.remove(consent);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new OAuth2AuthenticationException("用户未登录，无法保存授权确认信息");
        }
        // 根据客户端id和用户查询授权确认信息
        BasicAuthorizationConsent consent = this.basicAuthorizationConsentService.findById(registeredClientId, authenticatedUser.getId() + "");
        return this.basic2ConsentConverter.convert(consent);
    }
}
