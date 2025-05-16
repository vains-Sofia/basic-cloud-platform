package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.MailSenderRequest;
import jakarta.validation.Valid;

/**
 * 公共通用接口 Service
 *
 * @author vains
 */
public interface CommonService {

    /**
     * 邮件发送
     *
     * @param request 用户注册入参
     */
    void mailSender(@Valid MailSenderRequest request);

}
