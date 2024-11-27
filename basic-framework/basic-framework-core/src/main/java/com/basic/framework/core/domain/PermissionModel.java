package com.basic.framework.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限类
 *
 * @author vains
 */
@Data
public class PermissionModel implements Serializable {

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
