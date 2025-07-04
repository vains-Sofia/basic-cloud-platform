package com.basic.cloud.system.domain;

import com.basic.cloud.system.api.enums.StatusEnum;
import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

/**
 * 字典类型实体类
 * 用于存储系统中的字典类型信息
 * 包括字典类型编码、名称、说明和状态等字段
 *
 * @author vains
 */
@Data
@Entity
@Table(name = "sys_dict_type")
@EqualsAndHashCode(callSuper = true)
public class SysDictType extends BasicAuditorEntity {

    @Id
    @Comment("主键")
    private Long id;

    @Comment("字典类型编码")
    @Column(name = "type_code", nullable = false, length = 50, unique = true)
    private String typeCode;

    @Comment("字典名称")
    @Column(nullable = false, length = 100)
    private String name;

    @Comment("类型说明")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Comment("状态（Y=启用，N=禁用）")
    @Column(nullable = false, length = 10)
    private StatusEnum status = StatusEnum.ENABLE;
}
