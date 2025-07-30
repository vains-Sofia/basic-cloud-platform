package com.basic.cloud.system.api.domain.request;

import com.basic.cloud.system.api.enums.ScanStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询API接口入参
 *
 * @author vains
 */
@Data
@Schema(name = "查询API接口信息入参")
public class FindApiEndpointRequest implements Serializable {

    /**
     * 请求路径
     */
    @Schema(title = "请求路径", description = "请求路径")
    private String path;

    /**
     * 请求方式
     */
    @Schema(title = "请求方式", description = "请求方式")
    private String requestMethod;

    /**
     * 扫描批次ID
     */
    @Schema(title = "扫描批次ID", description = "扫描批次ID")
    private Long scanBatchId;

    /**
     * 扫描状态
     */
    @Schema(title = "扫描状态", description = "扫描状态")
    private ScanStatusEnum scanStatus;
}
