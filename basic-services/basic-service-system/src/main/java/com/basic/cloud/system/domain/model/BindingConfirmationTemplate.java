package com.basic.cloud.system.domain.model;

import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 绑定确认Bean
 *
 * @author vains
 */
@Data
@Builder
public class BindingConfirmationTemplate {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账号所属平台
     */
    private OAuth2AccountPlatformEnum accountPlatform;

    /**
     * 电子邮箱地址
     */
    private String email;

    /**
     * 请求时间
     */
    private String requestTime;

    /**
     * IP 地址
     */
    private String ipAddress;

    /**
     * 确认链接
     */
    private String confirmUrl;

    /**
     * 过期时间
     */
    private String expiresIn;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 当前年份
     */
    private String currentYear;

}
