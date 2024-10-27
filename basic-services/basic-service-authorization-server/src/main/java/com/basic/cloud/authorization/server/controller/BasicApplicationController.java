package com.basic.cloud.authorization.server.controller;

import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.storage.core.domain.BasicApplication;
import com.basic.framework.oauth2.storage.core.service.BasicApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "客户端接口", description = "客户端接口")
public class BasicApplicationController {

    private final BasicApplicationService applicationService;

    @GetMapping("/findByClientId")
    @PreAuthorize("hasAnyAuthority('message.read')")
    @Parameter(name = "clientId", description = "客户端id")
    @Operation(summary = "根据客户端id查询客户端信息", description = "根据客户端id查询客户端信息")
    public Result<BasicApplication> findByClientId(String clientId) {
        BasicApplication application = applicationService.findByClientId(clientId);
        return Result.success(application);
    }

}
