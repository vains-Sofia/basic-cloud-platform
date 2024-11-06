package com.basic.cloud.authorization.server.controller;

import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.storage.core.domain.request.FindAuthorizationPageRequest;
import com.basic.framework.oauth2.storage.core.domain.response.FindAuthorizationPageResponse;
import com.basic.framework.oauth2.storage.core.service.BasicAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证信息接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2/basic/authorization")
@Tag(name = "认证信息接口", description = "认证信息相关接口")
public class OAuth2AuthorizationController {

    private final BasicAuthorizationService basicAuthorizationService;

    @GetMapping("/findByPage")
    @PreAuthorize("hasAnyAuthority('message.read')")
    @Operation(summary = "根据入参分页查询认证信息", description = "根据入参分页查询认证信息")
    public Result<PageResult<FindAuthorizationPageResponse>> findAuthorizationPage(
            FindAuthorizationPageRequest request) {
        PageResult<FindAuthorizationPageResponse> authorizationPage =
                basicAuthorizationService.findAuthorizationPage(request);

        return Result.success(authorizationPage);
    }

}
