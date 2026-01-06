package com.basic.cloud.workflow.api.enums;

import com.basic.framework.core.enums.BasicEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 审批动作枚举
 * APPROVE-同意 / REJECT-驳回 / COUNTERSIGN-会签 / TRANSFER-转交审批
 *
 * @author vains
 */
@Getter
@RequiredArgsConstructor
public enum ApproveActionEnum implements BasicEnum<String, ApproveActionEnum> {

    APPROVE("APPROVE", "同意"),

    REJECT("REJECT", "驳回"),

    COUNTERSIGN("COUNTERSIGN", "会签"),

    TRANSFER("TRANSFER", "转交审批");

    private final String action;

    private final String description;

    @Override
    public String getValue() {
        return this.action;
    }
}
