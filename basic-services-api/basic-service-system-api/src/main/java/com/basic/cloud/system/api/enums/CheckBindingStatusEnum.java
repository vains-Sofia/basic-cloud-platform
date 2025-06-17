package com.basic.cloud.system.api.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 检查是否绑定状态枚举
 * bound: '绑定成功，欢迎回来',
 * pending_confirm: '我们向您的邮箱发送了确认邮件',
 * new_created: '系统已为您创建新账号',
 * conflict: '绑定失败',
 * non_email: '没有邮箱，需要主动绑定'
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum CheckBindingStatusEnum implements BasicEnum<String, CheckBindingStatusEnum> {

    /**
     * 已绑定账户。跳过
     */
    BOUND("bound", "已绑定账户。"),

    /**
     * 我们向您的邮箱发送了确认邮件
     */
    PENDING_CONFIRM("pending_confirm", "检测到已存在邮箱对应的账户，我们向您的邮箱发送了确认邮件，请点击邮件中的链接完成绑定。"),

    /**
     * 系统已为您创建新账号。跳过
     */
    NEW_CREATED("new_created", "系统已为您创建新账号。"),

    /**
     * 绑定失败
     */
    CONFLICT("conflict", "已存在待确认的绑定请求，请检查您的邮箱或联系管理员。"),

    /**
     * 没有邮箱，需要主动绑定
     */
    NON_EMAIL("non_email", "为了完成绑定，请提供您的邮箱地址。");

    /**
     * 检查绑定状态值
     */
    private final String value;

    /**
     * 检查绑定状态描述
     */
    private final String description;

}
