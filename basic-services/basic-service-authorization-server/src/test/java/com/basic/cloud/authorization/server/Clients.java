package com.basic.cloud.authorization.server;

import com.basic.framework.oauth2.core.core.BasicAuthorizationGrantType;
import com.basic.framework.core.util.Sequence;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户端信息类
 *
 * @author vains
 */
@UtilityClass
public class Clients {
    
    private final Sequence SEQUENCE = new Sequence(null);

    /**
     * Junit测试默认客户端列表
     *
     * @return 客户端列表
     */
    public static List<RegisteredClient> defaultClients() {
        // 默认需要授权确认
        ClientSettings.Builder clientSettingsBuilder = ClientSettings.builder()
                .requireAuthorizationConsent(Boolean.TRUE);

        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder()
                // 自包含token(jwt)
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                // Access Token 存活时间：2小时
                .accessTokenTimeToLive(Duration.ofHours(2L))
                // 授权码存活时间：5分钟
                .authorizationCodeTimeToLive(Duration.ofMinutes(5L))
                // 设备码存活时间：5分钟
                .deviceCodeTimeToLive(Duration.ofMinutes(5L))
                // Refresh Token 存活时间：7天
                .refreshTokenTimeToLive(Duration.ofDays(7L))
                // 刷新 Access Token 后是否重用 Refresh Token
                .reuseRefreshTokens(Boolean.TRUE)
                // 设置 Id Token 加密方式
                .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256);

        List<RegisteredClient> clients = new ArrayList<>();
        RegisteredClient oidcClient = RegisteredClient.withId(String.valueOf(SEQUENCE.nextId()))
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
                .tokenSettings(tokenSettingsBuilder.build())
                .clientSettings(clientSettingsBuilder.build())
                .build();

        RegisteredClient swaggerClient = RegisteredClient.withId(String.valueOf(SEQUENCE.nextId()))
                .clientId("swagger-client")
                .clientSecret("{noop}123456")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(BasicAuthorizationGrantType.EMAIL)
                .authorizationGrantType(BasicAuthorizationGrantType.PASSWORD)
                .redirectUri("https://www.baidu.com")
                .redirectUri("http://127.0.0.1:5173/OAuth2Redirect")
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
                .redirectUri("http://127.0.0.1:8080/auth/swagger-ui/oauth2-redirect.html")
                .redirectUri("https://cloudflow.top/webjars/swagger-ui/oauth2-redirect.html")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .tokenSettings(tokenSettingsBuilder.build())
                .clientSettings(clientSettingsBuilder.build())
                .build();

        RegisteredClient deviceClient = RegisteredClient.withId(String.valueOf(SEQUENCE.nextId()))
                .clientId("device-messaging-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.DEVICE_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("message.read")
                .scope("message.write")
                .tokenSettings(tokenSettingsBuilder.build())
                .clientSettings(clientSettingsBuilder.build())
                .build();

        RegisteredClient messagingClient = RegisteredClient.withId(String.valueOf(SEQUENCE.nextId()))
                .clientId("messaging-client")
                .clientSecret("{noop}123456")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(BasicAuthorizationGrantType.EMAIL)
                .authorizationGrantType(BasicAuthorizationGrantType.PASSWORD)
                .redirectUri("https://www.baidu.com")
                .redirectUri("http://127.0.0.1:5173/OAuth2Redirect")
                .redirectUri("http://127.0.0.1:8000/login/oauth2/code/client-oidc")
                .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope("message.read")
                .scope("message.write")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .tokenSettings(tokenSettingsBuilder.build())
                .clientSettings(clientSettingsBuilder.build())
                .build();

        RegisteredClient privateKeyJwtClient = RegisteredClient.withId(String.valueOf(SEQUENCE.nextId()))
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
                .redirectUri("http://127.0.0.1:8000/login/oauth2/code/private-key-client-oidc")
                .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .tokenSettings(tokenSettingsBuilder.build())
                .clientSettings(
                        clientSettingsBuilder
                                .jwkSetUrl("http://127.0.0.1:8000/jwkSet")
                                .tokenEndpointAuthenticationSigningAlgorithm(SignatureAlgorithm.RS256)
                                .build()
                )
                .build();

        // PKCE客户端
        RegisteredClient pkceClient = RegisteredClient.withId(String.valueOf(SEQUENCE.nextId()))
                .clientId("pkce-message-client")
                .clientName("PKCE流程")
                // 公共客户端
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                // 设备码授权
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // 授权码模式回调地址，oauth2.1已改为精准匹配，不能只设置域名，并且屏蔽了localhost，本机使用127.0.0.1访问
                .redirectUri("http://127.0.0.1:5173/PkceRedirect")
                .redirectUri("https://j1zr8ren8w.51xd.pub/PkceRedirect")
                .redirectUri("https://flow-cloud.love/PkceRedirect")
                .redirectUri("https://authorization-example.vercel.app/PkceRedirect")
                .tokenSettings(tokenSettingsBuilder.build())
                // 开启 PKCE 流程
                .clientSettings(clientSettingsBuilder.requireProofKey(Boolean.TRUE).build())
                // 指定scope
                .scope("message.read")
                .scope("message.write")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .postLogoutRedirectUri("http://127.0.0.1:8080/getCaptcha")
                .build();

        clientSettingsBuilder.requireProofKey(Boolean.FALSE);

        // 匿名令牌客户端
        RegisteredClient opaqueClient = RegisteredClient.withId(String.valueOf(SEQUENCE.nextId()))
                // 客户端id
                .clientId("opaque-client")
                // 客户端名称
                .clientName("匿名token")
                // 客户端秘钥，使用密码解析器加密
                .clientSecret("{noop}123456")
                // 客户端认证方式，基于请求头的认证
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 配置资源服务器使用该客户端获取授权时支持的方式
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                // 授权码模式回调地址，oauth2.1已改为精准匹配，不能只设置域名，并且屏蔽了localhost，本机使用127.0.0.1访问
                .redirectUri("http://127.0.0.1:5173/OAuth2Redirect")
                .redirectUri("https://flow-cloud.love/OAuth2Redirect")
                .redirectUri("https://j1zr8ren8w.51xd.pub/OAuth2Redirect")
                .redirectUri("https://authorization-example.vercel.app/OAuth2Redirect")
                // 该客户端的授权范围，OPENID与PROFILE是IdToken的scope，获取授权时请求OPENID的scope时认证服务会返回IdToken
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                // 指定scope
                .scope("message.read")
                .scope("message.write")
                // 客户端设置，设置用户需要确认授权
                .clientSettings(clientSettingsBuilder.build())
                // token相关配置, 设置token为匿名token(opaque token)
                .tokenSettings(tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.REFERENCE).build())
                .build();
        tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
        clients.add(pkceClient);
        clients.add(oidcClient);
        clients.add(opaqueClient);
        clients.add(deviceClient);
        clients.add(swaggerClient);
        clients.add(messagingClient);
        clients.add(privateKeyJwtClient);
        return clients;
    }

}
