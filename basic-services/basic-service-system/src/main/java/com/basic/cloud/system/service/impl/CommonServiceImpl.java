package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.MailSenderRequest;
import com.basic.cloud.system.service.CommonService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

/**
 * 公共通用接口 Service 实现
 *
 * @author vains
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final JavaMailSender mailSender;

    private final MailProperties mailProperties;

    @Override
    public String mailSender(MailSenderRequest request) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            // 是否包含附件
            boolean hasMultipart = !ObjectUtils.isEmpty(request.getAttachments());
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, hasMultipart, StandardCharsets.UTF_8.name());

            // 发送人
            mimeMessageHelper.setFrom(request.getFrom() + "<" + mailProperties.getUsername() + ">");
            // 收件人
            mimeMessageHelper.setTo(request.getMailTo().toArray(new String[0]));
            // 主题
            mimeMessageHelper.setSubject(request.getSubject());
            // 设置内容
            mimeMessageHelper.setText(request.getContent(), request.getContentIsHtml());
            // 添加附件
            if (hasMultipart) {
                for (MultipartFile attachment : request.getAttachments()) {
                    if (attachment == null || ObjectUtils.isEmpty(attachment.getOriginalFilename())) {
                        continue;
                    }
                    mimeMessageHelper.addAttachment(attachment.getOriginalFilename(), attachment);
                }
            }
            mailSender.send(mimeMessage);
            log.info("{} Send email to {}, Subject {}", request.getFrom(), String.join(",", request.getMailTo()), request.getSubject());
        } catch (Exception e) {
            log.error("邮件发送失败，原因：{}", e.getMessage(), e);
            return e.getMessage();
        }
        return null;
    }

}
