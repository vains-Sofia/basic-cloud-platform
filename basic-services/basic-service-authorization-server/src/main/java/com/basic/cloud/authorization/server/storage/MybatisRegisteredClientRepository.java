package com.basic.cloud.authorization.server.storage;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.basic.cloud.authorization.server.converter.Client2RegisteredClientConverter;
import com.basic.cloud.authorization.server.converter.RegisteredClient2ClientConverter;
import com.basic.cloud.authorization.server.entity.MybatisOAuth2Application;
import com.basic.cloud.authorization.server.mapper.MybatisOAuth2ApplicationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 核心 services —— 客户端
 *
 * @author vains
 */
@Component
@RequiredArgsConstructor
public class MybatisRegisteredClientRepository implements RegisteredClientRepository {

    private final MybatisOAuth2ApplicationMapper MybatisOAuth2ApplicationMapper;

    private final RegisteredClient2ClientConverter registeredClient2ClientConverter = new RegisteredClient2ClientConverter();

    private final Client2RegisteredClientConverter client2RegisteredClientConverter = new Client2RegisteredClientConverter();

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        MybatisOAuth2Application selectOne = this.MybatisOAuth2ApplicationMapper.selectOne(Wrappers.lambdaQuery(MybatisOAuth2Application.class).eq(MybatisOAuth2Application::getClientId, registeredClient.getClientId()));
        if (selectOne != null) {
            this.MybatisOAuth2ApplicationMapper.delete(Wrappers.lambdaUpdate(MybatisOAuth2Application.class).eq(MybatisOAuth2Application::getClientId, selectOne.getClientId()));
        }
        MybatisOAuth2Application MybatisOAuth2Application = this.registeredClient2ClientConverter.convert(registeredClient);
        this.MybatisOAuth2ApplicationMapper.insert(MybatisOAuth2Application);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        MybatisOAuth2Application MybatisOAuth2Application = this.MybatisOAuth2ApplicationMapper.selectById(id);
        return client2RegisteredClientConverter.convert(MybatisOAuth2Application);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        MybatisOAuth2Application selectOne = this.MybatisOAuth2ApplicationMapper.selectOne(Wrappers.lambdaQuery(MybatisOAuth2Application.class).eq(MybatisOAuth2Application::getClientId, clientId));
        if (selectOne != null) {
            return client2RegisteredClientConverter.convert(selectOne);
        }
        return null;
    }
}
