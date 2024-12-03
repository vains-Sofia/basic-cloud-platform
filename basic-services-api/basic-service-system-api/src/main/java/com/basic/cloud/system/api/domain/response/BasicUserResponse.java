package com.basic.cloud.system.api.domain.response;

import com.basic.cloud.system.api.domain.security.PermissionAuthority;
import com.basic.framework.core.enums.GenderEnum;
import com.basic.framework.core.enums.OAuth2AccountPlatformEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 基础用户信息响应
 *
 * @author vains
 */
@Data
@Schema(name = "基础用户信息响应")
public class BasicUserResponse implements Serializable {

    @Schema(title = "主键id")
    private Long id;

    @Schema(title = "用户名、昵称")
    private String nickname;

    @Schema(title = "用户个人资料页面的 URL。")
    private String profile;

    @Schema(title = "用户个人资料图片的 URL。", description = "此 URL 必须指向图像文件（例如，PNG、JPEG 或 GIF 图像文件），而不是指向包含图像的网页。")
    private String picture;

    @Schema(title = "用户的首选电子邮件地址。", description = "其值必须符合RFC 5322 [RFC5322] addr-spec 语法。")
    private String email;

    @Schema(title = "邮箱是否验证过")
    private Boolean emailVerified;

    @Schema(title = "用户性别")
    private GenderEnum gender;

    @Schema(title = "密码")
    private String password;

    @Schema(title = "出生日期", description = "以 ISO 8601-1 [ISO8601‑1] YYYY-MM-DD 格式表示。")
    private LocalDate birthdate;

    @Schema(title = "手机号")
    private String phoneNumber;

    @Schema(title = "手机号是否已验证")
    private Boolean phoneNumberVerified;

    @Schema(title = "用户的首选邮政地址")
    private String address;

    @Schema(title = "是否已删除")
    private Boolean deleted;

    @Schema(title = "用户来源")
    private OAuth2AccountPlatformEnum accountPlatform;

    @Schema(title = "用户拥有的权限信息")
    private Set<PermissionAuthority> authorities;

    @Schema(title = "创建人名称")
    private String createName;

    @Schema(title = "修改人名称")
    private String updateName;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "修改时间")
    private LocalDateTime updateTime;

}
