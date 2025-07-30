package com.basic.cloud.system.api.domain.request;

import com.basic.cloud.system.api.enums.ScanStatusEnum;
import com.basic.framework.data.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.io.Serializable;

/**
 * 保存或修改API接口信息入参
 *
 * @author vains
 */
@Data
@Schema(name = "保存或修改API接口信息入参")
public class SaveApiEndpointRequest implements Serializable {

    /**
     * 主键id
     */
    @NotNull(groups = Update.class)
    @Schema(title = "主键ID", description = "主键ID")
    private Long id;

    /**
     * 扫描批次ID
     */
    @NotNull(groups = {Update.class, Default.class})
    @Schema(title = "扫描批次ID", description = "扫描批次ID")
    private Long scanBatchId;

    /**
     * 请求路径
     */
    @NotBlank(groups = {Update.class, Default.class})
    @Schema(title = "请求路径", description = "请求路径")
    private String path;

    /**
     * 请求方式
     */
    @NotBlank(groups = {Update.class, Default.class})
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
    @Schema(title = "扫描状态", description = "扫描状态：1-新发现 2-已存在 3-缺少注解 4-异常")
    private ScanStatusEnum scanStatus;

    /**
     * 已存在的接口ID
     */
    @Schema(title = "已存在的接口ID", description = "已存在的接口ID（关联权限表接口id）")
    private Long existingPermissionId;

    /**
     * 错误信息
     */
    @Schema(title = "错误信息", description = "错误信息")
    private String errorMessage;
}
