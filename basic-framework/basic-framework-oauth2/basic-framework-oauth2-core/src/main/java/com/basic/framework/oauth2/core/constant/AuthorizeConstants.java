package com.basic.framework.oauth2.core.constant;

import java.util.Set;

/**
 * 鉴权常量
 *
 * @author vains
 */
public class AuthorizeConstants {

    /**
     * 权限在令牌中的key
     */
    public static final String AUTHORITIES = "authorities";

    /**
     * 权限前缀内容，默认为空
     */
    public static final String AUTHORITY_PREFIX = "";

    /**
     * 用户信息缓存前缀
     */
    public static final String USERINFO_PREFIX = "jti:userinfo:";

    /**
     * 存储位置在yml中的key
     */
    public static final String CORE_SERVICE_STORAGE = "basic.cloud.oauth2.server.core-service-storage";

    /**
     * 邮箱验证码邮件参数名
     */
    public static final String EMAIL_PARAMETER = "email";

    /**
     * 邮箱验证码在参数中的名字
     */
    public static final String EMAIL_CAPTCHA_PARAMETER = "captcha";

    /**
     * 默认忽略鉴权的地址
     */
    public static final Set<String> DEFAULT_IGNORE_PATHS = Set.of(
            "/login",
            "/error",
            "/assets/**",
            "/favicon.ico",
            "/login/email",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

}
