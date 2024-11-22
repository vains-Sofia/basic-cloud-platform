package com.basic.cloud.system.converter;

import com.basic.framework.core.enums.OAuth2AccountPlatformEnum;
import com.basic.framework.data.jpa.converter.BasicEnumConverter;
import jakarta.persistence.Converter;

/**
 * 账号来源枚举Jpa转换器
 *
 * @author vains
 */
@Converter(autoApply = true)
public class AccountPlatformEnumConverter extends BasicEnumConverter<String, OAuth2AccountPlatformEnum> {

    /**
     * 调用父类构造器传入当前转换器枚举的class
     */
    public AccountPlatformEnumConverter() {
        super(OAuth2AccountPlatformEnum.class);
    }

}
