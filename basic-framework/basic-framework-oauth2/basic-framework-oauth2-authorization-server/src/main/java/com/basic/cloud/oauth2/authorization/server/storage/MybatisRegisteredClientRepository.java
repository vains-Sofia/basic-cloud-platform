package com.basic.cloud.oauth2.authorization.server.storage;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.basic.cloud.oauth2.authorization.server.converter.Client2RegisteredClientConverter;
import com.basic.cloud.oauth2.authorization.server.converter.RegisteredClient2ClientConverter;
import com.basic.cloud.oauth2.authorization.server.entity.Client;
import com.basic.cloud.oauth2.authorization.server.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * 核心 services —— 客户端
 *
 * @author vains
 */
@RequiredArgsConstructor
public class MybatisRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientMapper clientMapper;

    private final RegisteredClient2ClientConverter registeredClient2ClientConverter = new RegisteredClient2ClientConverter();

    private final Client2RegisteredClientConverter client2RegisteredClientConverter = new Client2RegisteredClientConverter();

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        Client selectOne = this.clientMapper.selectOne(Wrappers.lambdaQuery(Client.class).eq(Client::getClientId, registeredClient.getClientId()));
        if (selectOne != null) {
            this.clientMapper.delete(Wrappers.lambdaUpdate(Client.class).eq(Client::getClientId, selectOne.getClientId()));
        }
        Client client = this.registeredClient2ClientConverter.convert(registeredClient);
        this.clientMapper.insert(client);
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        Client client = this.clientMapper.selectById(id);
        return client2RegisteredClientConverter.convert(client);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        Client selectOne = this.clientMapper.selectOne(Wrappers.lambdaQuery(Client.class).eq(Client::getClientId, clientId));
        if (selectOne != null) {
            return client2RegisteredClientConverter.convert(selectOne);
        }
        return null;
    }
}
