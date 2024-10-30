package com.basic.framework.oauth2.storage.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.oauth2.storage.core.domain.BasicApplication;
import com.basic.framework.oauth2.storage.core.domain.request.FindApplicationPageRequest;
import com.basic.framework.oauth2.storage.core.domain.request.SaveApplicationRequest;
import com.basic.framework.oauth2.storage.core.domain.response.BasicApplicationResponse;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2Application;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2ApplicationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * 客户端服务的MybatisPlus实现
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class MybatisBasicApplicationService implements BasicApplicationService {

    private final PasswordEncoder passwordEncoder;

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

    @Override
    public PageResult<BasicApplicationResponse> findByPage(FindApplicationPageRequest request) {
        IPage<BasicApplicationResponse> pageResult = oAuth2ApplicationMapper.selectConditionPage(Page.of(request.getCurrent(), request.getSize()), request);

        return PageResult.of(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), pageResult.getRecords());
    }

    @Override
    public String saveApplication(SaveApplicationRequest request) {
        this.validRedirectUris(request);
        MybatisOAuth2Application mybatisOAuth2Application = new MybatisOAuth2Application();
        BeanUtils.copyProperties(request, mybatisOAuth2Application);
        mybatisOAuth2Application.setId(IdWorker.getId());
        // 密码加密
        String password = IdWorker.get32UUID();
        String encodePassword = passwordEncoder.encode(password);
        mybatisOAuth2Application.setClientSecret(encodePassword);
        // 设置客户端id签发时间
        mybatisOAuth2Application.setClientIdIssuedAt(LocalDateTime.now());
        this.oAuth2ApplicationMapper.insert(mybatisOAuth2Application);
        return password;
    }

    @Override
    public void updateApplication(SaveApplicationRequest request) {
        this.validRedirectUris(request);
        MybatisOAuth2Application mybatisOAuth2Application = new MybatisOAuth2Application();
        BeanUtils.copyProperties(request, mybatisOAuth2Application);
        this.oAuth2ApplicationMapper.updateById(mybatisOAuth2Application);
    }

    @Override
    public void removeByClientId(String clientId) {
        LambdaQueryWrapper<MybatisOAuth2Application> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Application.class)
                .eq(MybatisOAuth2Application::getClientId, clientId);
        this.oAuth2ApplicationMapper.delete(wrapper);
    }

    /**
     * 验证回调地址
     *
     * @param request 保存/更新客户端入参
     */
    private void validRedirectUris(SaveApplicationRequest request) {
        if (request.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE.getValue())) {
            // 授权码/PKCE模式下回调地址不能为空
            Assert.notEmpty(request.getRedirectUris(), "回调地址不能为空.");
        }
    }
}
