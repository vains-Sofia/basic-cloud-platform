package com.basic.cloud.authorization.server.domain.response;

import com.basic.framework.oauth2.storage.domain.model.ScopeWithDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 授权确认响应
 *
 * @author vains
 */
@Data
public class OAuth2ConsentResponse implements Serializable {

    @Schema(title = "客户端ID", description = "需要授权的客户端ID")
    private String clientId;

    @Schema(title = "客户端logo", description = "需要授权的客户端logo")
    private String clientLogo;

    @Schema(title = "客户端名称", description = "需要授权的客户端名称")
    private String clientName;

    @Schema(title = "客户端要授权的scope", description = "客户端要授权的scope")
    private Set<ScopeWithDescription> scopes;

    @Schema(title = "已授权的scope", description = "之前已经授权的scope")
    private Set<ScopeWithDescription> previouslyApprovedScopes;

    @Schema(title = "用户名称", description = "需要授权的用户名称")
    private String principalName;

    @Schema(title = "设备码模式-用户码", description = "设备码模式-用户码")
    private String userCode;

    @Schema(title = "oauth2授权申请时传入的state", description = "会原样返回state")
    private String state;

    @Schema(title = "项目的context path", description = "项目的context path")
    private String contextPath;

    @Schema(title = "授权后跳转URI", description = "授权后跳转URI")
    private String requestURI;

}
