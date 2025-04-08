package com.basic.cloud.system.api.domain.request;

import com.basic.framework.core.domain.DataPageable;
import com.basic.framework.oauth2.core.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页查询用户入参
 *
 * @author vains
 */
@Data
@Schema(name = "分页查询用户入参")
@EqualsAndHashCode(callSuper = true)
public class FindBasicUserPageRequest extends DataPageable {

    /**
     * 昵称
     */
    @Schema(title = "用户名、昵称", description = "用户名、昵称")
    private String nickname;

    /**
     * 用户首选邮箱地址
     */
    @Schema(title = "用户的首选电子邮件地址。", description = "用户的首选电子邮件地址。")
    private String email;

    /**
     * 用户性别
     */
    @Schema(title = "用户性别", description = "用户性别")
    private GenderEnum gender;

}
