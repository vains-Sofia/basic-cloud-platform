package com.basic.framework.oauth2.core.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 二维码状态枚举
 * INIT, SCANNED, CONFIRMED, EXPIRED
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum QrCodeStatusEnum implements BasicEnum<String, QrCodeStatusEnum> {

    /**
     * 二维码初始化状态
     */
    PENDING("pending", "二维码初始化状态"),

    /**
     * 二维码已被扫描
     */
    SCANNED("scanned", "二维码已被扫描"),

    /**
     * 二维码已被确认
     */
    CONFIRMED("confirmed", "二维码已被确认"),

    /**
     * 二维码已过期
     */
    EXPIRED("expired", "二维码已过期");

    /**
     * 二维码状态
     */
    private final String status;

    /**
     * 二维码状态描述
     */
    private final String description;

    @Override
    public String getValue() {
        return this.status;
    }
}
