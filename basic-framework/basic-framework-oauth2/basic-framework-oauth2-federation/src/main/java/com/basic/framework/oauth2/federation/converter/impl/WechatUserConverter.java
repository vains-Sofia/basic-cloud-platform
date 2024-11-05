package com.basic.framework.oauth2.federation.converter.impl;

import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import com.basic.framework.oauth2.federation.converter.OAuth2UserConverter;
import com.basic.framework.oauth2.federation.domain.ThirdAuthenticatedUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

/**
 * 微信用户信息转换器
 *
 * @author vains
 */
public class WechatUserConverter implements OAuth2UserConverter {

    @Override
    public AuthenticatedUser convert(OAuth2User oAuth2User) {
        // 获取三方用户信息
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 初始化统一用户信息实例
        ThirdAuthenticatedUser authenticatedUser = new ThirdAuthenticatedUser(
                oAuth2User.getName(), OAuth2AccountPlatformEnum.WECHAT, oAuth2User.getAuthorities());

        // TODO 将openid存入unionId字段中，attributes.get("openid")
        // 转换至 统一用户信息类中
        authenticatedUser.setId(0L);
        authenticatedUser.setThirdUsername(oAuth2User.getName());
        authenticatedUser.setLocation(attributes.get("province") + " " + attributes.get("city"));
        authenticatedUser.setAvatarUrl(String.valueOf(attributes.get("headimgurl")));
        authenticatedUser.setAttributes(attributes);

        // 设置三方access token信息
        setThirdAccessTokenInfo(authenticatedUser, attributes);
        return authenticatedUser;
    }
}