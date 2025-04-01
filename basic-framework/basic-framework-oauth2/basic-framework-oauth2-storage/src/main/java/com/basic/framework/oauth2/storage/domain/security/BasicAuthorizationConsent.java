package com.basic.framework.oauth2.storage.domain.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * oauth2认证确认信息
 *
 * @author vains
 */
@Data
@Schema(name = "oauth2认证确认信息")
public class BasicAuthorizationConsent {

    /**
     * 主键
     */
    @Schema(name = "主键")
    private Long id;

    @Schema(name = "授权确认的客户端")
    private String registeredClientId;

    @Schema(name = "授权确认的用户")
    private String principalName;

    @Schema(name = "授权确认的scope")
    private Set<String> authorities;

    /**
     * 创建人
     */
    @Schema(title = "创建人")
    private Long createBy;

    /**
     * 修改人
     */
    @Schema(title = "修改人")
    private Long updateBy;

    /**
     * 创建人名称
     */
    @Schema(title = "创建人名称")
    private String createName;

    /**
     * 修改人名称
     */
    @Schema(title = "修改人名称")
    private String updateName;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(title = "修改时间")
    private LocalDateTime updateTime;

}
