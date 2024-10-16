package com.basic.framework.oauth2.storage.mybatis.converter;

import com.basic.framework.oauth2.authorization.server.core.BasicCoreServiceConverter;
import com.basic.framework.oauth2.storage.mybatis.entity.MybatisOAuth2AuthorizationConsent;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.ObjectUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 实体转框架类
 *
 * @author vains
 */
@RequiredArgsConstructor
public class Entity2OAuth2AuthorizationConsentConverter implements BasicCoreServiceConverter<MybatisOAuth2AuthorizationConsent, OAuth2AuthorizationConsent> {

    private final RegisteredClientRepository registeredClientRepository;

    @Override
    public OAuth2AuthorizationConsent convert(@Nullable MybatisOAuth2AuthorizationConsent source) {
        if (source == null) {
            return null;
        }

        String registeredClientId = source.getRegisteredClientId();
        RegisteredClient registeredClient = this.registeredClientRepository.findById(registeredClientId);
        if (registeredClient == null) {
            throw new DataRetrievalFailureException(
                    "The RegisteredClient with id '" + registeredClientId + "' was not found in the RegisteredClientRepository.");
        }
        OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(
                registeredClientId, source.getPrincipalName());
        if (!ObjectUtils.isEmpty(source.getAuthorities())) {
            Set<GrantedAuthority> authorities = source.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            builder.authorities(e -> e.addAll(authorities));
        }
        return builder.build();
    }
}
