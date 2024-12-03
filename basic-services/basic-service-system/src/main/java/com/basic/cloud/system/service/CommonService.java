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
     * @return 如果发送失败则会返回错误信息
     */
    String mailSender(@Valid MailSenderRequest request);

}
