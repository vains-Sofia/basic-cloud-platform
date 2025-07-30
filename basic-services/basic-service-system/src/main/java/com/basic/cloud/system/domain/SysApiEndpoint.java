package com.basic.cloud.system.domain;

import com.basic.cloud.system.api.enums.ScanStatusEnum;
import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 扫描接口详情表
 * <p>
 * 该表用于存储扫描到的接口信息，包括请求路径、请求方法、所属模块等。
 * </p>
 *
 * @author vains
 */
@Data
@Entity
@Comment("扫描接口详情表")
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_api_endpoint", indexes = {
        @Index(name = "idx_request_path", columnList = "path"),
        @Index(name = "idx_scan_status", columnList = "scan_status"),
        @Index(name = "idx_module_name", columnList = "module_name"),
        @Index(name = "idx_scan_batch_id", columnList = "scan_batch_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_batch_path_method", columnNames = {"scan_batch_id", "module_name", "path", "request_method"})
})
public class SysApiEndpoint extends BasicAuditorEntity {

    @Id
    @Comment("主键ID")
    private Long id;

    @Comment("扫描批次ID(扫描记录id)")
    @Column(name = "scan_batch_id", nullable = false)
    private Long scanBatchId;

    @Comment("请求路径")
    @Column(name = "path", nullable = false, length = 100)
    private String path;

    @Comment("请求方式")
    @Column(name = "request_method", nullable = false, length = 10)
    private String requestMethod;

    @Comment("所属模块")
    @Column(name = "module_name", length = 100)
    private String moduleName;

    @Comment("权限码")
    @Column(name = "permission", length = 50)
    private String permission;

    @Comment("接口描述(标题)")
    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "scan_status", nullable = false)
    @Comment("扫描状态：1-新发现 2-已存在 3-缺少注释 4-忽略")
    private ScanStatusEnum scanStatus;

    @Column(name = "existing_permission_id")
    @Comment("已存在的接口ID（关联权限表接口id）")
    private Long existingPermissionId;

    @Column(name = "imported")
    @Comment("是否已导入：0-未导入 1-已导入")
    private Boolean imported = false;

    @Comment("导入时间")
    @Column(name = "import_time")
    private LocalDateTime importTime;

    @Lob
    @Comment("错误信息")
    @Column(name = "error_message")
    private String errorMessage;
}