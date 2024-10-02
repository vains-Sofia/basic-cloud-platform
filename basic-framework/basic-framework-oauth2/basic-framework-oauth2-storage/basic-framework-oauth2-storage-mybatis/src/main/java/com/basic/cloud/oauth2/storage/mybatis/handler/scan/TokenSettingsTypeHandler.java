package com.basic.cloud.oauth2.storage.mybatis.handler.scan;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.basic.cloud.oauth2.authorization.server.util.OAuth2JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.Map;

/**
 * token 设置类型转换器
 *
 * @author vains
 */
@MappedTypes(TokenSettings.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class TokenSettingsTypeHandler extends AbstractJsonTypeHandler<TokenSettings> {

    public TokenSettingsTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    public TokenSettings parse(String json) {
        Map<String, Object> settings = OAuth2JsonUtils.toObject(json, new TypeReference<Map<String, Object>>() {
        }.getType());
        return TokenSettings.withSettings(settings).build();
    }

    @Override
    public String toJson(TokenSettings tokenSettings) {
        if (tokenSettings != null) {
            return OAuth2JsonUtils.toJson(tokenSettings.getSettings());
        }
        return null;
    }
}
