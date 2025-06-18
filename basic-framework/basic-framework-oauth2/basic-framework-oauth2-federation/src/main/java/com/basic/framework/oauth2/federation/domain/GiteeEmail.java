package com.basic.framework.oauth2.federation.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * Gitee获取Email响应
 *
 * @author vains
 */
@Data
public class GiteeEmail implements Serializable {

    /**
     * Email地址
     */
    private String email;

    /**
     * 是否通过验证
     */
    private String state;

    /**
     * 邮箱权限
     */
    private Set<String> scope;
}
