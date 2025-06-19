package com.basic.framework.oauth2.federation.converter.impl;

import com.basic.framework.oauth2.core.domain.thired.ThirdAuthenticatedUser;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import com.basic.framework.oauth2.federation.converter.OAuth2UserConverter;
import com.basic.framework.oauth2.federation.util.GiteeApiHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * 转换通过码云登录的用户信息
 *
 * @author vains
 */
@Slf4j
public class GiteeUserConverter implements OAuth2UserConverter {

    @Override
    public ThirdAuthenticatedUser convert(OAuth2User oAuth2User) {
        // 获取三方用户信息
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 初始化统一用户信息实例
        ThirdAuthenticatedUser authenticatedUser = new ThirdAuthenticatedUser(
                oAuth2User.getName(), OAuth2AccountPlatformEnum.GITEE, oAuth2User.getAuthorities());

        // 转换至 统一用户信息类中
        authenticatedUser.setNickname(oAuth2User.getName());
        authenticatedUser.setId(Long.parseLong(String.valueOf(attributes.get("id"))));
        authenticatedUser.setSub(String.valueOf(attributes.get("login")));
        authenticatedUser.setBlog(attributes.get("blog") + "");
        authenticatedUser.setProfile(attributes.get("html_url") + "");
        authenticatedUser.setPicture(attributes.get("avatar_url") + "");
        authenticatedUser.setEmail(attributes.get("email") + "");
        if (Objects.equals(authenticatedUser.getEmail(), "null")) {
            // 不知道为什么会出现 email 为 "null" 的情况...
            authenticatedUser.setEmail(null);
        }

        // 解析修改时间
        try {
            // Step 1: 解析为 OffsetDateTime
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(attributes.get("updated_at") + "", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            // Step 2: 转换为 Instant
            Instant instant = offsetDateTime.toInstant();
            // 获取秒级时间戳
            long updateAt = instant.getEpochSecond();
            authenticatedUser.setUpdatedAt(updateAt);
        } catch (Exception e) {
            log.debug("Gitee用户信息最后修改时间解析失败，{}", e.getMessage());
        }

        authenticatedUser.setAttributes(attributes);

        // 设置三方access token信息
        setThirdAccessTokenInfo(authenticatedUser, attributes);
        // 如果用户没有公开邮箱，则尝试通过 Gitee API 获取主邮箱
        if (ObjectUtils.isEmpty(authenticatedUser.getEmail())) {
            String primaryEmail = GiteeApiHelper.fetchPrimaryEmail(authenticatedUser.getAccessToken());
            if (!ObjectUtils.isEmpty(primaryEmail)) {
                authenticatedUser.setEmail(primaryEmail);
            }
        }
        return authenticatedUser;
    }
}