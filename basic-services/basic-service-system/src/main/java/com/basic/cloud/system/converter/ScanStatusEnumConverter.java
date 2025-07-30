package com.basic.cloud.system.converter;

import com.basic.cloud.system.api.enums.ScanStatusEnum;
import com.basic.framework.data.jpa.converter.BasicEnumConverter;
import jakarta.persistence.Converter;

/**
 * 扫描状态注解转换器
 *
 * @author vains
 */
@Converter(autoApply = true)
public class ScanStatusEnumConverter extends BasicEnumConverter<Integer, ScanStatusEnum> {

    public ScanStatusEnumConverter() {
        super(ScanStatusEnum.class);
    }

}
