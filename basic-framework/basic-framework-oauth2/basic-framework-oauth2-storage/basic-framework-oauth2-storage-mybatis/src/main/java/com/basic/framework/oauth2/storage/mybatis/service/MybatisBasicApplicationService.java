package com.basic.framework.oauth2.storage.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.basic.framework.oauth2.storage.core.domain.BasicApplication;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2Application;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2ApplicationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * 客户端服务的MybatisPlus实现
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class MybatisBasicApplicationService implements BasicApplicationService {

    private final MybatisOAuth2ApplicationMapper oAuth2ApplicationMapper;

    @Override
    public void save(BasicApplication basicApplication) {
        Assert.notNull(basicApplication, "registeredClient cannot be null");
        MybatisOAuth2Application mybatisOAuth2Application = new MybatisOAuth2Application();
        BeanUtils.copyProperties(basicApplication, mybatisOAuth2Application);
        LambdaQueryWrapper<MybatisOAuth2Application> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Application.class)
                .eq(MybatisOAuth2Application::getClientId, basicApplication.getClientId());
        MybatisOAuth2Application selectOne = this.oAuth2ApplicationMapper.selectOne(wrapper);
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
    public BasicApplication findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        MybatisOAuth2Application mybatisOAuth2Application = this.oAuth2ApplicationMapper.selectById(id);
        BasicApplication basicApplication = new BasicApplication();
        BeanUtils.copyProperties(mybatisOAuth2Application, basicApplication);
        return basicApplication;
    }

    @Override
    public BasicApplication findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        LambdaQueryWrapper<MybatisOAuth2Application> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Application.class)
                .eq(MybatisOAuth2Application::getClientId, clientId);
        MybatisOAuth2Application selectOne = this.oAuth2ApplicationMapper.selectOne(wrapper);
        if (selectOne != null) {
            BasicApplication basicApplication = new BasicApplication();
            BeanUtils.copyProperties(selectOne, basicApplication);
            return basicApplication;
        }
        return null;
    }
}
