package com.basic.cloud.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分页请求入参
 *
 * @author vains
 */
@Data
public abstract class PageRequest {

    /**
     * 当前页码
     */
    @Min(1)
    @NotNull
    @Schema(description = "当前页码")
    private Long current;

    /**
     * 每页行数
     */
    @Min(1)
    @NotNull
    @Schema(description = "每页行数")
    private Long size;

}
