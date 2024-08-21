package com.basic.cloud.authorization.server;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.basic.cloud.oauth2.authorization.core.BasicAuthorizationGrantType;
import com.basic.cloud.oauth2.authorization.domain.DefaultAuthenticatedUser;
import com.basic.cloud.oauth2.authorization.enums.OAuth2AccountPlatformEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.List;

/**
 * @author vains
 */
@SpringBootTest
class MybatisRegisteredClientRepositoryTests {

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

    @BeforeEach
    void setUp() {
        DefaultAuthenticatedUser user = new DefaultAuthenticatedUser("admin",
                OAuth2AccountPlatformEnum.SYSTEM,
                List.of(new SimpleGrantedAuthority("USER")));
        user.setId(123L);
        user.setPassword("{noop}123456");
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    public void findById() {
        RegisteredClient registeredClient = this.registeredClientRepository.findById("1791832160811171843");
        System.out.println(registeredClient);
    }

    @Test
    public void findByClientId() {
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId("device-messaging-client");
        System.out.println(registeredClient);
    }

    @Test
    public void initClients() {
        RegisteredClient oidcClient = RegisteredClient.withId(IdWorker.getIdStr())
                .clientId("oidc-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(BasicAuthorizationGrantType.EMAIL)
                .authorizationGrantType(BasicAuthorizationGrantType.PASSWORD)
                .redirectUri("https://www.baidu.com")
                .redirectUri("http://127.0.0.1:5173/OAuth2Redirect")
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        RegisteredClient deviceClient = RegisteredClient.withId(IdWorker.getIdStr())
                .clientId("device-messaging-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.DEVICE_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("message.read")
                .scope("message.write")
                .build();

        RegisteredClient messagingClient = RegisteredClient.withId(IdWorker.getIdStr())
                .clientId("messaging-client")
                .clientSecret("{noop}123456")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(BasicAuthorizationGrantType.EMAIL)
                .authorizationGrantType(BasicAuthorizationGrantType.PASSWORD)
                .redirectUri("https://www.baidu.com")
                .redirectUri("http://127.0.0.1:5173/OAuth2Redirect")
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        RegisteredClient privateKeyJwtClient = RegisteredClient.withId(IdWorker.getIdStr())
                .clientId("private-key-jwt-client")
                .clientSecret("{noop}12345678")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(BasicAuthorizationGrantType.EMAIL)
                .authorizationGrantType(BasicAuthorizationGrantType.PASSWORD)
                .redirectUri("https://www.baidu.com")
                .redirectUri("http://127.0.0.1:5173/OAuth2Redirect")
                .redirectUri("http://127.0.0.1:8000/login/oauth2/code/messaging-client-oidc")
                .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(
                        ClientSettings.builder()
                                .requireAuthorizationConsent(true)
                                .jwkSetUrl("http://127.0.0.1:8000/jwkSet")
                                .tokenEndpointAuthenticationSigningAlgorithm(SignatureAlgorithm.RS256)
                                .build()
                )
                .build();

        this.registeredClientRepository.save(oidcClient);
        this.registeredClientRepository.save(deviceClient);
        this.registeredClientRepository.save(messagingClient);
        this.registeredClientRepository.save(privateKeyJwtClient);
    }

}