package com.basic.cloud.oauth2.authorization.server.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.basic.cloud.oauth2.authorization.server.util.OAuth2JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * oauth2相关类的map转换器
 *
 * @author vains
 */
@Slf4j
public class OAuth2MapTypeHandler<K, V> extends AbstractJsonTypeHandler<Map<K, V>> {

    public OAuth2MapTypeHandler() {
        super(new TypeReference<Map<K, V>>() {
        }.getClass());
    }

    public OAuth2MapTypeHandler(Class<?> type, Field field) {
        super(type, field);
    }

    @Override
    public Map<K, V> parse(String json) {
        return OAuth2JsonUtils.toObject(json, getFieldType());
    }

    @Override
    public String toJson(Object obj) {
        return OAuth2JsonUtils.toJson(obj);
    }
}
