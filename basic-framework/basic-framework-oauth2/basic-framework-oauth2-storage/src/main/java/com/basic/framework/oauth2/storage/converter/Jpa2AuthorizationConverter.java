package com.basic.framework.oauth2.storage.converter;

import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2Authorization;
import com.basic.framework.oauth2.storage.domain.security.BasicAuthorization;
import jakarta.annotation.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

/**
 * Jpa实体转认证信息实体
 *
 * @author vains
 */
public class Jpa2AuthorizationConverter implements Converter<JpaOAuth2Authorization, BasicAuthorization> {

    @Override
    public BasicAuthorization convert(@Nullable JpaOAuth2Authorization source) {
        if (source == null) {
            return null;
        }
        BasicAuthorization authorization = new BasicAuthorization();
        BeanUtils.copyProperties(source, authorization);
        authorization.setAuthorizedScopes(JsonUtils.toObject(source.getAuthorizedScopes(), Set.class, String.class));
        return authorization;
    }
}
