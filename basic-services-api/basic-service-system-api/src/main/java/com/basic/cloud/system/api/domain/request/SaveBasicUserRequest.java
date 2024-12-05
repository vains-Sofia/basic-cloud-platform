package com.basic.cloud.system.api.domain.request;

import com.basic.framework.core.enums.GenderEnum;
import com.basic.framework.core.enums.OAuth2AccountPlatformEnum;
import com.basic.framework.data.validation.annotation.Phone;
import com.basic.framework.data.validation.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 保存或修改用户入参
 *
 * @author vains
 */
@Data
@Schema(name = "保存或修改用户入参")
public class SaveBasicUserRequest implements Serializable {

    @NotNull(groups = Update.class)
    @Schema(title = "用户id", description = "修改时必传")
    private Long id;

    @Schema(title = "昵称")
    private String nickname;

    @Schema(title = "密码(修改时无效)", description = "修改时无效")
    private String password;

    @Schema(title = "邮箱地址")
    @Email(groups = {Update.class, Default.class})
    @NotBlank(groups = {Update.class, Default.class})
    private String email;

    @Schema(title = "用户个人资料图片的 URL。", description = "此 URL 必须指向图像文件（例如，PNG、JPEG 或 GIF 图像文件），而不是指向包含图像的网页。")
    private String picture;

    @Schema(title = "用户性别")
    private GenderEnum gender;

    @Schema(title = "出生日期", description = "以 ISO 8601-1 [ISO8601‑1] YYYY-MM-DD 格式表示。")
    private LocalDate birthdate;

    @Schema(title = "用户地址")
    private String address;

    @Schema(title = "手机号")
    @Phone(groups = {Update.class, Default.class})
    private String phoneNumber;

    @Schema(title = "用户来源", description = "默认为system")
    private OAuth2AccountPlatformEnum accountPlatform;

}
