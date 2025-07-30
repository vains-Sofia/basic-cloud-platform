package com.basic.cloud.system.api.domain.request;

import com.basic.cloud.system.api.enums.ScanStatusEnum;
import com.basic.framework.core.domain.DataPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询API接口入参
 *
 * @author vains
 */
@Data
@Schema(name = "分页查询API接口信息入参")
@EqualsAndHashCode(callSuper = true)
public class FindApiEndpointPageRequest extends DataPageable {

    /**
     * 请求路径
     */
    @Schema(title = "请求路径", description = "请求路径")
    private String keyword;

    /**
     * 请求方式
     */
    @Schema(title = "请求方式", description = "请求方式")
    private String requestMethod;

    /**
     * 所属模块
     */
    @Schema(title = "所属模块", description = "所属模块")
    private String moduleName;

    /**
     * 扫描状态
     */
    @Schema(title = "扫描状态", description = "扫描状态：1-新发现 2-已存在 3-缺少注解 4-异常")
    private ScanStatusEnum scanStatus;

    /**
     * 扫描批次ID
     */
    @Schema(title = "扫描批次ID", description = "扫描批次ID")
    private Long scanBatchId;

    /**
     * 是否已导入
     */
    @Schema(title = "是否已导入", description = "是否已导入：0-未导入 1-已导入")
    private Boolean imported;
}
