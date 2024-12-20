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

    @Schema(title = "主键id")
    private Long id;

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

    /**
     * 是否需要鉴权(针对某些接口在权限表维护以后想控制其需不需要鉴权)
     */
    @Schema(title = "是否需要鉴权")
    private Boolean needAuthentication;

}
