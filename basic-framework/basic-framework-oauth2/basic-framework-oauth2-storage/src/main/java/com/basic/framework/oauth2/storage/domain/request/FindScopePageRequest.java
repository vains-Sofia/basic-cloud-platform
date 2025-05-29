package com.basic.framework.oauth2.storage.domain.request;

import com.basic.framework.core.domain.DataPageable;
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
public class FindScopePageRequest extends DataPageable {

    @Schema(title = "scope名称/描述", description = "scope名称/描述")
    private String name;

    @Schema(title = "scope编码", description = "scope编码")
    private String scope;

    @Schema(title = "是否启用", description = "是否启用")
    private Boolean enabled;

}
