package com.basic.framework.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 分页出参
 *
 * @author vains
 */
@Data
@Schema(title = "分页请求出参")
@EqualsAndHashCode(callSuper = true)
public abstract class PageResult<T> extends Pageable {

    @Schema(title = "总条数")
    private Long total;

    @Schema(title = "分页数据")
    private List<T> records;

}
