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
     * 用户id在令牌中的key
     */
    public static final String USER_ID_KEY = "basic_id";

    /**
     * 用户id与令牌唯一id的映射关系缓存key
     */
    public static final String JTI_USER_HASH = "basic:userinfo:jti:";

    /**
     * 用户信息缓存前缀
     */
    public static final String USERINFO_PREFIX = "basic:userinfo:id:";

    /**
     * 用户信息缓存前缀
     */
    public static final String QR_STATUS_CACHE = "basic:qr-code:status:";

    /**
     * 二维码过期时间，单位为秒，默认为5分钟
     */
    public static final Long EXPIRE_SECONDS = 5 * 60L;

    /**
     * 用户信息缓存前缀
     */
    public static final String IS_CLIENT_CREDENTIALS = "client_credentials";

    /**
     * 所有权限缓存信息
     */
    public static final String ALL_PERMISSIONS = "basic:permission:need_authorization:all";

    /**
     * 存储位置在yml中的key
     */
    public static final String CORE_SERVICE_STORAGE = "basic.cloud.oauth2.server.core-service-storage";

    /**
     * 邮箱验证码邮件参数名
     */
    public static final String EMAIL_PARAMETER = "email";

    /**
     * 代表二维码唯一标识参数的名称
     */
    public static final String QR_CODE_PARAMETER = "token";

    /**
     * 邮箱验证码在参数中的名字
     */
    public static final String EMAIL_CAPTCHA_PARAMETER = "captcha";

    /**
     * scope与权限关联缓存
     */
    public static final String SCOPE_PERMISSION_KEY = "basic:scope:permission:all";

    /**
     * 验证码登录时验证码缓存的前缀
     */
    public static final String CAPTCHA_KEY_PREFIX = "basic:captcha:";

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
