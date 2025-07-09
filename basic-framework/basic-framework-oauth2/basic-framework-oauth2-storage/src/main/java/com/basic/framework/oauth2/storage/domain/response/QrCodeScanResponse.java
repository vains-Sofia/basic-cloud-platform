package com.basic.framework.oauth2.storage.domain.response;

import com.basic.framework.oauth2.core.enums.QrCodeStatusEnum;
import lombok.Data;

/**
 * 扫描二维码响应bean
 *
 * @author vains
 */
@Data
public class QrCodeScanResponse {

    /**
     * 扫描临时票据
     */
    private String ticket;

    /**
     * 二维码状态
     */
    private QrCodeStatusEnum status;

}
