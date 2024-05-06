package com.basic.cloud.authorization.server.controller;

import com.basic.cloud.authorization.server.domain.vo.CaptchaResultVo;
import com.basic.cloud.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 验证码接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "验证码接口", description = "生成验证码并返回")
public class CaptchaController {

    @GetMapping("/getCaptcha")
    @Operation(summary = "获取验证码", description = "获取验证码")
    public Result<CaptchaResultVo> getCaptcha() {
        // 字符串挑选模板
        String baseCharNumber = "abcdefghijklmnopqrstuvwxyz".toUpperCase() + "abcdefghijklmnopqrstuvwxyz0123456789";
        // 随机字符串长度
        int randomLength = 4;

        StringBuilder captchaBuilder = new StringBuilder(randomLength);

        for (int i = 0; i < randomLength; ++i) {
            int number = ThreadLocalRandom.current().nextInt(baseCharNumber.length());
            captchaBuilder.append(baseCharNumber.charAt(number));
        }

        String captcha = captchaBuilder.toString();
        // 生成一个唯一id
        String id = UUID.randomUUID().toString();
        // 存入缓存中，5分钟后过期
        return Result.success(new CaptchaResultVo(id, captcha), "获取验证码成功.");
    }

    @GetMapping("/getSmsCaptcha")
    @Parameter(name = "手机号", description = "验证码将发送至该手机号中.")
    @Operation(summary = "获取短信验证码", description = "获取短信验证码")
    public Result<String> getSmsCaptcha(String phone) {
        // 示例项目，固定1234
        String smsCaptcha = "666999";
        // 存入缓存中，5分钟后过期
        return Result.success("获取短信验证码成功.", smsCaptcha);
    }

    @GetMapping("/getEmailCaptcha")
    @Parameter(name = "邮箱", description = "验证码将发送至该邮箱中.")
    @Operation(summary = "获取邮件验证码", description = "获取邮件验证码")
    public Result<String> getEmailCaptcha(String email) {
        // 示例项目，固定1234
        String smsCaptcha = "666999";
        // 存入缓存中，5分钟后过期
        return Result.success("获取短信验证码成功.", smsCaptcha);
    }

}
