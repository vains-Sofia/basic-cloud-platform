package com.basic.example.mybatis.plus.domain.request;

import com.basic.framework.core.domain.BasicPageable;
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
public class PageRequest extends BasicPageable {

    @Schema(description = "用户id")
    private Long id;

}
