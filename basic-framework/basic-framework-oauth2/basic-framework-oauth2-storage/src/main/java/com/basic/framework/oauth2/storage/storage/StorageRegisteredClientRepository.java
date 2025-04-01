package com.basic.framework.oauth2.storage.storage;

import com.basic.framework.oauth2.storage.converter.Basic2RegisteredClientConverter;
import com.basic.framework.oauth2.storage.converter.RegisteredClient2BasicConverter;
import com.basic.framework.oauth2.storage.domain.security.BasicApplication;
import com.basic.framework.oauth2.storage.service.BasicApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * 核心 services —— 客户端
 * 该实现仅限框架调用，对外提供接口时请使用{@link BasicApplicationService}
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class StorageRegisteredClientRepository implements RegisteredClientRepository {

    private final BasicApplicationService applicationService;

    private final RegisteredClient2BasicConverter registeredClient2ClientConverter = new RegisteredClient2BasicConverter();

    private final Basic2RegisteredClientConverter client2RegisteredClientConverter = new Basic2RegisteredClientConverter();

    @Override
    public void save(RegisteredClient registeredClient) {
        // 该方法仅限框架内部使用，外部接口使用时不能使用该方法
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        BasicApplication basicApplication = this.registeredClient2ClientConverter.convert(registeredClient);
        if (basicApplication == null) {
            if (log.isDebugEnabled()) {
                log.debug("OAuth2Application convert failed. Interrupt registeredClient save.");
            }
            return;
        }
        applicationService.save(basicApplication);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        BasicApplication application = this.applicationService.findById(id);
        return client2RegisteredClientConverter.convert(application);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        BasicApplication application = this.applicationService.findByClientId(clientId);
        if (application != null) {
            return client2RegisteredClientConverter.convert(application);
        }
        return null;
    }
}
