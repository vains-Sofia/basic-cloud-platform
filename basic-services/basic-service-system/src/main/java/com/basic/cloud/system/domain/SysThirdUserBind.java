package com.basic.cloud.system.domain;

import com.basic.cloud.system.enums.BindStatusEnum;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 第三方用户绑定实体类
 *
 * @author Vains
 */
@Getter
@Setter
@Entity
@Comment(value = "第三方账号绑定表")
@Table(name = "sys_third_user_bind")
public class SysThirdUserBind {

    @Id
    @Comment(value = "主键ID")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Comment(value = "本地系统用户ID")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Comment(value = "第三方平台提供商")
    @Column(name = "provider", nullable = false, length = 50)
    private OAuth2AccountPlatformEnum provider;

    @NotNull
    @Size(max = 100)
    @Comment(value = "第三方平台用户ID")
    @Column(name = "provider_user_id", nullable = false, length = 100)
    private String providerUserId;

    @Size(max = 255)
    @Column(name = "email")
    @Comment(value = "第三方平台用户邮箱")
    private String email;

    @Lob
    @Column(name = "access_token")
    @Comment(value = "第三方登录时的 access token")
    private String accessToken;

    @Lob
    @Column(name = "refresh_token")
    @Comment(value = "第三方登录时的 refresh token")
    private String refreshToken;

    @Column(name = "expires_at")
    @Comment(value = "access token 过期时间")
    private LocalDateTime expiresAt;

    @ColumnDefault("0")
    @Column(name = "bind_status")
    @Comment(value = "绑定状态，0-待确认，1-已绑定")
    private BindStatusEnum bindStatus;

    @Comment(value = "绑定时间")
    @Column(name = "bind_time")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime bindTime;

    @Size(max = 255)
    @Column(name = "confirm_token")
    @Comment(value = "用于邮箱绑定确认的 token")
    private String confirmToken;

    @Column(name = "token_expires_at")
    @Comment(value = "确认 token 过期时间")
    private LocalDateTime tokenExpiresAt;

    @Comment(value = "创建时间")
    @Column(name = "create_time")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    @Comment(value = "更新时间")
    @Column(name = "update_time")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

}