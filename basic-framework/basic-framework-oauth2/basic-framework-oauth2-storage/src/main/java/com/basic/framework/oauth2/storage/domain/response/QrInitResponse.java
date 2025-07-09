package com.basic.framework.oauth2.storage.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 二维码初始化响应
 *
 * @author vains
 */
@Data
@AllArgsConstructor
public class QrInitResponse {

    /**
     * 二维码唯一标识
     */
    private String token;

    /**
     * 二维码有效时长
     */
    private long expireIn;
}
