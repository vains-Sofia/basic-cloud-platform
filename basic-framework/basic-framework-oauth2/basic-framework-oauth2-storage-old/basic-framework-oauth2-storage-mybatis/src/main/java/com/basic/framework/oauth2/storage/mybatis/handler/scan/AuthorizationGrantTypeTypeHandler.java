package com.basic.framework.oauth2.storage.mybatis.handler.scan;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * 授权方式类型转换器
 *
 * @author vains
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(AuthorizationGrantType.class)
public class AuthorizationGrantTypeTypeHandler extends AbstractJsonTypeHandler<AuthorizationGrantType> {

    public AuthorizationGrantTypeTypeHandler(Class<?> type) {
        super(type);
    }

    @Override
    public AuthorizationGrantType parse(String json) {
        return new AuthorizationGrantType(json);
    }

    @Override
    public String toJson(AuthorizationGrantType grantType) {
        if (grantType != null) {
            return grantType.getValue();
        }
        return null;
    }
}
