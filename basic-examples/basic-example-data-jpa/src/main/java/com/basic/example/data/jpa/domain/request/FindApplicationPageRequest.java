package com.basic.example.data.jpa.domain.request;

import com.basic.framework.core.domain.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询客户端入参
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FindApplicationPageRequest extends Pageable {

    @Schema(title = "客户端id", description = "客户端id")
    private String clientId;

    @Schema(title = "客户端名称", description = "客户端名称")
    private String applicationName;

    @Schema(title = "客户端认证方式", description = "客户端认证方式")
    private String clientAuthenticationMethod;

    @Schema(title = "客户端支持的grant type", description = "客户端支持的grant type")
    private String authorizationGrantType;

}
