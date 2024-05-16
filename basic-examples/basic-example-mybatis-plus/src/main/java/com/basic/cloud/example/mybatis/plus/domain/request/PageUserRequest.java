package com.basic.cloud.example.mybatis.plus.domain.request;

import com.basic.cloud.core.domain.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询用户入参
 *
 * @author vains
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageUserRequest extends PageRequest {

    @Schema(description = "用户id")
    private Long id;

}
