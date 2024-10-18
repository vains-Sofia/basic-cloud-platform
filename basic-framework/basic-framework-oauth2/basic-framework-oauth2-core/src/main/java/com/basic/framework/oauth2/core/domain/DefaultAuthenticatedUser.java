package com.basic.framework.oauth2.core.domain;

import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 统一用户信息 默认实现
 *
 * @author vains
 */
@Data
@Slf4j
@NoArgsConstructor
public class DefaultAuthenticatedUser implements AuthenticatedUser {

    /**
     * 账号
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 授权申请到的权限(scope)
     */
    private Set<GrantedAuthority> authorities;

    /**
     * OAuth2 登录三方提供商类型
     */
    private OAuth2AccountPlatformEnum accountPlatform;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String sub;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 地址
     */
    private String location;

    /**
     * 三方登录获取的认证信息
     */
    private String credentials;

    /**
     * 三方登录用户名
     */
    private String thirdUsername;

    /**
     * 博客地址
     */
    private String blog;

    /**
     * 三方登录获取的认证信息的过期时间
     */
    private LocalDateTime credentialsExpiresAt;

    @JsonIgnore
    private Map<String, Object> attributes = new HashMap<>();

    public DefaultAuthenticatedUser(String name, OAuth2AccountPlatformEnum accountPlatform, Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.accountPlatform = accountPlatform;
        this.authorities = (authorities != null)
                ? Collections.unmodifiableSet(new LinkedHashSet<>(this.sortAuthorities(authorities)))
                : Collections.unmodifiableSet(new LinkedHashSet<>(AuthorityUtils.NO_AUTHORITIES));
    }

    private Set<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(
                Comparator.comparing(GrantedAuthority::getAuthority));
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities == null ? Collections.emptySet() : Collections.unmodifiableSet(new LinkedHashSet<>(authorities));
    }
}
