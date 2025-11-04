package com.basic.framework.oauth2.storage.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.storage.core.domain.BasicApplication;
import com.basic.framework.oauth2.storage.core.domain.request.FindApplicationPageRequest;
import com.basic.framework.oauth2.storage.core.domain.request.SaveApplicationRequest;
import com.basic.framework.oauth2.storage.core.domain.response.BasicApplicationResponse;
import com.basic.framework.oauth2.storage.core.exception.BasicApplicationStorageException;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.core.util.ClientUtils;
import com.basic.framework.oauth2.storage.mybatis.domain.MybatisOAuth2Application;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2ApplicationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

        if (Objects.equals(mybatisOAuth2Application.getClientId(), AuthorizeConstants.STANDARD_OAUTH2_CLIENT_ID)) {
            // 系统专用客户端
            mybatisOAuth2Application.setSystemClient(Boolean.TRUE);
        }
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
        Page<BasicApplicationResponse> pageQuery = Page.of(request.getCurrent(), request.getSize());
        IPage<BasicApplicationResponse> pageResult = oAuth2ApplicationMapper.selectConditionPage(pageQuery, request);

        return PageResult.of(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), pageResult.getRecords());
    }

    @Override
    public String saveApplication(SaveApplicationRequest request) {
        // 唯一校验
        LambdaQueryWrapper<MybatisOAuth2Application> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Application.class)
                .eq(MybatisOAuth2Application::getClientId, request.getClientId());
        MybatisOAuth2Application application = oAuth2ApplicationMapper.selectOne(wrapper);
        if (application != null) {
            throw new BasicApplicationStorageException("客户端已存在.");
        }

        this.validRedirectUris(request);
        MybatisOAuth2Application mybatisOAuth2Application = new MybatisOAuth2Application();
        BeanUtils.copyProperties(request, mybatisOAuth2Application);
        // 如果没有token设置，则使用默认的
        if (mybatisOAuth2Application.getTokenSettings() == null) {
            mybatisOAuth2Application.setTokenSettings(ClientUtils.resolveTokenSettings(TokenSettings.builder().build()));
        }
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
        // 校验是否存在
        MybatisOAuth2Application application = oAuth2ApplicationMapper.selectById(request.getId());
        if (application == null) {
            throw new BasicApplicationStorageException("客户端不存在.");
        }

        // 校验唯一性
        LambdaQueryWrapper<MybatisOAuth2Application> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Application.class)
                .eq(MybatisOAuth2Application::getClientId, request.getClientId())
                .ne(MybatisOAuth2Application::getId, request.getId());
        List<MybatisOAuth2Application> oAuth2Applications = oAuth2ApplicationMapper.selectList(wrapper);
        if (!ObjectUtils.isEmpty(oAuth2Applications)) {
            throw new BasicApplicationStorageException("客户端id不能重复.");
        }

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
}
