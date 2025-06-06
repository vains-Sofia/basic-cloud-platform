package com.basic.framework.oauth2.core.core;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;

/**
 * oauth2认证参数名
 *
 * @author vains
 */
public class BasicOAuth2ParameterNames {

    /**
     * {@code email } - used in Access Token Request.
     */
    public static final String EMAIL = AuthorizeConstants.EMAIL_PARAMETER;

    /**
     * {@code captcha } - used in Access Token Request.
     */
    public static final String EMAIL_CAPTCHA = AuthorizeConstants.EMAIL_CAPTCHA_PARAMETER;

    /**
     * 三方登录相关参数——登录类型
     */
    public static final String OAUTH2_ACCOUNT_PLATFORM = "account_platform";

    /**
     * 三方登录相关参数——accessToken在用户信息中的属性
     */
    public static final String OAUTH2_ACCESS_TOKEN = "accessToken";

    /**
     * 微信登录相关参数——uniqueId：用户唯一id
     */
    public static final String TOKEN_UNIQUE_ID = "uniqueId";

    /**
     * 微信登录相关参数——forcePopup：强制此次授权需要用户弹窗确认
     */
    public static final String WECHAT_PARAMETER_FORCE_POPUP = "forcePopup";

    /**
     * 微信登录相关参数——appid：微信的应用id
     */
    public static final String WECHAT_PARAMETER_APPID = "appid";

    /**
     * 微信登录相关参数——secret：微信的应用秘钥
     */
    public static final String WECHAT_PARAMETER_SECRET = "secret";

    /**
     * 微信登录相关参数——openid：用户唯一id
     */
    public static final String WECHAT_PARAMETER_OPENID = "openid";

    /**
     * 三方登录类型——微信
     */
    public static final String THIRD_LOGIN_WECHAT = "wechat";

    /**
     * 三方登录类型——Gitee
     */
    public static final String THIRD_LOGIN_GITEE = "gitee";

    /**
     * 三方登录类型——Github
     */
    public static final String THIRD_LOGIN_GITHUB = "github";

}
