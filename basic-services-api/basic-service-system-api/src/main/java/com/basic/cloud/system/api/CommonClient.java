package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.FilePreSignedRequest;
import com.basic.cloud.system.api.domain.request.MailSenderRequest;
import com.basic.cloud.system.api.domain.response.FilePreSignedResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

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
     * @param request 发送邮件相关数据
     * @return 统一响应
     */
    @PostMapping("/email/sender")
    @Operation(summary = "邮件发送", description = "邮件发送")
    Result<String> mailSender(@Valid @SpringQueryMap MailSenderRequest request);

    /**
     * 文件上传预签名
     *
     * @param request 文件预签名入参
     * @return 统一响应
     */
    @PutMapping("/pre/signed")
    @Operation(summary = "文件上传预签名", description = "文件上传预签名")
    Result<FilePreSignedResponse> uploadPreSigned(@Valid @RequestBody FilePreSignedRequest request);

    /**
     * 文件下载预签名
     *
     * @param request 文件预签名入参
     * @return 统一响应
     */
    @GetMapping("/pre/signed")
    @Operation(summary = "文件下载预签名", description = "文件下载预签名")
    Result<FilePreSignedResponse> downloadPreSigned(@Valid @SpringQueryMap FilePreSignedRequest request);

    /**
     * 删除文件预签名
     *
     * @param request 文件预签名入参
     * @return 统一响应
     */
    @DeleteMapping("/pre/signed")
    @Operation(summary = "删除文件预签名", description = "删除文件预签名")
    Result<FilePreSignedResponse> deletePreSigned(@Valid @SpringQueryMap FilePreSignedRequest request);

}
