package com.basic.cloud.authorization.server.controller;

import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.storage.converter.Basic2AuthorizationConverter;
import com.basic.framework.oauth2.storage.domain.request.FindAuthorizationPageRequest;
import com.basic.framework.oauth2.storage.domain.request.OfflineAuthorizationRequest;
import com.basic.framework.oauth2.storage.domain.response.FindAuthorizationResponse;
import com.basic.framework.oauth2.storage.domain.security.BasicAuthorization;
import com.basic.framework.oauth2.storage.service.BasicAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.*;

/**
 * 认证信息接口
 *
 * @author vains
 */
@RestController
@RequestMapping("/authorization")
@Tag(name = "认证信息接口", description = "认证信息相关接口")
public class OAuth2AuthorizationController {

    private final BasicAuthorizationService basicAuthorizationService;

    private final Basic2AuthorizationConverter authorizationConverter;

    public OAuth2AuthorizationController(BasicAuthorizationService basicAuthorizationService, RegisteredClientRepository registeredClientRepository) {
        this.basicAuthorizationService = basicAuthorizationService;
        this.authorizationConverter = new Basic2AuthorizationConverter(registeredClientRepository);
    }

    @GetMapping("/findByPage")
    @Operation(summary = "根据入参分页查询认证信息", description = "根据入参分页查询认证信息")
    public Result<PageResult<FindAuthorizationResponse>> findAuthorizationPage(@Valid FindAuthorizationPageRequest request) {
        PageResult<FindAuthorizationResponse> authorizationPage = basicAuthorizationService.findAuthorizationPage(request);

        return Result.success(authorizationPage);
    }

    @GetMapping("/findById/{id}")
    @Parameter(name = "id", description = "认证信息的主键id")
    @Operation(summary = "根据id查询认证信息详情", description = "根据id查询认证信息详情")
    public Result<OAuth2Authorization> findById(@PathVariable String id) {
        BasicAuthorization basicAuthorization = basicAuthorizationService.findById(id);
        OAuth2Authorization authorization = authorizationConverter.convert(basicAuthorization);

        return Result.success(authorization);
    }

    @DeleteMapping("/offline")
    @Operation(summary = "踢出登录", description = "根据access token撤销认证信息")
    public Result<String> offline(@Valid @RequestBody OfflineAuthorizationRequest request) {
        basicAuthorizationService.offline(request);
        return Result.success();
    }

}
