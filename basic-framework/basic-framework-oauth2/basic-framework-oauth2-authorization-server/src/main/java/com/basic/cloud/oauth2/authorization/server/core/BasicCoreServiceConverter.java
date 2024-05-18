package com.basic.cloud.oauth2.authorization.server.core;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 基础实体转换器
 *
 * @author vains
 */
public interface BasicCoreServiceConverter<T, R> extends Converter<T, R> {

    default LocalDateTime instantToTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    default Instant timeToInstant(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * 设置token的值
     *
     * @param token              Token实例
     * @param tokenValueConsumer set方法
     * @param issuedAtConsumer   set方法
     * @param expiresAtConsumer  set方法
     * @param metadataConsumer   set方法
     */
    default void setTokenValues(
            OAuth2Authorization.Token<?> token,
            Consumer<String> tokenValueConsumer,
            Consumer<LocalDateTime> issuedAtConsumer,
            Consumer<LocalDateTime> expiresAtConsumer,
            Consumer<Map<String, Object>> metadataConsumer) {
        if (token != null) {
            OAuth2Token oAuth2Token = token.getToken();
            tokenValueConsumer.accept(oAuth2Token.getTokenValue());
            issuedAtConsumer.accept(instantToTime(oAuth2Token.getIssuedAt()));
            expiresAtConsumer.accept(instantToTime(oAuth2Token.getExpiresAt()));
            metadataConsumer.accept(token.getMetadata());
        }
    }

}
