package com.basic.cloud.system.api.domain.request;

import com.basic.cloud.system.api.enums.StatusEnum;
import com.basic.framework.core.domain.DataPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典项分页查询请求参数
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictItemPageRequest extends DataPageable {

    @Schema(title = "字典类型编码", description = "字典类型的唯一编码")
    private String typeCode;

    @Schema(title = "模糊查询 itemKey 或 itemValue", description = "模糊查询 itemKey 或 itemValue")
    private String keyword;

    @Schema(title = "状态", description = "状态（Y=启用，N=禁用）")
    private StatusEnum status;
}
