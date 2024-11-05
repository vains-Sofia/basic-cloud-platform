package com.basic.framework.oauth2.federation.domain;

import com.basic.framework.oauth2.core.domain.DefaultAuthenticatedUser;
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
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 地址
     */
    private String location;

    /**
     * 三方登录获取的认证信息
     */
    private String credentials;

    /**
     * 三方登录用户名
     */
    private String thirdUsername;

    /**
     * 博客地址
     */
    private String blog;

    /**
     * 三方登录获取的认证信息的过期时间
     */
    private LocalDateTime credentialsExpiresAt;

}
