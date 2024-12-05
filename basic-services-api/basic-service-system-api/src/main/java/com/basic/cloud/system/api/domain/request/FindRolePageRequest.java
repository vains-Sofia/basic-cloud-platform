package com.basic.cloud.system.api.domain.request;

import com.basic.framework.core.domain.DataPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询角色入参
 *
 * @author YuJx
 */
@Data
@Schema(name = "分页查询角色信息入参")
@EqualsAndHashCode(callSuper = true)
public class FindRolePageRequest extends DataPageable {

    /**
     * 角色代码
     */
    @Schema(title = "角色代码", description = "角色代码")
    private String code;

    /**
     * 角色名称
     */
    @Schema(title = "角色名称", description = "角色名称")
    private String name;

    /**
     * 角色描述
     */
    @Schema(title = "角色描述", description = "角色描述")
    private String description;
    
}
