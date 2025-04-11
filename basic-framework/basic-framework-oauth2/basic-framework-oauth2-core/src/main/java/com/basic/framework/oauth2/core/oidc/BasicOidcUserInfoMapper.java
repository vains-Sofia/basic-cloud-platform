package com.basic.framework.oauth2.core.oidc;

import com.basic.framework.core.enums.BasicEnum;
import com.basic.framework.oauth2.core.enums.GenderEnum;
import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oidc.OidcUserInfoResult;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 自定义oidc用户信息响应，idToken至OidcUser用户信息映射
 *
 * @author vains
 */
@Slf4j
public class BasicOidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {

    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext context) {
        // oidc 用户信息响应bean
        OidcUserInfoResult oidcUserInfoResult = new OidcUserInfoResult();

        // 认证信息
        OAuth2Authorization authorization = context.getAuthorization();
        // oidc token
        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization.getToken(OidcIdToken.class);
        if (oidcIdToken == null) {
            return null;
        }
        // 获取id token
        OidcIdToken idToken = oidcIdToken.getToken();
        Map<String, Object> claims = idToken.getClaims();
        oidcUserInfoResult.setSub(claims.get(StandardClaimNames.SUB).toString());

        // 获取access token
        OAuth2AccessToken accessToken = context.getAccessToken();
        // 获取授权申请的scope
        Set<String> requestedScopes = accessToken.getScopes();

        // 如果有 address 的scope
        if (requestedScopes.contains(OidcScopes.ADDRESS)) {
            AddressStandardClaim address = idToken.getAddress();
            if (address != null) {
                oidcUserInfoResult.setAddress(address.getFormatted());
            }
        }

        // 如果有 email 的scope
        if (requestedScopes.contains(OidcScopes.EMAIL)) {
            oidcUserInfoResult.setEmail(idToken.getEmail());
            oidcUserInfoResult.setEmailVerified(idToken.getEmailVerified());
        }

        // 如果有 phone 的scope
        if (requestedScopes.contains(OidcScopes.PHONE)) {
            oidcUserInfoResult.setPhoneNumber(idToken.getPhoneNumber());
            oidcUserInfoResult.setPhoneNumberVerified(idToken.getPhoneNumberVerified());
        }

        // 如果有 profile 的scope
        if (requestedScopes.contains(OidcScopes.PROFILE)) {
            oidcUserInfoResult.setNickname(idToken.getNickName());
            oidcUserInfoResult.setProfile(idToken.getProfile());
            oidcUserInfoResult.setPicture(idToken.getPicture());
            if (!ObjectUtils.isEmpty(idToken.getGender())) {
                oidcUserInfoResult.setGender(BasicEnum.fromValue(Integer.valueOf(idToken.getGender()), GenderEnum.class));
            }
            oidcUserInfoResult.setBirthdate(idToken.getBirthdate());
            // 最后更新时间戳为null的情况下会有问题，添加额外处理
            if (claims.get(StandardClaimNames.UPDATED_AT) != null) {
                try {
                    Instant updatedAt = idToken.getUpdatedAt();
                    oidcUserInfoResult.setUpdatedAt(updatedAt.getEpochSecond());
                } catch (Exception e) {
                    log.debug("获取id token中用户信息最后更新时间戳失败，{}", e.getMessage());
                }
            }
        }

        // 获取当前认证的用户信息
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        if (authenticatedUser != null) {
            // 设置系统相关的用户属性
            oidcUserInfoResult.setId(authenticatedUser.getId());
            oidcUserInfoResult.setUsername(authenticatedUser.getUsername());
            oidcUserInfoResult.setAccountPlatform(authenticatedUser.getAccountPlatform());
        }

        // 响应bean转为map
        Map<String, Object> scopeRequestedClaims = JsonUtils.objectToObject(oidcUserInfoResult, Map.class, String.class, Object.class);
        return new OidcUserInfo(scopeRequestedClaims);
    }

}
