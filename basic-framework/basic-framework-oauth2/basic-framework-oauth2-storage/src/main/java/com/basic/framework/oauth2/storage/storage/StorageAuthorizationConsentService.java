package com.basic.framework.oauth2.storage.storage;

import com.basic.framework.core.util.Sequence;
import com.basic.framework.oauth2.storage.converter.Basic2ConsentConverter;
import com.basic.framework.oauth2.storage.converter.Consent2BasicConverter;
import com.basic.framework.oauth2.storage.domain.security.BasicAuthorizationConsent;
import com.basic.framework.oauth2.storage.service.BasicAuthorizationConsentService;
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
        return this.basic2ConsentConverter.convert(consent);
    }
}
