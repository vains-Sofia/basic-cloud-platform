package com.basic.framework.oauth2.storage.domain.model;

/**
 * scope与对应描述类
 *
 * @param scope       scope名称
 * @param description scope描述
 * @author vains
 */
public record ScopeWithDescription(String scope, String description) {
}
