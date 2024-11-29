package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.MailSenderRequest;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公共通用接口
 *
 * @author vains
 */
@RequestMapping("/common")
@Tag(name = "公共通用接口", description = "公共通用接口")
@FeignClient(name = FeignConstants.SYSTEM_APPLICATION, path = FeignConstants.SYSTEM_CONTEXT_PATH, contextId = "CommonClient")
public interface CommonClient {

    /**
     * 邮件发送
     *
     * @param request 用户注册入参
     * @return 统一响应
     */
    @PostMapping("/email/sender")
    @Operation(summary = "邮件发送", description = "邮件发送")
    Result<String> mailSender(@Valid @SpringQueryMap MailSenderRequest request);

}
