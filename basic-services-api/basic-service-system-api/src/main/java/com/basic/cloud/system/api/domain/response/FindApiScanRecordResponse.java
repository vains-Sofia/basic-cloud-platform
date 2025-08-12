package com.basic.cloud.system.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询API扫描记录响应
 *
 * @author vains
 */
@Data
@Schema(name = "查询API扫描记录响应")
public class FindApiScanRecordResponse implements Serializable {

    /**
     * 主键ID
     */
    @Schema(title = "主键ID", description = "主键ID")
    private Long id;

    /**
     * 扫描时间
     */
    @Schema(title = "扫描时间", description = "扫描时间")
    private LocalDateTime scanTime;

    /**
     * 总接口数
     */
    @Schema(title = "总接口数", description = "总接口数")
    private Integer totalCount;

    /**
     * 新发现接口数
     */
    @Schema(title = "新发现接口数", description = "新发现接口数")
    private Integer newCount;

    /**
     * 已存在接口数
     */
    @Schema(title = "已存在接口数", description = "已存在接口数")
    private Integer existCount;

    /**
     * 缺少注释接口数
     */
    @Schema(title = "缺少注释接口数", description = "缺少注释接口数")
    private Integer missingDescCount;

    /**
     * 扫描结果摘要
     */
    @Schema(title = "扫描结果摘要", description = "扫描结果摘要")
    private String scanResult;

    @Schema(title = "创建人名称")
    private String createName;

    @Schema(title = "修改人名称")
    private String updateName;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "修改时间")
    private LocalDateTime updateTime;
}
