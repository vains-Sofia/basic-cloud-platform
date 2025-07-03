package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.SysThirdUserBindClient;
import com.basic.cloud.system.api.domain.request.BindEmailRequest;
import com.basic.cloud.system.api.domain.request.EnhancedThirdUserRequest;
import com.basic.cloud.system.api.domain.response.EnhancedUserResponse;
import com.basic.cloud.system.api.enums.CheckBindingStatusEnum;
import com.basic.cloud.system.service.SysThirdUserBindService;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方登录用户绑定接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class SysThirdUserBindController implements SysThirdUserBindClient {

    private final SysThirdUserBindService thirdUserBindService;

    @Override
    public Result<CheckBindingStatusEnum> checkBinding() {
        CheckBindingStatusEnum bindingStatus = thirdUserBindService.checkBinding();
        return Result.success(bindingStatus, bindingStatus.getDescription());
    }

    @Override
    public Result<EnhancedUserResponse> enhancedThirdUser(EnhancedThirdUserRequest request) {
        EnhancedUserResponse userResponse = thirdUserBindService.enhancedThirdUser(request);
        return Result.success(userResponse);
    }

    @Override
    public Result<CheckBindingStatusEnum> bindEmail(BindEmailRequest request) {
        CheckBindingStatusEnum bindingStatus = thirdUserBindService.bindEmail(request);
        return Result.success(bindingStatus, bindingStatus.getDescription());
    }

    @Override
    public Result<String> sendBindEmailCode(String email) {
        thirdUserBindService.sendBindEmailCode(email);
        return Result.success();
    }

}
