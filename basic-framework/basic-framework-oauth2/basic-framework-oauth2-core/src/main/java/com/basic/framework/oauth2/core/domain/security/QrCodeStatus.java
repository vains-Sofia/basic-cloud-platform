package com.basic.framework.oauth2.core.domain.security;

import com.basic.framework.oauth2.core.enums.QrCodeStatusEnum;
import lombok.Data;

/**
 * 二维码状态
 * <p>
 * 用于记录二维码的状态以及相关用户信息
 * </p>
 *
 * @author vains
 */
@Data
public class QrCodeStatus {

    /**
     * 二维码唯一标识
     */
    private String token;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户头像
     */
    private String picture;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 二维码状态 INIT, SCANNED, CONFIRMED, EXPIRED
     */
    private QrCodeStatusEnum status;

}
