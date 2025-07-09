package com.basic.framework.oauth2.storage.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 二维码登录确认入参
 *
 * @author vains
 */
@Data
public class QrCodeConfirmRequest {

    /**
     * 二维码唯一标识
     */
    @NotBlank
    private String token;

    /**
     * 扫码二维码后产生的临时票据(仅一次有效)
     */
    @NotBlank
    private String ticket;

}
