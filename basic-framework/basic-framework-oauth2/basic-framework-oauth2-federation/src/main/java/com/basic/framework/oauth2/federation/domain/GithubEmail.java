package com.basic.framework.oauth2.federation.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Github获取Email响应
 *
 * @author vains
 */
@Data
public class GithubEmail implements Serializable {

    /**
     * Email地址
     */
    private String email;

    /**
     * 是否通过验证
     */
    private Boolean verified;

    /**
     * 是否为主邮箱
     */
    private Boolean primary;

    /**
     * 可见性
     * 可选值：public, private
     */
    private String visibility;

}
