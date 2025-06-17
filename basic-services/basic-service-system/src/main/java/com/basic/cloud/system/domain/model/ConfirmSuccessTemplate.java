package com.basic.cloud.system.domain.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 绑定确认成功bean
 *
 * @author vains
 */
@Data
@Builder
public class ConfirmSuccessTemplate implements Serializable {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 绑定时间
     */
    private String bindTime;

}
