package com.basic.cloud.authorization.server.handler.scan;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.basic.cloud.core.util.JsonUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;

import java.util.Map;

/**
 * 客户端设置类型转换器
 *
 * @author vains
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(ClientSettings.class)
public class ClientSettingsTypeHandler extends AbstractJsonTypeHandler<ClientSettings> {

    public ClientSettingsTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    public ClientSettings parse(String json) {
        Map<String, Object> settings = JsonUtils.toObject(json, Map.class, String.class, Object.class);
        if (settings.containsKey(ConfigurationSettingNames.Client.TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM)) {
            String signName = (String) settings.get(ConfigurationSettingNames.Client.TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM);
            settings.put(ConfigurationSettingNames.Client.TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM, SignatureAlgorithm.from(signName));
        }
        return ClientSettings.withSettings(settings).build();
    }

    @Override
    public String toJson(Object obj) {
        if (obj instanceof ClientSettings clientSettings) {
            return JsonUtils.toJson(clientSettings.getSettings());
        }
        return null;
    }
}
