package com.basic.framework.oauth2.authorization.server.captcha.impl;

import com.basic.framework.oauth2.authorization.server.captcha.CaptchaService;
import com.basic.framework.oauth2.authorization.server.exception.InvalidCaptchaException;
import com.basic.framework.oauth2.core.property.OAuth2ServerCaptchaRequestProperties;
import com.basic.framework.oauth2.core.property.OAuth2ServerProperties;
import com.basic.framework.oauth2.core.util.ServletUtils;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * 基于redis的验证码存储与验证
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class RedisCaptchaService implements CaptchaService {

    private static final String KEY_PREFIX = "captcha:";

    private final OAuth2ServerProperties properties;

    private final RedisOperator<String> redisOperator;

    @Override
    @SneakyThrows
    public void save(String captcha) {
        HttpServletRequest request = ServletUtils.getRequest();
        Assert.notNull(request, "request cannot be null");
        OAuth2ServerCaptchaRequestProperties propertiesEmail = properties.getEmail();
        // yaml中配置的验证码key在请求中的参数名
        String captchaKeyParameter = propertiesEmail.getCaptchaKeyParameter();
        // 从当前请求中获取验证码的key
        String captchaKey = ServletRequestUtils.getStringParameter(request, captchaKeyParameter);

        Assert.notNull(captchaKey, captchaKeyParameter + "不能为空.");
        String key = KEY_PREFIX.concat(captchaKeyParameter).concat(":").concat(captchaKey);
        // 存入缓存中
        redisOperator.set(key, captcha, propertiesEmail.getCaptchaEffective(), propertiesEmail.getTimeUnit());
    }

    @Override
    public void validate() {
        HttpServletRequest request = ServletUtils.getRequest();
        Assert.notNull(request, "request cannot be null");
        OAuth2ServerCaptchaRequestProperties propertiesEmail = properties.getEmail();

        // yaml中配置的验证码key在请求中的参数名
        String captchaKeyParameter = propertiesEmail.getCaptchaKeyParameter();
        try {
            // 从当前请求中获取验证码的key
            String captchaKey = ServletRequestUtils.getStringParameter(request, captchaKeyParameter);

            // 获取请求中携带的验证码
            String codeInRequest = ServletRequestUtils.getStringParameter(request, propertiesEmail.getCaptchaParameter());

            if (ObjectUtils.isEmpty(codeInRequest)) {
                throw new InvalidCaptchaException("验证码不能为空.");
            }

            Assert.notNull(captchaKey, captchaKeyParameter + " 不能为空.");
            String key = KEY_PREFIX.concat(captchaKeyParameter).concat(":").concat(captchaKey);

            // 获取缓存的验证码
            String cacheCaptcha = redisOperator.get(key);
            if (ObjectUtils.isEmpty(cacheCaptcha)) {
                throw new InvalidCaptchaException("验证码已过期，请重新获取");
            }

            if (!codeInRequest.equalsIgnoreCase(cacheCaptcha)) {
                throw new InvalidCaptchaException("验证码错误");
            }
        } catch (ServletRequestBindingException e) {
            throw new InvalidCaptchaException("获取验证码失败，请重试.");
        }
        log.info("验证码验证通过.");
    }
}
