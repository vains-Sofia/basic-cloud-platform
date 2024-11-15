package com.basic.cloud.authorization.server.domain.request;

import com.basic.framework.core.domain.Pageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询scope入参
 *
 * @author vains
 */
@Data
@Schema(name = "分页查询scope入参")
@EqualsAndHashCode(callSuper = true)
public class FindScopePageRequest extends Pageable {

    @Schema(title = "scope名称/描述", description = "scope名称/描述")
    private Boolean scope;

    @Schema(title = "是否启用", description = "是否启用")
    private Boolean enabled;

}
