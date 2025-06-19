package com.basic.framework.oauth2.federation.converter.impl;

import com.basic.framework.oauth2.core.domain.thired.ThirdAuthenticatedUser;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import com.basic.framework.oauth2.federation.converter.OAuth2UserConverter;
import com.basic.framework.oauth2.federation.util.GithubApiHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.Map;

/**
 * 转换通过Github登录的用户信息
 *
 * @author vains
 */
@Slf4j
public class GithubUserConverter implements OAuth2UserConverter {

    @Override
    public ThirdAuthenticatedUser convert(OAuth2User oAuth2User) {
        // 获取三方用户信息
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 初始化统一用户信息实例
        ThirdAuthenticatedUser authenticatedUser = new ThirdAuthenticatedUser(
                oAuth2User.getName(), OAuth2AccountPlatformEnum.GITHUB, oAuth2User.getAuthorities());

        // 转换至 统一用户信息类中
        // 转换至 统一用户信息类中
        authenticatedUser.setNickname(oAuth2User.getName());
        authenticatedUser.setSub(String.valueOf(attributes.get("login")));
        authenticatedUser.setBlog(attributes.get("blog") + "");
        authenticatedUser.setProfile(attributes.get("html_url") + "");
        authenticatedUser.setPicture(attributes.get("avatar_url") + "");
        authenticatedUser.setAddress(attributes.get("location") + "");
        authenticatedUser.setId(Long.parseLong(String.valueOf(attributes.get("id"))));

        // 解析修改时间
        try {
            // 转换为 Instant
            Instant instant = Instant.parse(attributes.get("updated_at") + "");
            // 获取秒级时间戳
            long updateAt = instant.getEpochSecond();
            authenticatedUser.setUpdatedAt(updateAt);
        } catch (Exception e) {
            log.debug("Github用户信息最后修改时间解析失败，{}", e.getMessage());
        }

        authenticatedUser.setAttributes(attributes);

        // 设置三方access token信息
        setThirdAccessTokenInfo(authenticatedUser, attributes);

        // 获取用户的主邮箱
        String primaryEmail = GithubApiHelper.fetchPrimaryEmail(authenticatedUser.getAccessToken());
        authenticatedUser.setEmail(primaryEmail);
        return authenticatedUser;
    }
}
