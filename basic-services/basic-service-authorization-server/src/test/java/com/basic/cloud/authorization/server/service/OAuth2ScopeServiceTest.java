package com.basic.cloud.authorization.server.service;

import com.basic.framework.core.domain.PageResult;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2AuthorizationConsent;
import com.basic.framework.oauth2.storage.domain.model.ScopeWithDescription;
import com.basic.framework.oauth2.storage.domain.request.FindScopePageRequest;
import com.basic.framework.oauth2.storage.domain.request.SaveScopeRequest;
import com.basic.framework.oauth2.storage.domain.response.FindScopeResponse;
import com.basic.framework.oauth2.storage.repository.OAuth2AuthorizationConsentRepository;
import com.basic.framework.oauth2.storage.service.OAuth2ScopeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.oidc.OidcScopes;

import java.util.List;
import java.util.Set;

/**
 * OAuth2 Scope Junit测试类
 *
 * @author vains
 */
@SpringBootTest
class OAuth2ScopeServiceTest {

    @Autowired
    private OAuth2ScopeService scopeService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OAuth2AuthorizationConsentRepository authorizationConsentRepository;

    @BeforeEach
    void setUp() {
        UserDetails user = userDetailsService.loadUserByUsername("user");
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    void findScopePage() {
        FindScopePageRequest request = new FindScopePageRequest();
        request.setSize(10L);
        request.setCurrent(1L);
        PageResult<FindScopeResponse> pageResult = scopeService.findScopePage(request);
        System.out.println(pageResult);
        pageResult.getRecords().forEach(System.out::println);
    }

    @Test
    void findByScopes() {
        Set<ScopeWithDescription> withDescriptions = scopeService.findByScopes(Set.of(OidcScopes.PROFILE));
        withDescriptions.forEach(System.out::println);
    }

    @Test
    void saveScope() {
        SaveScopeRequest request = new SaveScopeRequest();
        request.setScope(OidcScopes.EMAIL);
        request.setDescription("The email scope requests access to the email and email_verified claims.");
        scopeService.saveScope(request);
    }

    @Test
    void updateScope() {
        SaveScopeRequest request = new SaveScopeRequest();
        request.setId(1856578945196253186L);
        request.setScope(OidcScopes.EMAIL);
        request.setEnabled(Boolean.TRUE);
        scopeService.updateScope(request);
    }

    @Test
    void findAll() {
        List<JpaOAuth2AuthorizationConsent> authorizationConsents = authorizationConsentRepository.findAll();
        for (JpaOAuth2AuthorizationConsent authorizationConsent : authorizationConsents) {
            System.out.println(authorizationConsent);
        }
    }
}