package com.basic.cloud.authorization.server.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询scope响应
 *
 * @author vains
 */
@Data
@Schema(name = "查询scope响应")
public class FindScopeResponse implements Serializable {

    @Schema(title = "主键id", description = "主键id")
    private Long id;

    @Schema(title = "scope 名称", description = "scope 名称")
    private String scope;

    @Schema(title = "scope 描述", description = "scope 描述")
    private String description;

    @Schema(title = "是否启用", description = "是否启用")
    private Boolean enabled;
}