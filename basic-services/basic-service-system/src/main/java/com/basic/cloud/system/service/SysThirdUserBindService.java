package com.basic.cloud.system.service;

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
     */
    void registerBasicUser(ThirdAuthenticatedUser thirdUser);

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
}
