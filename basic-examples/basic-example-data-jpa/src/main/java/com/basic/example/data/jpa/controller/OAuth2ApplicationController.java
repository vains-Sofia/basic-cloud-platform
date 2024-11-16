package com.basic.example.data.jpa.controller;

import com.basic.example.data.jpa.domain.entity.OAuth2Application;
import com.basic.example.data.jpa.domain.request.FindApplicationPageRequest;
import com.basic.example.data.jpa.repository.OAuth2ApplicationRepository;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * oauth2客户端接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/example/application")
public class OAuth2ApplicationController {

    private final OAuth2ApplicationRepository applicationRepository;

    @GetMapping("/findPage")
    public Result<PageResult<OAuth2Application>> findPage(FindApplicationPageRequest request) {
        // 组装查询条件
        SpecificationBuilder<OAuth2Application> builder = new SpecificationBuilder<>();
        builder.like(!ObjectUtils.isEmpty(request.getApplicationName()), OAuth2Application::getClientName, request.getApplicationName())
                .eq(!ObjectUtils.isEmpty(request.getClientId()), OAuth2Application::getClientId, request.getClientId())
                .like(!ObjectUtils.isEmpty(request.getClientAuthenticationMethod()), OAuth2Application::getClientAuthenticationMethods, request.getClientAuthenticationMethod());
        PageRequest pageRequest = PageRequest.of(request.getCurrent().intValue(), request.getSize().intValue());
        // 查询
        Page<OAuth2Application> applicationPage = applicationRepository.findAll(builder, pageRequest);
        // 响应
        PageResult<OAuth2Application> pageResult = PageResult.of((long) applicationPage.getNumber(), (long) applicationPage.getSize(), applicationPage.getTotalElements(), applicationPage.getContent());
        return Result.success(pageResult);
    }

}
