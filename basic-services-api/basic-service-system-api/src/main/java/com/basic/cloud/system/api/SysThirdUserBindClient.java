package com.basic.cloud.system.api;

import com.basic.cloud.system.api.enums.CheckBindingStatusEnum;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 第三方用户绑定 api
 *
 * @author vains
 */
@RequestMapping("/third/user")
@Tag(name = "第三方用户绑定api", description = "第三方用户绑定api")
@FeignClient(name = FeignConstants.SYSTEM_APPLICATION, path = FeignConstants.SYSTEM_CONTEXT_PATH, contextId = "SysThirdUserBindClient")
public interface SysThirdUserBindClient {

    /**
     * 检查当前三方用户是否绑定本地用户
     *
     * @return 结果
     */
    @GetMapping("/check-binding")
    @Operation(summary = "检查当前三方用户是否绑定本地用户", description = "检查当前三方用户是否绑定本地用户")
    Result<CheckBindingStatusEnum> checkBinding();

}
