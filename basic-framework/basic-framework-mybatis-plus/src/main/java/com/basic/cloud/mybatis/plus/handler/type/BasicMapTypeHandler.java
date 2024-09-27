package com.basic.cloud.mybatis.plus.handler.type;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.basic.cloud.core.util.JsonUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础键值对集合类型转换器
 *
 * @author vains
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({Map.class, HashMap.class})
public class BasicMapTypeHandler<K, V> extends AbstractJsonTypeHandler<Map<K, V>> {

    public BasicMapTypeHandler(Class<?> type) {
        super(type);
    }

    public BasicMapTypeHandler(Class<?> type, Field field) {
        super(type, field);
    }

    @Override
    public Map<K, V> parse(String json) {
        return JsonUtils.toObject(json, getFieldType());
    }

    @Override
    public String toJson(Map<K, V> obj) {
        return JsonUtils.toJson(obj);
    }

}
