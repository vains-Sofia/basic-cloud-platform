package com.basic.framework.oauth2.storage.converter;

import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.authorization.server.util.OAuth2JsonUtils;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2Authorization;
import com.basic.framework.oauth2.storage.domain.response.FindAuthorizationResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Token.INVALIDATED_METADATA_NAME;

/**
 * Jpa实体转认证信息实体
 *
 * @author vains
 */
public class Jpa2AuthorizationResponseConverter implements Converter<JpaOAuth2Authorization, FindAuthorizationResponse> {

    private final TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
    };

    /**
     * 缓存解析结果，避免重复解析相同的JSON
     */
    private static final Map<String, Map<String, Object>> METADATA_CACHE = new ConcurrentHashMap<>();


    @Override
    public FindAuthorizationResponse convert(@Nullable JpaOAuth2Authorization source) {
        if (source == null) {
            return null;
        }
        FindAuthorizationResponse authorization = new FindAuthorizationResponse();
        BeanUtils.copyProperties(source, authorization);
        authorization.setAuthorizedScopes(JsonUtils.toObject(source.getAuthorizedScopes(), Set.class, String.class));

        // 检查令牌状态
        this.checkTokenStatus(source, authorization);

        return authorization;
    }

    /**
     * 检查所有令牌的状态
     * 使用函数式编程简化重复逻辑
     */
    private void checkTokenStatus(JpaOAuth2Authorization source, FindAuthorizationResponse authorization) {
        // 使用函数式编程简化重复逻辑
        LocalDateTime now = LocalDateTime.now();

        // 授权码
        if (ObjectUtils.isEmpty(source.getAuthorizationCodeValue())) {
            authorization.setAuthorizationCodeInvalidated(true);
        } else {
            checkSingleTokenStatus(source.getAuthorizationCodeMetadata(), source.getAuthorizationCodeExpiresAt(),
                    now, authorization::setAuthorizationCodeInvalidated);
        }

        // 刷新令牌
        if (ObjectUtils.isEmpty(source.getRefreshTokenValue())) {
            authorization.setRefreshTokenInvalidated(true);
        } else {
            checkSingleTokenStatus(source.getRefreshTokenMetadata(), source.getRefreshTokenExpiresAt(),
                    now, authorization::setRefreshTokenInvalidated);
        }

        if (ObjectUtils.isEmpty(source.getAccessTokenValue())) {
            authorization.setAccessTokenInvalidated(true);
        } else {
            checkSingleTokenStatus(source.getAccessTokenMetadata(), source.getAccessTokenExpiresAt(),
                    now, authorization::setAccessTokenInvalidated);
        }

        if (ObjectUtils.isEmpty(source.getOidcIdTokenValue())) {
            authorization.setOidcIdTokenInvalidated(true);
        } else {
            checkSingleTokenStatus(source.getOidcIdTokenMetadata(), source.getOidcIdTokenExpiresAt(),
                    now, authorization::setOidcIdTokenInvalidated);
        }

        if (ObjectUtils.isEmpty(source.getUserCodeValue())) {
            authorization.setUserCodeInvalidated(true);
        } else {
            checkSingleTokenStatus(source.getUserCodeMetadata(), source.getUserCodeExpiresAt(),
                    now, authorization::setUserCodeInvalidated);
        }

        if (ObjectUtils.isEmpty(source.getDeviceCodeValue())) {
            authorization.setDeviceCodeInvalidated(true);
        } else {
            checkSingleTokenStatus(source.getDeviceCodeMetadata(), source.getDeviceCodeExpiresAt(),
                    now, authorization::setDeviceCodeInvalidated);
        }
    }

    /**
     * 检查单个令牌状态的通用方法
     */
    private void checkSingleTokenStatus(String metadataJson, LocalDateTime expiresAt, LocalDateTime now,
                                        Consumer<Boolean> setter) {
        if (expiresAt == null) {
            return;
        }

        boolean invalidated = false;

        if (metadataJson != null && !metadataJson.trim().isEmpty()) {
            Map<String, Object> metadata = parseMetadata(metadataJson);
            if (metadata != null) {
                Boolean metadataInvalidated = (Boolean) metadata.getOrDefault(INVALIDATED_METADATA_NAME, false);
                invalidated = metadataInvalidated || now.isAfter(expiresAt);
            }
        } else {
            // 如果没有元数据，只检查时间
            invalidated = now.isAfter(expiresAt);
        }

        setter.accept(invalidated);
    }

    /**
     * 解析元数据，使用缓存避免重复解析
     */
    private Map<String, Object> parseMetadata(String metadataJson) {
        if (metadataJson == null || metadataJson.trim().isEmpty()) {
            return null;
        }

        // 尝试清理缓存
        if (METADATA_CACHE.size() > 200) {
            // 如果缓存超过200条，清理缓存
            METADATA_CACHE.clear();
        }

        // 使用缓存避免重复解析相同的JSON
        return METADATA_CACHE.computeIfAbsent(metadataJson, json -> {
            try {
                return OAuth2JsonUtils.toObject(json, typeRef.getType());
            } catch (Exception e) {
                // 解析失败时记录日志但不抛异常
                return null;
            }
        });
    }


}
