package com.basic.cloud.system.api.domain.response;

import com.basic.framework.oauth2.core.domain.security.BasicGrantedAuthority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

/**
 * 增强的用户信息响应
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnhancedUserResponse extends FindBasicUserResponse {

    /**
     * 角色列表
     */
    @Schema(title = "角色列表")
    private List<String> roles;

    /**
     * 权限列表
     */
    @Schema(title = "权限列表")
    private Set<BasicGrantedAuthority> authorities;

}
