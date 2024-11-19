package com.basic.framework.oauth2.storage.jpa.service;

import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.authorization.server.util.OAuth2JsonUtils;
import com.basic.framework.oauth2.storage.core.domain.BasicApplication;
import com.basic.framework.oauth2.storage.core.domain.request.FindApplicationPageRequest;
import com.basic.framework.oauth2.storage.core.domain.request.SaveApplicationRequest;
import com.basic.framework.oauth2.storage.core.domain.response.BasicApplicationResponse;
import com.basic.framework.oauth2.storage.core.exception.BasicApplicationStorageException;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.core.util.ClientUtils;
import com.basic.framework.oauth2.storage.jpa.converter.Application2JpaConverter;
import com.basic.framework.oauth2.storage.jpa.converter.Jpa2ApplicationConverter;
import com.basic.framework.oauth2.storage.jpa.converter.Jpa2ApplicationResponseConverter;
import com.basic.framework.oauth2.storage.jpa.domain.JpaOAuth2Application;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.Assert;
import org.springframework.util.IdGenerator;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 客户端服务基于jpa的实现
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class JpaBasicApplicationService implements BasicApplicationService {

    private final PasswordEncoder passwordEncoder;

    private final OAuth2ApplicationRepository applicationRepository;

    private final IdGenerator idGenerator = new AlternativeJdkIdGenerator();

    private final Application2JpaConverter application2JpaConverter = new Application2JpaConverter();

    private final Jpa2ApplicationConverter jpa2ApplicationConverter = new Jpa2ApplicationConverter();

    private final Jpa2ApplicationResponseConverter jpa2ApplicationResponseConverter = new Jpa2ApplicationResponseConverter();

    @Override
    public void save(BasicApplication basicApplication) {
        Assert.notNull(basicApplication, "registeredClient cannot be null");
        // 转为jpa实体
        JpaOAuth2Application jpaOAuth2Application = this.application2JpaConverter.convert(basicApplication);
        if (jpaOAuth2Application == null) {
            if (log.isDebugEnabled()) {
                log.debug("JpaOAuth2Application convert failed. Interrupt registeredClient save.");
            }
            return;
        }
        // 查询客户端是否存在
        Optional<JpaOAuth2Application> application = applicationRepository.findByClientId(jpaOAuth2Application.getClientId());
        // 设置添加时的审计信息
        if (application.isPresent()) {
            jpaOAuth2Application.setId(application.get().getId());
            jpaOAuth2Application.setCreateBy(application.get().getCreateBy());
            jpaOAuth2Application.setCreateTime(application.get().getCreateTime());
        }
        // 添加或修改
        applicationRepository.save(jpaOAuth2Application);
    }

    @Override
    public BasicApplication findById(String id) {
        // 查询并转换
        return applicationRepository.findById(Long.parseLong(id))
                .map(jpa2ApplicationConverter::convert)
                .orElse(null);
    }

    @Override
    public BasicApplication findByClientId(String clientId) {
        return applicationRepository.findByClientId(clientId)
                .map(jpa2ApplicationConverter::convert)
                .orElse(null);
    }

    @Override
    public PageResult<BasicApplicationResponse> findByPage(FindApplicationPageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(JpaOAuth2Application::getUpdateTime));
        // 分页
        PageRequest pageQuery = PageRequest.of(request.current(), request.size(), sort);

        // 条件构造器
        SpecificationBuilder<JpaOAuth2Application> builder = new SpecificationBuilder<>();
        builder.eq(!ObjectUtils.isEmpty(request.getClientId()),
                JpaOAuth2Application::getClientId, request.getClientId());

        builder.like(!ObjectUtils.isEmpty(request.getApplicationName()),
                JpaOAuth2Application::getClientName, request.getApplicationName());

        builder.like(!ObjectUtils.isEmpty(request.getClientAuthenticationMethod()),
                JpaOAuth2Application::getClientAuthenticationMethods, request.getClientAuthenticationMethod());

        builder.like(!ObjectUtils.isEmpty(request.getAuthorizationGrantType()),
                JpaOAuth2Application::getAuthorizationGrantTypes, request.getAuthorizationGrantType());

        // 查询
        Page<JpaOAuth2Application> applicationPage = applicationRepository.findAll(builder, pageQuery);

        // 转为响应bean
        List<BasicApplicationResponse> applicationList = applicationPage.getContent()
                .stream()
                .map(jpa2ApplicationResponseConverter::convert)
                .toList();
        return DataPageResult.of(applicationPage.getNumber(), applicationPage.getSize(), applicationPage.getTotalElements(), applicationList);
    }

    @Override
    public String saveApplication(SaveApplicationRequest request) {
        // 唯一校验
        SpecificationBuilder<JpaOAuth2Application> builder = new SpecificationBuilder<>();
        builder.eq(JpaOAuth2Application::getClientId, request.getClientId());
        List<JpaOAuth2Application> applicationsByClientId = applicationRepository.findAll(builder);
        if (!ObjectUtils.isEmpty(applicationsByClientId)) {
            throw new BasicApplicationStorageException("客户端已存在.");
        }

        this.validRedirectUris(request);

        BasicApplication basicApplication = new BasicApplication();
        BeanUtils.copyProperties(request, basicApplication);
        JpaOAuth2Application application = application2JpaConverter.convert(basicApplication);
        if (application == null) {
            throw new BasicApplicationStorageException("JpaOAuth2Application convert failed. Interrupt registeredClient save.");
        }

        // 如果没有token设置，则使用默认的
        if (application.getTokenSettings() == null) {
            application.setTokenSettings(OAuth2JsonUtils.toJson(ClientUtils.resolveTokenSettings(TokenSettings.builder().build())));
        }

        // 密码加密
        String password = idGenerator.generateId().toString();
        String encodePassword = passwordEncoder.encode(password);
        application.setClientSecret(encodePassword);
        // 设置客户端id签发时间
        application.setClientIdIssuedAt(LocalDateTime.now());
        return password;
    }

    @Override
    public void updateApplication(SaveApplicationRequest request) {
        // 校验是否存在
        Optional<JpaOAuth2Application> applicationById = applicationRepository.findById(request.getId());
        if (applicationById.isEmpty()) {
            throw new BasicApplicationStorageException("客户端不存在.");
        }

        // 校验唯一性
        SpecificationBuilder<JpaOAuth2Application> builder = new SpecificationBuilder<>();
        builder.ne(JpaOAuth2Application::getId, request.getId())
                .eq(JpaOAuth2Application::getClientId, request.getClientId());
        List<JpaOAuth2Application> applicationsByClientId = applicationRepository.findAll(builder);
        if (!ObjectUtils.isEmpty(applicationsByClientId)) {
            throw new BasicApplicationStorageException("客户端id不能重复.");
        }

        this.validRedirectUris(request);

        BasicApplication basicApplication = new BasicApplication();
        BeanUtils.copyProperties(request, basicApplication);
        JpaOAuth2Application application = application2JpaConverter.convert(basicApplication);
        if (application == null) {
            throw new BasicApplicationStorageException("JpaOAuth2Application convert failed. Interrupt registeredClient save.");
        }
        applicationRepository.save(application);
    }

    @Override
    public void removeByClientId(String clientId) {
        applicationRepository.removeByClientId(clientId);
    }

}
