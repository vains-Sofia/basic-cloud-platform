package com.basic.framework.oauth2.core.customizer;

import com.basic.framework.core.constants.DateFormatConstants;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oauth2.BasicAuthenticatedUser;
import com.basic.framework.oauth2.core.domain.security.BasicGrantedAuthority;
import com.basic.framework.oauth2.core.domain.security.ScopePermissionModel;
import com.basic.framework.oauth2.core.domain.thired.ThirdAuthenticatedUser;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.DefaultAddressStandardClaim;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.util.ObjectUtils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.OAUTH2_ACCESS_TOKEN;
import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.OAUTH2_ACCOUNT_PLATFORM;

/**
 * 基础idToken自定义接口
 *
 * @author vains
 */
@RequiredArgsConstructor
public class BasicIdTokenCustomizer {

    private final RedisOperator<List<ScopePermissionModel>> scopePermissionOperator;

    private final RedisOperator<Map<String, List<BasicGrantedAuthority>>> permissionRedisOperator;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_FORMAT);

    static final Set<String> ID_TOKEN_CLAIMS = Set.of(
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
    public static Map<String, Object> extractClaims(Authentication principal) {
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
            if (user instanceof BasicAuthenticatedUser basicAuthenticatedUser) {
                claims.put(StandardClaimNames.NICKNAME, basicAuthenticatedUser.getNickname());
                claims.put(StandardClaimNames.PROFILE, basicAuthenticatedUser.getProfile());
                claims.put(StandardClaimNames.PICTURE, basicAuthenticatedUser.getPicture());
                claims.put(StandardClaimNames.EMAIL, basicAuthenticatedUser.getEmail());
                claims.put(StandardClaimNames.EMAIL_VERIFIED, basicAuthenticatedUser.getEmailVerified());
                if (basicAuthenticatedUser.getGender() != null) {
                    claims.put(StandardClaimNames.GENDER, basicAuthenticatedUser.getGender().getValue());
                }
                if (basicAuthenticatedUser.getBirthdate() != null) {
                    claims.put(StandardClaimNames.BIRTHDATE, FORMATTER.format(basicAuthenticatedUser.getBirthdate()));
                }
                claims.put(StandardClaimNames.PHONE_NUMBER, basicAuthenticatedUser.getPhoneNumber());
                claims.put(StandardClaimNames.PHONE_NUMBER_VERIFIED, basicAuthenticatedUser.getPhoneNumberVerified());
                if (!ObjectUtils.isEmpty(basicAuthenticatedUser.getAddress())) {
                    DefaultAddressStandardClaim.Builder addressBuilder = new DefaultAddressStandardClaim.Builder();
                    addressBuilder.formatted(basicAuthenticatedUser.getAddress());
                    claims.put(StandardClaimNames.ADDRESS, addressBuilder.build());
                }
                // 获取修改时间戳
                claims.put(StandardClaimNames.UPDATED_AT, basicAuthenticatedUser.getUpdateTime().atZone(ZoneId.systemDefault()).toEpochSecond());
            }

        }

        return new HashMap<>(claims);
    }

    /**
     * 将scope中包含的权限信息和用户的权限信息合并，重置用户拥有的权限
     *
     * @param user             用户信息
     * @param authorizedScopes 授权确认的scope
     */
    public void transferScopesAuthorities(AuthenticatedUser user, Set<String> authorizedScopes) {
        // 查询scope关联的权限
        List<ScopePermissionModel> scopePermissionList = scopePermissionOperator.get(AuthorizeConstants.SCOPE_PERMISSION_KEY);
        if (ObjectUtils.isEmpty(scopePermissionList)) {
            return;
        }
        // 提取权限id
        Set<Long> permissionsId = scopePermissionList.stream()
                .filter(permissionModel -> authorizedScopes.contains(permissionModel.getScope()))
                .map(ScopePermissionModel::getPermissionId)
                .collect(Collectors.toSet());

        // 获取缓存中管理的权限信息
        Map<String, List<BasicGrantedAuthority>> permissionsMap = permissionRedisOperator.get(AuthorizeConstants.ALL_PERMISSIONS);
        // scope对应的权限信息
        List<BasicGrantedAuthority> grantedAuthorities = permissionsMap.values().stream()
                .flatMap(e -> e.stream()
                        .map(p -> {
                            BasicGrantedAuthority authority = new BasicGrantedAuthority();
                            authority.setRequestMethod(p.getRequestMethod());
                            authority.setId(p.getId());
                            authority.setPath(p.getPath());
                            authority.setAuthority(p.getPermission());
                            authority.setNeedAuthentication(p.getNeedAuthentication());
                            return authority;
                        })
                )
                .filter(e -> permissionsId.contains(e.getId()))
                .toList();
        // 合并用户原有权限
        Set<GrantedAuthority> newAuthorities = new HashSet<>(grantedAuthorities);
        if (!ObjectUtils.isEmpty(user.getAuthorities())) {
            newAuthorities.addAll(user.getAuthorities());
        }
        user.setAuthorities(newAuthorities);
    }
}
