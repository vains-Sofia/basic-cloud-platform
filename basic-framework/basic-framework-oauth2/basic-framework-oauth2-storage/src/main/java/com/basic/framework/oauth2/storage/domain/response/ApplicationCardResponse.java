package com.basic.framework.oauth2.storage.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户端响应bean
 *
 * @author vains
 */
@Data
@Schema(name = "客户端信息，卡片式展示")
public class ApplicationCardResponse implements Serializable {

    /**
     * 客户端数据id
     */
    @Schema(title = "客户端数据id")
    private Long id;

    /**
     * 客户端id
     */
    @Schema(title = "客户端id")
    private String clientId;

    /**
     * 客户端名称
     */
    @Schema(title = "客户端名称")
    private String clientName;

    /**
     * 客户端Logo
     */
    @Schema(title = "客户端Logo")
    private String clientLogo;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 客户端描述
     */
    @Schema(title = "客户端描述")
    private String description;

}
