package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.CommonClient;
import com.basic.cloud.system.api.domain.request.MailSenderRequest;
import com.basic.cloud.system.service.CommonService;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
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

    private final CommonService commonService;

    @Override
    public Result<String> mailSender(MailSenderRequest request) {
        String errorMessage = commonService.mailSender(request);
        return ObjectUtils.isEmpty(errorMessage) ? Result.success() : Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
    }

}
