package com.basic.framework.oauth2.storage.domain.model;

/**
 * scope与对应描述类
 *
 * @param id          主键id
 * @param name        scope名称
 * @param scope       scope编码
 * @param description scope描述
 * @author vains
 */
public record ScopeWithDescription(Long id, String name, String scope, String description) {
}
