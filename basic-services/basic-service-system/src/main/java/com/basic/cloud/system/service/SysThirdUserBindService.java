package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.BindEmailRequest;
import com.basic.cloud.system.api.domain.request.EnhancedThirdUserRequest;
import com.basic.cloud.system.api.domain.response.EnhancedUserResponse;
import com.basic.cloud.system.api.enums.CheckBindingStatusEnum;
import com.basic.cloud.system.domain.model.ConfirmSuccessTemplate;
import com.basic.framework.oauth2.core.domain.thired.ThirdAuthenticatedUser;

/**
 * 三方登录用户绑定服务接口
 *
 * @author vains
 */
public interface SysThirdUserBindService {

    /**
     * 检查用户绑定状态
     *
     * @return 绑定状态枚举
     */
    CheckBindingStatusEnum checkBinding();

    /**
     * 根据三方用户信息自动生成用户信息
     *
     * @param thirdUser 三方用户
     * @return 用户id
     */
    Long registerBasicUser(ThirdAuthenticatedUser thirdUser);

    /**
     * 发送绑定确认消息
     *
     * @param thirdUser    三方用户信息
     * @param confirmToken 确认绑定token
     */
    void sendBindConfirmation(ThirdAuthenticatedUser thirdUser, String confirmToken);

    /**
     * 确认绑定
     *
     * @param confirmToken 确认绑定token
     */
    ConfirmSuccessTemplate confirm(String confirmToken);

    /**
     * 获取增强的三方用户信息
     *
     * @param request 增强三方用户请求参数
     * @return 增强的三方用户信息
     */
    EnhancedUserResponse enhancedThirdUser(EnhancedThirdUserRequest request);

    /**
     * 绑定邮箱
     *
     * @param request 邮箱绑定请求参数
     * @return 绑定状态
     */
    CheckBindingStatusEnum bindEmail(BindEmailRequest request);

    /**
     * 发送绑定邮箱验证码
     *
     * @param email 电子邮箱地址
     */
    void sendBindEmailCode(String email);
}
