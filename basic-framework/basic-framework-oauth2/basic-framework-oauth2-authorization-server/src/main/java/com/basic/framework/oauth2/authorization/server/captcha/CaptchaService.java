package com.basic.framework.oauth2.authorization.server.captcha;

/**
 * 验证码repository存储接口
 *
 * @author vains
 */
public interface CaptchaService {

    /**
     * 根据当前请求保存验证码
     *
     * @param captcha 验证码
     */
    void save(String captcha);

    /**
     * 根据当前请求去校验验证码
     */
    void validate();

}