package com.basic.cloud.authorization.server.controller;

import com.basic.cloud.authorization.server.domain.response.CaptchaResponse;
import com.basic.framework.core.domain.Result;
import com.basic.framework.core.util.RandomUtils;
import com.basic.framework.data.validation.annotation.Phone;
import com.basic.framework.oauth2.authorization.server.captcha.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 验证码接口
 *
 * @author vains
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "验证码接口", description = "生成验证码并返回")
public class CaptchaController {

    private final JavaMailSender mailSender;

    private final CaptchaService captchaService;

    private final MailProperties mailProperties;

    @GetMapping("/getCaptcha")
    @Operation(summary = "获取验证码", description = "获取验证码")
    public Result<CaptchaResponse> getCaptcha() {
        // 随机字符串长度
        int randomLength = 4;

        String captcha = RandomUtils.randomString(randomLength);
        // 生成一个唯一id
        String id = UUID.randomUUID().toString();
        // 存入缓存中，5分钟后过期
        return Result.success(new CaptchaResponse(id, captcha), "获取验证码成功.");
    }

    @GetMapping("/getSmsCaptcha")
    @Parameter(name = "phone", description = "验证码将发送至该手机号中.")
    @Operation(summary = "获取短信验证码", description = "获取短信验证码")
    public Result<String> getSmsCaptcha(@Phone String phone) {
        // 示例项目，固定1234
        String smsCaptcha = "666999";
        // 存入缓存中，5分钟后过期
        return Result.success("获取短信验证码成功.", smsCaptcha);
    }

    @GetMapping("/getEmailCaptcha")
    @Parameter(name = "email", description = "验证码将发送至该邮箱中.")
    @Operation(summary = "获取邮件验证码", description = "获取邮件验证码")
    public Result<String> getEmailCaptcha(@Email String email) {
        // 随机字符串长度
        int randomLength = 4;
        String captcha = RandomUtils.randomNumber(randomLength);

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("(Basic cloud platform)" + mailProperties.getUsername());
        message.setTo(email);
        message.setSubject("Your Verification Code");
        message.setText("Your verification code is: " + captcha);
        log.info("Send email to {}, Verification Code {}", email, captcha);
        mailSender.send(message);

        this.captchaService.save(captcha);
        return Result.success("获取验证码成功.");
    }

}
