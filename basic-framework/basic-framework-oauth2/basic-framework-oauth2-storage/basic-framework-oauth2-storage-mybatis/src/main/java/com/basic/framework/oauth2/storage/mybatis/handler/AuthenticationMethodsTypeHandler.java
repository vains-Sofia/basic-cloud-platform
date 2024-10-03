package com.basic.framework.oauth2.storage.mybatis.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.basic.framework.core.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 客户端认证方式类型转换器
 *
 * @author vains
 */
public class AuthenticationMethodsTypeHandler extends AbstractJsonTypeHandler<Set<ClientAuthenticationMethod>> {

    public AuthenticationMethodsTypeHandler() {
        super(new TypeReference<Set<ClientAuthenticationMethod>>() {}.getClass());
    }

    public AuthenticationMethodsTypeHandler(Class<?> type, Field field) {
        super(type, field);
    }

    @Override
    public Set<ClientAuthenticationMethod> parse(String json) {
        Set<String> methods = JsonUtils.toObject(json, Set.class, String.class);
        if (ObjectUtils.isEmpty(methods)) {
            return null;
        }
        return methods.stream().map(ClientAuthenticationMethod::new).collect(Collectors.toSet());
    }

    @Override
    public String toJson(Set<ClientAuthenticationMethod> authenticationMethods) {
        Set<String> strings = authenticationMethods.stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toSet());
        return JsonUtils.toJson(strings);
    }
}
