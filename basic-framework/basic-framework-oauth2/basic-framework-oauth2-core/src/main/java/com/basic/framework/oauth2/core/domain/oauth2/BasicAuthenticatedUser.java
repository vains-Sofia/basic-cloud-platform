package com.basic.framework.oauth2.core.domain.oauth2;

import com.basic.framework.oauth2.core.enums.GenderEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 系统基础用户信息
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasicAuthenticatedUser extends DefaultAuthenticatedUser {

    /**
     * 账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户个人资料页面的 URL。
     */
    private String profile;

    /**
     * 用户个人资料图片的 URL。此 URL 必须指向图像文件（例如，PNG、JPEG 或 GIF 图像文件），而不是指向包含图像的网页。
     */
    private String picture;

    /**
     * 用户的首选电子邮件地址。其值必须符合RFC 5322 [RFC5322] addr-spec 语法。
     */
    private String email;

    /**
     * 邮箱是否验证过
     */
    private Boolean emailVerified;

    /**
     * 用户性别
     */
    private GenderEnum gender;

    /**
     * 出生日期。以 ISO 8601-1 [ISO8601‑1] YYYY-MM-DD 格式表示。
     */
    private LocalDate birthdate;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 手机号是否已验证
     */
    private Boolean phoneNumberVerified;

    /**
     * 用户的首选邮政地址
     */
    private String address;

    /**
     * 是否已删除
     */
    private Boolean deleted;

    /**
     * 创建人名称
     */
    private String createName;

    /**
     * 修改人名称
     */
    private String updateName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}
