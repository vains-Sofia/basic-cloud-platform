package com.basic.cloud.oauth2.authorization.domain;

import com.basic.cloud.oauth2.authorization.enums.OAuth2AccountPlatformEnum;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

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

}
