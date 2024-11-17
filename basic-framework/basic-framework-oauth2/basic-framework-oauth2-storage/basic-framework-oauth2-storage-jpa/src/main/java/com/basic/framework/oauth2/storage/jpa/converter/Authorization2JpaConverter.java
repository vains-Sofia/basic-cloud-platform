package com.basic.framework.oauth2.storage.jpa.converter;

import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.storage.core.domain.BasicAuthorization;
import com.basic.framework.oauth2.storage.jpa.domain.JpaOAuth2Authorization;
import jakarta.annotation.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * 认证信息实体转Jpa实体
 *
 * @author vains
 */
public class Authorization2JpaConverter implements Converter<BasicAuthorization, JpaOAuth2Authorization> {

    @Override
    public JpaOAuth2Authorization convert(@Nullable BasicAuthorization source) {
        if (source == null) {
            return null;
        }
        JpaOAuth2Authorization authorization = new JpaOAuth2Authorization();
        BeanUtils.copyProperties(source, authorization);
        authorization.setAuthorizedScopes(JsonUtils.toJson(source.getAuthorizedScopes()));
        return authorization;
    }
}
