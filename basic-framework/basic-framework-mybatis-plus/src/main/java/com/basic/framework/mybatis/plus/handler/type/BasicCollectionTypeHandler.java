package com.basic.framework.mybatis.plus.handler.type;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.basic.framework.core.util.JsonUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 基础单值集合类型转换器
 *
 * @author vains
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({Collection.class, List.class, Set.class})
public class BasicCollectionTypeHandler<T> extends AbstractJsonTypeHandler<Collection<T>> {

    public BasicCollectionTypeHandler(Class<?> type) {
        super(type);
    }

    public BasicCollectionTypeHandler(Class<?> type, Field field) {
        super(type, field);
    }

    @Override
    public Collection<T> parse(String json) {
        return JsonUtils.toObject(json, getFieldType());
    }

    @Override
    public String toJson(Collection<T> obj) {
        return JsonUtils.toJson(obj);
    }

}
