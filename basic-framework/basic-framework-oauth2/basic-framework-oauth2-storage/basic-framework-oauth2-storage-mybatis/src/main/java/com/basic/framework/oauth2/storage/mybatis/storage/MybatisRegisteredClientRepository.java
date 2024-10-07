package com.basic.framework.oauth2.storage.mybatis.storage;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.basic.framework.oauth2.storage.mybatis.converter.Client2RegisteredClientConverter;
import com.basic.framework.oauth2.storage.mybatis.converter.RegisteredClient2ClientConverter;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2Application;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2ApplicationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * 核心 services —— 客户端
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class MybatisRegisteredClientRepository implements RegisteredClientRepository {

    private final MybatisOAuth2ApplicationMapper oAuth2ApplicationMapper;

    private final RegisteredClient2ClientConverter registeredClient2ClientConverter = new RegisteredClient2ClientConverter();

    private final Client2RegisteredClientConverter client2RegisteredClientConverter = new Client2RegisteredClientConverter();

    @Override
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        MybatisOAuth2Application mybatisOAuth2Application = this.registeredClient2ClientConverter.convert(registeredClient);
        if (mybatisOAuth2Application == null) {
            if (log.isDebugEnabled()) {
                log.debug("OAuth2Application convert failed. Interrupt registeredClient save.");
            }
            return;
        }
        MybatisOAuth2Application selectOne = this.oAuth2ApplicationMapper.selectOne(Wrappers.lambdaQuery(MybatisOAuth2Application.class).eq(MybatisOAuth2Application::getClientId, registeredClient.getClientId()));
        if (selectOne != null) {
            mybatisOAuth2Application.setId(selectOne.getId());
            mybatisOAuth2Application.setCreateBy(selectOne.getCreateBy());
            mybatisOAuth2Application.setCreateTime(selectOne.getCreateTime());
            this.oAuth2ApplicationMapper.updateById(mybatisOAuth2Application);
        } else {
            this.oAuth2ApplicationMapper.insert(mybatisOAuth2Application);
        }
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        MybatisOAuth2Application MybatisOAuth2Application = this.oAuth2ApplicationMapper.selectById(id);
        return client2RegisteredClientConverter.convert(MybatisOAuth2Application);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        MybatisOAuth2Application selectOne = this.oAuth2ApplicationMapper.selectOne(Wrappers.lambdaQuery(MybatisOAuth2Application.class).eq(MybatisOAuth2Application::getClientId, clientId));
        if (selectOne != null) {
            return client2RegisteredClientConverter.convert(selectOne);
        }
        return null;
    }
}
