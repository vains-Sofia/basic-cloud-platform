package com.basic.cloud.system.api.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件预签名响应
 *
 * @author vains
 */
@Data
public class FilePreSignedResponse implements Serializable {

    @Schema(title = "文件唯一标识")
    private String name;

    @Schema(title = "文件访问地址")
    private String url;

    @Schema(title = "文件所在的存储桶")
    private String bucket;

}
