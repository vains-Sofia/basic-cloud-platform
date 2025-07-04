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
 * 系统字典项实体类
 * <p>
 * 用于存储系统中的字典项信息，包括字典类型编码、字典项键、字典项值等。
 * </p>
 * <p>
 * 字典项通常用于存储一些固定的、可选的值，例如状态、类型等，以便在系统中进行统一管理和使用。
 * </p>
 *
 * @author vains
 */
@Data
@Entity
@Table(name = "sys_dict_item")
@EqualsAndHashCode(callSuper = true)
public class SysDictItem extends BasicAuditorEntity {

    @Id
    @Comment("主键")
    private Long id;

    @Comment("字典类型编码")
    @Column(name = "type_code", nullable = false, length = 50)
    private String typeCode;

    @Comment("字典项键")
    @Column(name = "item_key", nullable = false, length = 50)
    private String itemKey;

    @Comment("字典项值")
    @Column(name = "item_value", nullable = false, length = 100)
    private String itemValue;

    @Comment("排序值")
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Comment("状态（Y=启用，N=禁用）")
    @Column(nullable = false, length = 10)
    private StatusEnum status = StatusEnum.ENABLE;

    @Comment("多语言 JSON 值")
    @Column(name = "i18n_json", columnDefinition = "TEXT")
    private String i18nJson;

}
