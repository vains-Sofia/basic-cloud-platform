package com.basic.framework.oauth2.federation.converter.impl;

import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.core.enums.OAuth2AccountPlatformEnum;
import com.basic.framework.oauth2.federation.converter.OAuth2UserConverter;
import com.basic.framework.oauth2.core.domain.thired.ThirdAuthenticatedUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Objects;

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

        // 转换至 统一用户信息类中
        authenticatedUser.setSub(attributes.get("openid") + "");
        authenticatedUser.setNickname(oAuth2User.getName());
        // 地址
        String address = attributes.get("country") + " " + attributes.get("province") + " " + attributes.get("city");
        authenticatedUser.setAddress(address);
        authenticatedUser.setPicture(String.valueOf(attributes.get("headimgurl")));

        // 处理性别
        String sex = String.valueOf(attributes.get("sex"));
        String gender;
        if (Objects.equals(sex, "1")) {
            gender = "male";
        } else if (Objects.equals(sex, "2")) {
            gender = "female";
        } else {
            gender = "null";
        }
        authenticatedUser.setGender(gender);
        authenticatedUser.setAttributes(attributes);

        // 设置三方access token信息
        setThirdAccessTokenInfo(authenticatedUser, attributes);
        return authenticatedUser;
    }
}