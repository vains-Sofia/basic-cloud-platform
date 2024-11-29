package com.basic.cloud.system.domain;

import com.basic.framework.core.enums.GenderEnum;
import com.basic.framework.core.enums.OAuth2AccountPlatformEnum;
import com.basic.framework.data.jpa.domain.BasicAuditorEntity;
import com.basic.framework.data.validation.annotation.Phone;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.List;

/**
 * 基础用户信息表
 *
 * @author vains
 */
@Data
@Entity
@Table(name = "sys_basic_user")
@Comment(value = "基础用户信息表")
@SQLRestriction("deleted = false")
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE sys_basic_user u SET u.deleted = true WHERE id = ?")
public class SysBasicUser extends BasicAuditorEntity {

    /**
     * 主键id
     */
    @Id
    @Comment(value = "主键id")
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 用户名、昵称
     */
    @Size(max = 255)
    @Column(name = "nickname")
    @Comment(value = "用户名、昵称")
    private String nickname;

    /**
     * 用户个人资料页面的 URL。
     */
    @Size(max = 255)
    @Column(name = "profile")
    @Comment(value = "用户个人资料页面的 URL。")
    private String profile;

    /**
     * 用户个人资料图片的 URL。此 URL 必须指向图像文件（例如，PNG、JPEG 或 GIF 图像文件），而不是指向包含图像的网页。
     */
    @Size(max = 255)
    @Column(name = "picture")
    @Comment(value = "用户个人资料图片的 URL。此 URL 必须指向图像文件（例如，PNG、JPEG 或 GIF 图像文件），而不是指向包含图像的网页。")
    private String picture;

    /**
     * 用户的首选电子邮件地址。其值必须符合RFC 5322 [RFC5322] addr-spec 语法
     */
    @Email
    @Size(max = 50)
    @Column(name = "email", length = 50)
    @Comment(value = "用户的首选电子邮件地址。其值必须符合RFC 5322 [RFC5322] addr-spec 语法")
    private String email;

    /**
     * 邮箱是否验证过
     */
    @Comment(value = "邮箱是否验证过")
    @Column(name = "email_verified")
    private Boolean emailVerified;

    /**
     * 用户性别
     */
    @Column(name = "gender")
    @Comment(value = "用户性别")
    private GenderEnum gender;

    /**
     * 密码
     */
    @Size(max = 255)
    @Comment(value = "密码")
    @Column(name = "password")
    private String password;

    /**
     * 出生日期，以 ISO 8601-1 [ISO8601‑1] YYYY-MM-DD 格式表示。
     */
    @Column(name = "birthdate")
    @Comment(value = "出生日期，以 ISO 8601-1 [ISO8601‑1] YYYY-MM-DD 格式表示。")
    private LocalDate birthdate;

    /**
     * 手机号
     */
    @Phone
    @Size(max = 11)
    @Comment(value = "手机号")
    @Column(name = "phone_number", length = 11)
    private String phoneNumber;

    /**
     * 手机号是否已验证
     */
    @Comment(value = "手机号是否已验证")
    @Column(name = "phone_number_verified")
    private Boolean phoneNumberVerified;

    /**
     * 用户的首选邮政地址
     */
    @Size(max = 255)
    @Column(name = "address")
    @Comment(value = "用户的首选邮政地址")
    private String address;

    /**
     * 是否已删除
     */
    @Column(name = "deleted")
    @Comment(value = "是否已删除")
    private Boolean deleted;

    /**
     * 用户来源
     */
    @Comment(value = "用户来源")
    @Column(name = "account_platform")
    private OAuth2AccountPlatformEnum accountPlatform;

    /**
     * 用户拥有的角色
     */
    @ManyToMany
    @JoinTable(
            name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<SysRole> roles;

}