package com.basic.cloud.authorization.server.controller;

import com.basic.cloud.authorization.server.domain.request.FindScopePageRequest;
import com.basic.cloud.authorization.server.domain.request.FindScopeResult;
import com.basic.cloud.authorization.server.service.OAuth2ScopeService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * oauth2客户端的scope相关接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/open/scope")
@Tag(name = "客户端的scope接口", description = "oauth2客户端的scope相关接口")
public class OAuth2ScopeController {

    private final OAuth2ScopeService scopeService;

    @GetMapping("/findByPage")
    @PreAuthorize("hasAnyAuthority('message.read')")
    @Operation(summary = "根据入参分页查询scope信息", description = "根据入参分页查询scope信息")
    public Result<PageResult<FindScopeResult>> findScopePage(FindScopePageRequest request) {

        return Result.success(scopeService.findScopePage(request));
    }

}
