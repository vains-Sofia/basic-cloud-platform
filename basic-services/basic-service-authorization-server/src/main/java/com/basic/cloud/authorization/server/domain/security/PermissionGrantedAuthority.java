package com.basic.cloud.authorization.server.domain.security;

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
public class PermissionGrantedAuthority implements GrantedAuthority {

    @Schema(title = "权限码")
    private String authority;

    @Schema(title = "路径")
    private String path;

    @Schema(title = "请求方式")
    private String requestMethod;

}
