package com.basic.cloud.system.api.domain.response;

import com.basic.cloud.system.api.enums.ScanStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询API接口响应
 *
 * @author vains
 */
@Data
@Schema(name = "查询API接口响应")
public class FindApiEndpointResponse implements Serializable {

    /**
     * 主键ID
     */
    @Schema(title = "主键ID", description = "主键ID")
    private Long id;

    /**
     * 扫描批次ID
     */
    @Schema(title = "扫描批次ID", description = "扫描批次ID")
    private Long scanBatchId;

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
     * 所属模块
     */
    @Schema(title = "所属模块", description = "所属模块")
    private String moduleName;

    /**
     * 权限码
     */
    @Schema(title = "权限码", description = "权限码")
    private String permission;

    /**
     * 接口描述(标题)
     */
    @Schema(title = "接口描述", description = "接口描述(标题)")
    private String title;

    /**
     * 扫描状态
     */
    @Schema(title = "扫描状态", description = "扫描状态：1-新发现 2-已存在 3-缺少注解 4-忽略")
    private ScanStatusEnum scanStatus;

    /**
     * 已存在的接口ID
     */
    @Schema(title = "已存在的接口ID", description = "已存在的接口ID（关联权限表接口id）")
    private Long existingPermissionId;

    /**
     * 是否已导入
     */
    @Schema(title = "是否已导入", description = "是否已导入：0-未导入 1-已导入")
    private Boolean imported;

    /**
     * 导入时间
     */
    @Schema(title = "导入时间", description = "导入时间")
    private LocalDateTime importTime;

    /**
     * 错误信息
     */
    @Schema(title = "错误信息", description = "错误信息")
    private String errorMessage;

    @Schema(title = "创建人名称")
    private String createName;

    @Schema(title = "修改人名称")
    private String updateName;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "修改时间")
    private LocalDateTime updateTime;
}
