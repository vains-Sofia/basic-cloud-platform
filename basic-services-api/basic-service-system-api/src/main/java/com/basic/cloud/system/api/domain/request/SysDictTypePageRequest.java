package com.basic.cloud.system.api.domain.request;

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
public class SysDictTypePageRequest extends DataPageable {

    @Schema(title = "模糊查询 itemKey 或 itemValue", description = "模糊查询 itemKey 或 itemValue")
    private String keyword;

}
