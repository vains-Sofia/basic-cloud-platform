package com.basic.framework.oauth2.core.domain.oidc;

import com.basic.framework.oauth2.core.enums.GenderEnum;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 根据Openid Connect规范解析idToken以后获取的用户信息
 *
 * @author vains
 * @see <a href='https://openid.net/specs/openid-connect-core-1_0.html#StandardClaims'>StandardClaims</a>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OidcUserInfoResult {

    /**
     * 用户账号，发行方的最终用户标识符。
     */
    private String sub;

    /**
     * 昵称
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
     * 用户的首选电子邮件地址。其值必须符合RFC 5322 [RFC5322] addr-spec 语法
     */
    private String email;

    /**
     * 邮箱是否已验证
     */
    private Boolean emailVerified;

    /**
     * 用户的性别。来源《个人基本信息分类与代码 第1部分：人的性别代码》。
     */
    private GenderEnum gender;

    /**
     * 出生日期，以 ISO 8601-1 [ISO8601‑1] YYYY-MM-DD 格式表示。
     */
    private String birthdate;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 手机号是否已验证
     */
    private Boolean phoneNumberVerified;

    /**
     * 用户的首选邮政地址。
     */
    private String address;

    /**
     * 用户信息最后更新时间。以时间戳表示。
     */
    private Long updatedAt;

    // ---------------扩展信息---------------

    /**
     * 自增id
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 账号来源类型
     */
    private OAuth2AccountPlatformEnum accountPlatform;

}
