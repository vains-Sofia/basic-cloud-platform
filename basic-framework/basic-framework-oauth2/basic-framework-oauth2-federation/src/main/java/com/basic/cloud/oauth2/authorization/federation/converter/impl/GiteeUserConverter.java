package com.basic.cloud.oauth2.authorization.federation.converter.impl;

import com.basic.cloud.oauth2.authorization.domain.AuthenticatedUser;
import com.basic.cloud.oauth2.authorization.domain.DefaultAuthenticatedUser;
import com.basic.cloud.oauth2.authorization.enums.OAuth2AccountPlatformEnum;
import com.basic.cloud.oauth2.authorization.federation.converter.OAuth2UserConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

/**
 * 转换通过码云登录的用户信息
 *
 * @author vains
 */
@Slf4j
public class GiteeUserConverter implements OAuth2UserConverter {

    @Override
    public AuthenticatedUser convert(OAuth2User oAuth2User) {
        // 获取三方用户信息
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 初始化统一用户信息实例
        DefaultAuthenticatedUser authenticatedUser = new DefaultAuthenticatedUser(
                oAuth2User.getName(), OAuth2AccountPlatformEnum.GITEE, oAuth2User.getAuthorities());

        // 转换至 统一用户信息类中
        authenticatedUser.setThirdUsername(oAuth2User.getName());
        authenticatedUser.setId(String.valueOf(attributes.get("id")));
        authenticatedUser.setBlog(attributes.get("blog") + "");
        authenticatedUser.setAvatarUrl(String.valueOf(attributes.get("avatar_url")));

        // 设置三方access token信息
        setThirdAccessTokenInfo(authenticatedUser, attributes);
        return authenticatedUser;
    }
}