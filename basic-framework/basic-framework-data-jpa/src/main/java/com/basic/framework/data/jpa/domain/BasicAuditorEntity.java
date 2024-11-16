package com.basic.framework.data.jpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 基础审计实体
 *
 * @author vains
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BasicAuditorEntity {

    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 修改人
     */
    @LastModifiedBy
    @Column(name = "update_by")
    private Long updateBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

}
