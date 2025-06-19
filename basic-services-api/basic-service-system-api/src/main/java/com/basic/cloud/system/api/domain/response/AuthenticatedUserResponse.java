package com.basic.cloud.system.api.domain.response;

import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

/**
 * 认证后用户信息响应
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthenticatedUserResponse extends FindBasicUserResponse implements AuthenticatedUser {

    /**
     * 角色列表
     */
    @Schema(title = "角色列表")
    private List<String> roles;

    /**
     * 权限列表
     */
    @Schema(title = "权限列表")
    private Set<GrantedAuthority> authorities;

    /**
     * 检查过，即是否已确认绑定
     */
    @Schema(title = "检查过，即是否已确认绑定")
    private Boolean bindBasicUserChecked;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities == null ? Collections.emptySet() : Collections.unmodifiableSet(new LinkedHashSet<>(authorities));
    }

    @Override
    public void eraseCredentials() {

    }

    @Override
    public String getName() {
        return super.getNickname();
    }
}
