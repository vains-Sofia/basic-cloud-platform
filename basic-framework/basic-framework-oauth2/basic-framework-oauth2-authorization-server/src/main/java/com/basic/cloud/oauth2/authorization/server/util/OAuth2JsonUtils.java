package com.basic.cloud.oauth2.authorization.server.util;

import com.basic.cloud.core.util.JsonUtils;
import com.basic.cloud.oauth2.authorization.server.handler.OAuth2MapTypeHandler;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * OAuth2相关类的序列化
 *
 * @author vains
 */
@Slf4j
@UtilityClass
public class OAuth2JsonUtils {

    private static final ObjectMapper objectMapper = JsonUtils.getMapper().copy();

    static {
        // 序列化所有字段
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // 此项必须配置，否则如果序列化的对象里边还有对象，会报如下错误：
        //     java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to XXX
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        // 认证模块Jackson Mixin
        ClassLoader classLoader = OAuth2MapTypeHandler.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
    }

    public static <T> T toObject(String json, Type type) {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(type);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (JacksonException e) {
            log.warn("deserialize json: {} to {} error {}", json, javaType, e.getMessage());
        }
        return null;
    }

    public static String toJson(Object object) {
        try {
            return object instanceof String s ? s : objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            log.warn("serialize {} to json error {}", object, e.getMessage());
        }
        return null;
    }

}
