package com.basic.cloud.system.api.domain.request;

import com.basic.framework.core.domain.DataPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 分页查询API扫描记录入参
 *
 * @author vains
 */
@Data
@Schema(name = "分页查询API扫描记录信息入参")
@EqualsAndHashCode(callSuper = true)
public class FindApiScanRecordPageRequest extends DataPageable {

    /**
     * 开始时间
     */
    @Schema(title = "开始时间", description = "扫描开始时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Schema(title = "结束时间", description = "扫描结束时间")
    private LocalDateTime endTime;
}
