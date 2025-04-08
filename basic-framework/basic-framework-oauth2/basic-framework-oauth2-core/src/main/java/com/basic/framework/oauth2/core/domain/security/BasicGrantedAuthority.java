package com.basic.framework.oauth2.core.domain.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * 用户权限
 *
 * @author vains
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicGrantedAuthority implements GrantedAuthority {

    @Schema(title = "主键id")
    private Long id;

    @Schema(title = "权限码")
    private String authority;

    @Schema(title = "路径")
    private String path;

    @Schema(title = "权限码")
    private String permission;

    @Schema(title = "请求方式")
    private String requestMethod;

    @Schema(title = "是否需要鉴权")
    private Boolean needAuthentication;

}
