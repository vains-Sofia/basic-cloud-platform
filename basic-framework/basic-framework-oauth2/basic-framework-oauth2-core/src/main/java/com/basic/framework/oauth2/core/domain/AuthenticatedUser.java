package com.basic.framework.oauth2.core.domain;

import com.basic.framework.core.enums.OAuth2AccountPlatformEnum;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;

/**
 * 统一用户信息
 *
 * @author vains 2023/12/29
 */
public interface AuthenticatedUser extends OAuth2User, UserDetails, CredentialsContainer {

    @Override
    default String getUsername() {
        return this.getName();
    }


    @Override
    default String getPassword() {
        return null;
    }

    /**
     * 获取用户id
     * TODO 待优化
     *
     * @return 用户id
     */
    Long getId();

    /**
     * 当三方登录时可通过该方法获取三方登录提供商类型
     *
     * @return 三方登录提供商类型
     */
    OAuth2AccountPlatformEnum getAccountPlatform();

    void setAuthorities(Collection<? extends GrantedAuthority> authorities);

}
