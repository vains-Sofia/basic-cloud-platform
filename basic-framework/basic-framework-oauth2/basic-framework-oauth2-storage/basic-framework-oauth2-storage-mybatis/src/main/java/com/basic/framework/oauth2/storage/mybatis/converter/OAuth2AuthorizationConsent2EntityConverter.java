package com.basic.framework.oauth2.storage.mybatis.converter;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.basic.framework.oauth2.authorization.server.core.BasicCoreServiceConverter;
import com.basic.framework.oauth2.storage.core.domain.BasicAuthorizationConsent;
import jakarta.annotation.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.util.ObjectUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 框架类转实体
 *
 * @author vains
 */
public class OAuth2AuthorizationConsent2EntityConverter implements BasicCoreServiceConverter<OAuth2AuthorizationConsent, BasicAuthorizationConsent> {

    @Override
    public BasicAuthorizationConsent convert(@Nullable OAuth2AuthorizationConsent source) {
        if (source == null) {
            return null;
        }
        BasicAuthorizationConsent authorizationConsent = new BasicAuthorizationConsent();
        authorizationConsent.setId(IdWorker.getId());
        authorizationConsent.setRegisteredClientId(source.getRegisteredClientId());
        authorizationConsent.setPrincipalName(source.getPrincipalName());
        if (!ObjectUtils.isEmpty(source.getAuthorities())) {
            Set<String> authorities = source.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            authorizationConsent.setAuthorities(authorities);
        }
        return authorizationConsent;
    }
}
