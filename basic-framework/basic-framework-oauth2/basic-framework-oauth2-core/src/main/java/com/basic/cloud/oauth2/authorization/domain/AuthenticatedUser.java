package com.basic.cloud.oauth2.authorization.domain;

import com.basic.cloud.oauth2.authorization.enums.OAuth2AccountPlatformEnum;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * 统一用户信息
 *
 * @author vains 2023/12/29
 */
public interface AuthenticatedUser extends OAuth2User, UserDetails {

    @Override
    default boolean isAccountNonExpired() {
        return true;
    }

    @Override
    default boolean isAccountNonLocked() {
        return true;
    }

    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    default boolean isEnabled() {
        return true;
    }

    @Override
    default String getUsername() {
        return this.getName();
    }


    @Override
    default String getPassword() {
        return null;
    }

    /**
     * 当三方登录时可通过该方法获取三方登录提供商类型
     *
     * @return 三方登录提供商类型
     */
    OAuth2AccountPlatformEnum getAccountPlatform();

}
