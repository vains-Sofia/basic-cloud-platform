package com.basic.example.data.jpa.controller;

import com.basic.example.data.jpa.domain.entity.OAuth2Application;
import com.basic.example.data.jpa.domain.request.FindApplicationPageRequest;
import com.basic.example.data.jpa.repository.OAuth2ApplicationRepository;
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
    public Result<Page<OAuth2Application>> findPage(FindApplicationPageRequest request) {
        SpecificationBuilder<OAuth2Application> builder = new SpecificationBuilder<>();
        builder.like(!ObjectUtils.isEmpty(request.getApplicationName()), "client_name", request.getApplicationName())
                .eq(!ObjectUtils.isEmpty(request.getClientId()), "client_id", request.getClientId())
                .like(!ObjectUtils.isEmpty(request.getClientAuthenticationMethod()), "client_authentication_methods", request.getClientAuthenticationMethod());
        PageRequest pageRequest = PageRequest.of(request.getCurrent().intValue(), request.getSize().intValue());
        Page<OAuth2Application> applicationPage = applicationRepository.findAll(builder, pageRequest);
        return Result.success(applicationPage);
    }

}
