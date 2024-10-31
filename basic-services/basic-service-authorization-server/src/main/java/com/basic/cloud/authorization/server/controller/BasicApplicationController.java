package com.basic.cloud.authorization.server.controller;

import com.basic.framework.data.validation.group.Update;
import com.basic.framework.oauth2.storage.core.domain.request.FindApplicationPageRequest;
import com.basic.framework.oauth2.storage.core.domain.request.SaveApplicationRequest;
import com.basic.framework.oauth2.storage.core.domain.response.BasicApplicationResponse;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.storage.core.domain.BasicApplication;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 客户端接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2/application")
@Tag(name = "客户端接口", description = "客户端接口")
public class BasicApplicationController {

    private final BasicApplicationService applicationService;

    @GetMapping("/findByClientId")
    @PreAuthorize("hasAnyAuthority('message.read')")
    @Parameter(name = "clientId", description = "客户端id")
    @Operation(summary = "根据客户端id查询客户端信息", description = "根据客户端id查询客户端信息")
    public Result<BasicApplicationResponse> findByClientId(String clientId) {
        BasicApplication application = applicationService.findByClientId(clientId);
        BasicApplicationResponse response = new BasicApplicationResponse();
        BeanUtils.copyProperties(application, response);
        return Result.success(response);
    }

    @GetMapping("/findByPage")
//    @PreAuthorize("hasAnyAuthority('message.read')")
    @Operation(summary = "根据入参分页查询客户端信息", description = "根据入参分页查询客户端信息")
    public Result<PageResult<BasicApplicationResponse>> findByPage(@Validated FindApplicationPageRequest request) {
        PageResult<BasicApplicationResponse> result = applicationService.findByPage(request);
        return Result.success(result);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('message.write')")
    @Operation(summary = "保存客户端信息", description = "保存客户端信息")
    public Result<String> saveApplication(@RequestBody @Valid SaveApplicationRequest request) {
        String password = applicationService.saveApplication(request);
        return Result.success(password);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('message.write')")
    @Operation(summary = "更新客户端信息", description = "更新保存客户端信息")
    public Result<String> updateApplication(@RequestBody @Validated(Update.class) SaveApplicationRequest request) {
        applicationService.updateApplication(request);
        return Result.success();
    }

    @DeleteMapping("/remove/{clientId}")
    @PreAuthorize("hasAnyAuthority('message.write')")
    @Operation(summary = "根据客户端id删除客户端", description = "根据客户端id删除客户端")
    public Result<String> remove(@PathVariable @Validated String clientId) {
        applicationService.removeByClientId(clientId);
        return Result.success();
    }

}
