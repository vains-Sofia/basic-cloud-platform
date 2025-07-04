package com.basic.cloud.system.converter;

import com.basic.cloud.system.api.enums.StatusEnum;
import com.basic.framework.data.jpa.converter.BasicEnumConverter;
import jakarta.persistence.Converter;

/**
 * 状态枚举转换器
 *
 * @author vains
 */
@Converter(autoApply = true)
public class StatusEnumConverter extends BasicEnumConverter<String, StatusEnum> {

    public StatusEnumConverter() {
        super(StatusEnum.class);
    }
}
