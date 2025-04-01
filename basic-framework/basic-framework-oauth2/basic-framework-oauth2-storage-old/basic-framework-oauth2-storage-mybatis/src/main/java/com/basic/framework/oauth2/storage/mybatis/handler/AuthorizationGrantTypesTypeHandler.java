package com.basic.framework.oauth2.storage.mybatis.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.basic.framework.core.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 授权方式类型转换器
 *
 * @author vains
 */
public class AuthorizationGrantTypesTypeHandler extends AbstractJsonTypeHandler<Set<AuthorizationGrantType>> {

    public AuthorizationGrantTypesTypeHandler() {
        super(new TypeReference<Set<AuthorizationGrantType>>() {}.getClass());
    }

    public AuthorizationGrantTypesTypeHandler(Class<?> type, Field field) {
        super(type, field);
    }

    @Override
    public Set<AuthorizationGrantType> parse(String json) {
        Set<String> methods = JsonUtils.toObject(json, Set.class, String.class);
        if (ObjectUtils.isEmpty(methods)) {
            return null;
        }
        return methods.stream().map(AuthorizationGrantType::new).collect(Collectors.toSet());
    }

    @Override
    public String toJson(Set<AuthorizationGrantType> authenticationMethods) {
        Set<String> strings = authenticationMethods.stream().map(AuthorizationGrantType::getValue).collect(Collectors.toSet());
        return JsonUtils.toJson(strings);
    }
}
