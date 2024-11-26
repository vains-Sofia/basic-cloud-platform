package com.basic.cloud.system.api.domain.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * HTTP请求权限控制
 *
 * @author vains
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionAuthority implements Serializable {

    /**
     * 路径
     */
    @Schema(title = "路径")
    private String path;

    /**
     * 权限码
     */
    @Schema(title = "权限码")
    private String permission;

    /**
     * 请求方式
     */

    @Schema(title = "请求方式")
    private String requestMethod;

}
