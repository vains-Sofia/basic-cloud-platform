package com.basic.cloud.system.converter;

import com.basic.framework.core.enums.GenderEnum;
import com.basic.framework.data.jpa.converter.BasicEnumConverter;
import jakarta.persistence.Converter;

/**
 * 账号来源枚举Jpa转换器
 *
 * @author vains
 */
@Converter(autoApply = true)
public class GenderEnumConverter extends BasicEnumConverter<Integer, GenderEnum> {

    /**
     * 调用父类构造器传入当前转换器枚举的class
     */
    public GenderEnumConverter() {
        super(GenderEnum.class);
    }

}
