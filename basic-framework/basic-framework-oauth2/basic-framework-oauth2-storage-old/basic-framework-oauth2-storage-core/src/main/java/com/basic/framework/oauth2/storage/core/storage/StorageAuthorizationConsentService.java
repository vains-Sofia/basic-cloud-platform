package com.basic.framework.oauth2.storage.core.storage;

import com.basic.framework.oauth2.storage.core.converter.Entity2OAuth2AuthorizationConsentConverter;
import com.basic.framework.oauth2.storage.core.converter.OAuth2AuthorizationConsent2EntityConverter;
import com.basic.framework.oauth2.storage.core.domain.BasicAuthorizationConsent;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationConsentService;
import com.basic.framework.core.util.Sequence;
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

    private final Entity2OAuth2AuthorizationConsentConverter authorizationConsentConverter;

    private final OAuth2AuthorizationConsent2EntityConverter entityConverter = new OAuth2AuthorizationConsent2EntityConverter();

    public StorageAuthorizationConsentService(BasicAuthorizationConsentService basicAuthorizationConsentService, RegisteredClientRepository registeredClientRepository) {
        this.basicAuthorizationConsentService = basicAuthorizationConsentService;
        this.authorizationConsentConverter = new Entity2OAuth2AuthorizationConsentConverter(registeredClientRepository);
    }

    @Override
    public void save(OAuth2AuthorizationConsent OAuth2AuthorizationConsent) {
        BasicAuthorizationConsent consent = this.entityConverter.convert(OAuth2AuthorizationConsent);
        if (consent == null) {
            return;
        }
        consent.setId(sequence.nextId());
        this.basicAuthorizationConsentService.save(consent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent OAuth2AuthorizationConsent) {
        BasicAuthorizationConsent consent = this.entityConverter.convert(OAuth2AuthorizationConsent);
        this.basicAuthorizationConsentService.remove(consent);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        BasicAuthorizationConsent consent = this.basicAuthorizationConsentService.findById(registeredClientId, principalName);
        return this.authorizationConsentConverter.convert(consent);
    }
}
