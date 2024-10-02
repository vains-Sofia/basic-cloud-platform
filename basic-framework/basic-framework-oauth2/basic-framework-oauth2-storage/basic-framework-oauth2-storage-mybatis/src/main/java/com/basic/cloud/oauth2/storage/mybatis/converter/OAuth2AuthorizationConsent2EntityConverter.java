package com.basic.cloud.oauth2.storage.mybatis.converter;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.basic.cloud.oauth2.storage.mybatis.entity.MybatisOAuth2AuthorizationConsent;
import com.basic.cloud.oauth2.authorization.server.core.BasicCoreServiceConverter;
import org.springframework.lang.Nullable;
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
public class OAuth2AuthorizationConsent2EntityConverter implements BasicCoreServiceConverter<OAuth2AuthorizationConsent, MybatisOAuth2AuthorizationConsent> {

    @Override
    public MybatisOAuth2AuthorizationConsent convert(@Nullable OAuth2AuthorizationConsent source) {
        if (source == null) {
            return null;
        }
        MybatisOAuth2AuthorizationConsent MybatisOAuth2AuthorizationConsent = new MybatisOAuth2AuthorizationConsent();
        MybatisOAuth2AuthorizationConsent.setId(IdWorker.getId());
        MybatisOAuth2AuthorizationConsent.setRegisteredClientId(source.getRegisteredClientId());
        MybatisOAuth2AuthorizationConsent.setPrincipalName(source.getPrincipalName());
        if (!ObjectUtils.isEmpty(source.getAuthorities())) {
            Set<String> authorities = source.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            MybatisOAuth2AuthorizationConsent.setAuthorities(authorities);
        }
        return MybatisOAuth2AuthorizationConsent;
    }
}
