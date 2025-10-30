package com.basic.framework.oauth2.core.property;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 登录相关配置
 *
 * @author vains
 */
@Data
@ConfigurationProperties(prefix = BasicLoginProperties.PREFIX)
public class BasicLoginProperties {

    static final String PREFIX = "basic.cloud.login";

    /**
     * 登录页面地址
     */
    private String loginPageUri = "/login";

    /**
     * 登录处理地址
     */
    private String loginProcessingUri = "/login";

    /**
     * 邮件登录处理地址
     */
    private String emailLoginProcessingUri = "/login/email";

    /**
     * 邮件登录处理地址
     */
    private String qrCodeLoginProcessingUri = "/login/qr-code";

    /**
     * 访问令牌有效期
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration accessTokenTimeToLive = Duration.ofHours(2);

    /**
     * 刷新令牌有效期
     */
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration refreshTokenTimeToLive = Duration.ofDays(30);

    /**
     * 邮件验证码配置
     */
    private BasicLoginCaptchaRequestProperties email = new BasicLoginCaptchaRequestProperties(AuthorizeConstants.EMAIL_PARAMETER);

}
