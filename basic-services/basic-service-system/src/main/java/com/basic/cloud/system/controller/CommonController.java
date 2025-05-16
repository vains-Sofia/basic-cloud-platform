package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.CommonClient;
import com.basic.cloud.system.api.domain.request.FilePreSignedRequest;
import com.basic.cloud.system.api.domain.request.MailSenderRequest;
import com.basic.cloud.system.api.domain.response.FilePreSignedResponse;
import com.basic.cloud.system.service.CommonService;
import com.basic.cloud.system.service.FileService;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公共通用接口
 *
 * @author vains
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CommonController implements CommonClient {

    private final FileService fileService;

    private final CommonService commonService;

    @Override
    public Result<String> mailSender(MailSenderRequest request) {
        commonService.mailSender(request);
        return Result.success();
    }

    @Override
    public Result<FilePreSignedResponse> uploadPreSigned(FilePreSignedRequest request) {
        FilePreSignedResponse response = fileService.filePreSigned(request);
        return Result.success(response);
    }

    @Override
    public Result<FilePreSignedResponse> downloadPreSigned(FilePreSignedRequest request) {
        FilePreSignedResponse response = fileService.filePreSigned(request);
        return Result.success(response);
    }

    @Override
    public Result<FilePreSignedResponse> deletePreSigned(FilePreSignedRequest request) {
        FilePreSignedResponse response = fileService.filePreSigned(request);
        return Result.success(response);
    }

}
