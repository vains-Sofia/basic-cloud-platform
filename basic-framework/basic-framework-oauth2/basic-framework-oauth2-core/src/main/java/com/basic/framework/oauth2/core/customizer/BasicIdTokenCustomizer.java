package com.basic.framework.oauth2.core.customizer;

import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.thired.ThirdAuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.DefaultAddressStandardClaim;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.OAUTH2_ACCESS_TOKEN;
import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.OAUTH2_ACCOUNT_PLATFORM;

/**
 * 基础idToken自定义接口
 *
 * @author vains
 */
public interface BasicIdTokenCustomizer {

    Set<String> ID_TOKEN_CLAIMS = Set.of(
            IdTokenClaimNames.ISS,
            IdTokenClaimNames.SUB,
            IdTokenClaimNames.AUD,
            IdTokenClaimNames.EXP,
            IdTokenClaimNames.IAT,
            IdTokenClaimNames.AUTH_TIME,
            IdTokenClaimNames.NONCE,
            IdTokenClaimNames.ACR,
            IdTokenClaimNames.AMR,
            IdTokenClaimNames.AZP,
            IdTokenClaimNames.AT_HASH,
            IdTokenClaimNames.C_HASH,
            OAUTH2_ACCESS_TOKEN,
            OAUTH2_ACCOUNT_PLATFORM
    );

    /**
     * 从认证信息中提取符合OpenID Connect规范的用户信息的 Claims
     *
     * @param principal 当前登录用户认证信息
     * @return 当前用户的信息
     */
    default Map<String, Object> extractClaims(Authentication principal) {
        Map<String, Object> claims = new HashMap<>();
        if (principal.getPrincipal() instanceof AuthenticatedUser user) {
            if (user instanceof ThirdAuthenticatedUser oidcUser) {
                claims.put(StandardClaimNames.NICKNAME, oidcUser.getNickname());
                claims.put(StandardClaimNames.PROFILE, oidcUser.getProfile());
                claims.put(StandardClaimNames.PICTURE, oidcUser.getPicture());
                claims.put(StandardClaimNames.GENDER, oidcUser.getGender());
                claims.put(StandardClaimNames.BIRTHDATE, oidcUser.getBirthdate());
                claims.put(StandardClaimNames.UPDATED_AT, oidcUser.getUpdatedAt());
                DefaultAddressStandardClaim.Builder addressBuilder = new DefaultAddressStandardClaim.Builder();
                addressBuilder.formatted(oidcUser.getAddress());
                claims.put(StandardClaimNames.ADDRESS, addressBuilder.build());
                claims.put(StandardClaimNames.EMAIL, oidcUser.getEmail());
                claims.put(StandardClaimNames.PHONE_NUMBER, oidcUser.getPhoneNumber());
                if (!ObjectUtils.isEmpty(oidcUser.getEmail())) {
                    claims.put(StandardClaimNames.EMAIL_VERIFIED, Boolean.TRUE);
                }
                if (!ObjectUtils.isEmpty(oidcUser.getPhoneNumber())) {
                    claims.put(StandardClaimNames.PHONE_NUMBER_VERIFIED, Boolean.TRUE);
                }
            }
        }
        // TODO 本地用户信息设置进id token中

        return new HashMap<>(claims);
    }

}
