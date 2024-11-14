package com.basic.framework.oauth2.core.domain.oauth2;

import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

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
     * 用户id
     */
    private Long id;

    /**
     * 账号
     */
    private String name;

    /**
     * 密码
     */
    @JsonIgnore
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
     * token签发对象
     */
    private String sub;

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
