package com.basic.cloud.authorization.server.controller;

import com.basic.framework.oauth2.storage.core.domain.request.FindScopePageRequest;
import com.basic.framework.oauth2.storage.core.domain.request.ResetScopePermissionRequest;
import com.basic.framework.oauth2.storage.core.domain.request.SaveScopeRequest;
import com.basic.framework.oauth2.storage.core.domain.response.FindScopeResponse;
import com.basic.framework.oauth2.storage.core.service.OAuth2ScopeService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.data.validation.group.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result<PageResult<FindScopeResponse>> findScopePage(@Valid FindScopePageRequest request) {

        return Result.success(scopeService.findScopePage(request));
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('message.write')")
    @Operation(summary = "保存scope信息", description = "保存scope信息")
    public Result<String> saveScope(@RequestBody @Valid SaveScopeRequest request) {
        scopeService.saveScope(request);
        return Result.success();
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('message.write')")
    @Operation(summary = "修改scope信息", description = "修改scope信息")
    public Result<String> updateScope(@RequestBody @Validated(Update.class) SaveScopeRequest request) {
        scopeService.updateScope(request);
        return Result.success();
    }

    @PutMapping("/resetScopePermission")
    @PreAuthorize("hasAnyAuthority('message.write')")
    @Operation(summary = "重置scope对应的权限", description = "重置scope对应的权限")
    public Result<String> resetScopePermission(@RequestBody @Validated ResetScopePermissionRequest request) {
        scopeService.resetScopePermission(request);
        return Result.success();
    }

}
