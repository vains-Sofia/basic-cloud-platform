package com.basic.framework.oauth2.storage.mybatis.storage;

import com.basic.framework.oauth2.storage.core.entity.OAuth2Application;
import com.basic.framework.oauth2.storage.core.service.OAuth2ApplicationService;
import com.basic.framework.oauth2.storage.mybatis.converter.Client2RegisteredClientConverter;
import com.basic.framework.oauth2.storage.mybatis.converter.RegisteredClient2ClientConverter;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2Application;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * 核心 services —— 客户端
 * 该实现仅限框架调用，对外提供接口时请使用{@link OAuth2ApplicationService}
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class MybatisRegisteredClientRepository implements RegisteredClientRepository {

    private final OAuth2ApplicationService applicationService;

    private final RegisteredClient2ClientConverter registeredClient2ClientConverter = new RegisteredClient2ClientConverter();

    private final Client2RegisteredClientConverter client2RegisteredClientConverter = new Client2RegisteredClientConverter();

    @Override
    public void save(RegisteredClient registeredClient) {
        // 该方法仅限框架内部使用，外部接口使用时不能使用该方法
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        MybatisOAuth2Application mybatisOAuth2Application = this.registeredClient2ClientConverter.convert(registeredClient);
        if (mybatisOAuth2Application == null) {
            if (log.isDebugEnabled()) {
                log.debug("OAuth2Application convert failed. Interrupt registeredClient save.");
            }
            return;
        }
        OAuth2Application oAuth2Application = new OAuth2Application();
        BeanUtils.copyProperties(mybatisOAuth2Application, oAuth2Application);
        applicationService.save(oAuth2Application);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        OAuth2Application application = this.applicationService.findById(id);
        MybatisOAuth2Application mybatisOAuth2Application = new MybatisOAuth2Application();
        BeanUtils.copyProperties(application, mybatisOAuth2Application);
        return client2RegisteredClientConverter.convert(mybatisOAuth2Application);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        OAuth2Application application = this.applicationService.findByClientId(clientId);
        if (application != null) {
            MybatisOAuth2Application mybatisOAuth2Application = new MybatisOAuth2Application();
            BeanUtils.copyProperties(application, mybatisOAuth2Application);
            return client2RegisteredClientConverter.convert(mybatisOAuth2Application);
        }
        return null;
    }
}
