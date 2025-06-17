package com.basic.cloud.system.converter;

import com.basic.cloud.system.enums.BindStatusEnum;
import com.basic.framework.data.jpa.converter.BasicEnumConverter;
import jakarta.persistence.Converter;

/**
 * Jpa 绑定状态枚举转换器
 *
 * @author vains
 */
@Converter(autoApply = true)
public class BindStatusEnumConverter extends BasicEnumConverter<Integer, BindStatusEnum> {

    public BindStatusEnumConverter() {
        super(BindStatusEnum.class);
    }
}
