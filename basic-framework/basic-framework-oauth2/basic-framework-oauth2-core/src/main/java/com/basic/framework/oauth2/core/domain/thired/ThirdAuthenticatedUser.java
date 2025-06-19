package com.basic.framework.oauth2.core.domain.thired;

import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 三方用户信息
 *
 * @author vains
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ThirdAuthenticatedUser extends DefaultAuthenticatedUser {

    public ThirdAuthenticatedUser(String name, OAuth2AccountPlatformEnum accountPlatform, Collection<? extends GrantedAuthority> authorities) {
        super(name, accountPlatform, authorities);
    }

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
     * 邮箱
     */
    private String email;

    /**
     * 用户的性别。本规范定义的值为female和 male。当定义的值都不适用时，可以使用其他值。
     */
    private String gender;

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

    /**
     * 三方登录获取的access token
     */
    private String accessToken;

    /**
     * 三方登录获取的refresh token
     */
    private String refreshToken;

    /**
     * access_token 过期时间
     */
    private LocalDateTime expiresAt;

    /**
     * 博客地址
     */
    private String blog;

    /**
     * 检查过，即是否已确认绑定
     */
    private Boolean bindBasicUserChecked;

}
