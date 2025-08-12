package com.basic.cloud.system.domain;

import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 接口扫描记录表
 * 该表用于记录系统接口扫描的结果，包括总接口数、新发现接口数、已存在接口数、缺少注解接口数等信息。
 *
 * @author vains
 */
@Data
@Entity
@Comment("接口扫描记录表")
@EqualsAndHashCode(callSuper = true)
@Table(name = "sys_api_scan_record", indexes = {
        @Index(name = "idx_scan_time", columnList = "scan_time")
})
public class SysApiScanRecord extends BasicAuditorEntity {

    @Id
    @Comment("主键ID")
    private Long id;

    @Comment("扫描时间")
    @Column(name = "scan_time")
    private LocalDateTime scanTime;

    @Comment("总接口数")
    @Column(name = "total_count")
    private Integer totalCount;

    @Comment("新发现接口数")
    @Column(name = "new_count")
    private Integer newCount;

    @Column(name = "exist_count")
    @Comment("已存在接口数")
    private Integer existCount;

    @Comment("缺少注释的接口数")
    @Column(name = "missing_desc_count")
    private Integer missingDescCount;

    @Lob
    @Comment("扫描结果摘要")
    @Column(name = "scan_result")
    private String scanResult;
}