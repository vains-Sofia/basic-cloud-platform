package com.basic.cloud.authorization.server.controller;

import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.storage.core.converter.Authorization2OAuth2AuthorizationConverter;
import com.basic.framework.oauth2.storage.core.domain.BasicAuthorization;
import com.basic.framework.oauth2.storage.core.domain.request.FindAuthorizationPageRequest;
import com.basic.framework.oauth2.storage.core.domain.response.FindAuthorizationPageResponse;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证信息接口
 *
 * @author vains
 */
@RestController
@RequestMapping("/open/authorization")
@Tag(name = "认证信息接口", description = "认证信息相关接口")
public class OAuth2AuthorizationController {

    private final BasicAuthorizationService basicAuthorizationService;

    private final Authorization2OAuth2AuthorizationConverter authorizationConverter;

    public OAuth2AuthorizationController(BasicAuthorizationService basicAuthorizationService, RegisteredClientRepository registeredClientRepository) {
        this.basicAuthorizationService = basicAuthorizationService;
        this.authorizationConverter = new Authorization2OAuth2AuthorizationConverter(registeredClientRepository);
    }

    @GetMapping("/findByPage")
    @PreAuthorize("hasAnyAuthority('message.read')")
    @Operation(summary = "根据入参分页查询认证信息", description = "根据入参分页查询认证信息")
    public Result<PageResult<FindAuthorizationPageResponse>> findAuthorizationPage(
            FindAuthorizationPageRequest request) {
        PageResult<FindAuthorizationPageResponse> authorizationPage =
                basicAuthorizationService.findAuthorizationPage(request);

        return Result.success(authorizationPage);
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("hasAnyAuthority('message.read')")
    @Parameter(name = "id", description = "认证信息的主键id")
    @Operation(summary = "根据id查询认证信息详情", description = "根据id查询认证信息详情")
    public Result<OAuth2Authorization> findById(@PathVariable String id) {
        BasicAuthorization basicAuthorization = basicAuthorizationService.findById(id);
        OAuth2Authorization authorization = authorizationConverter.convert(basicAuthorization);

        return Result.success(authorization);
    }

}
