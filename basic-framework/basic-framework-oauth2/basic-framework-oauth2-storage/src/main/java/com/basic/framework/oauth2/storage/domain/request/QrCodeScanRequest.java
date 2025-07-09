package com.basic.framework.oauth2.storage.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 二维码扫描请求
 *
 * @author vains
 */
@Data
public class QrCodeScanRequest {

    /**
     * 二维码唯一标识
     */
    @NotBlank
    private String token;

}