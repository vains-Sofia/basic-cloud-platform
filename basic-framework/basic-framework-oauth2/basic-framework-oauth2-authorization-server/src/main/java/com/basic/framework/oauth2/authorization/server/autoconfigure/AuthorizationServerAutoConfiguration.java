package com.basic.framework.oauth2.authorization.server.autoconfigure;

import com.basic.framework.oauth2.core.domain.security.BasicGrantedAuthority;
import com.basic.framework.oauth2.core.domain.security.ScopePermissionModel;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import com.basic.framework.oauth2.authorization.server.captcha.CaptchaService;
import com.basic.framework.oauth2.authorization.server.captcha.impl.RedisCaptchaService;
import com.basic.framework.oauth2.authorization.server.email.EmailCaptchaLoginAuthenticationProvider;
import com.basic.framework.oauth2.authorization.server.introspector.BasicOpaqueTokenIntrospector;
import com.basic.framework.oauth2.core.annotation.ConditionalOnInMemoryStorage;
import com.basic.framework.oauth2.core.converter.BasicJwtRedisAuthenticationConverter;
import com.basic.framework.oauth2.core.core.BasicAuthorizationGrantType;
import com.basic.framework.oauth2.core.customizer.BasicIdTokenCustomizer;
import com.basic.framework.oauth2.core.customizer.JwtIdTokenCustomizer;
import com.basic.framework.oauth2.core.customizer.OpaqueIdTokenCustomizer;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import com.basic.framework.oauth2.core.manager.ReactiveContextAuthorizationManager;
import com.basic.framework.oauth2.core.manager.RequestContextAuthorizationManager;
import com.basic.framework.oauth2.core.property.OAuth2ServerProperties;
import com.basic.framework.oauth2.core.property.ResourceServerProperties;
import com.basic.framework.oauth2.core.resolver.DelegatingTokenAuthenticationResolver;
import com.basic.framework.redis.support.RedisOperator;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.util.ObjectUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 认证服务自动配置
 *
 * @author vains
 */
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@Import({ServerRedisAutoConfiguration.class, RedisOperator.class})
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@EnableConfigurationProperties({OAuth2ServerProperties.class, ResourceServerProperties.class})
public class AuthorizationServerAutoConfiguration {

    /**
     * 认证服务配置类
     */
    private final OAuth2ServerProperties oAuth2ServerProperties;

    private final RedisOperator<AuthenticatedUser> redisOperator;

    private final ResourceServerProperties resourceServerProperties;

    private final RedisOperator<List<ScopePermissionModel>> scopePermissionOperator;

    private final RedisOperator<Map<String, List<BasicGrantedAuthority>>> permissionRedisOperator;

    /**
     * 将AuthenticationManager注入ioc中，其它需要使用地方可以直接从ioc中获取
     *
     * @param authenticationConfiguration 导出认证配置
     * @return AuthenticationManager 认证管理器
     */
    @Bean
    @SneakyThrows
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 自定义grant type密码模式中需要使用，所以提前注入ioc中
     *
     * @param userDetailsService 获取用户信息的服务
     * @param encoder            密码解析器
     * @return DaoAuthenticationProvider实例
     */
    @Bean
    @ConditionalOnMissingBean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            UserDetailsService userDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    /**
     * 自定义grant type邮件模式中需要使用，所以提前注入ioc中
     *
     * @param userDetailsService 获取用户信息的服务
     * @return DaoAuthenticationProvider实例
     */
    @Bean
    @ConditionalOnMissingBean
    public EmailCaptchaLoginAuthenticationProvider emailCaptchaLoginAuthenticationProvider(
            UserDetailsService userDetailsService, CaptchaService captchaService) {
        if (log.isDebugEnabled()) {
            log.debug("注入 邮件验证码登录Provider EmailCaptchaLoginAuthenticationProvider.");
        }
        // 这里使用了自定义的验证码登录认证提供者
        return new EmailCaptchaLoginAuthenticationProvider(userDetailsService, captchaService);
    }

    /**
     * 注入一个验证码Service
     *
     * @param redisOperator redis操作类
     * @return CaptchaService
     */
    @Bean
    @ConditionalOnMissingBean
    public CaptchaService captchaService(RedisOperator<String> redisOperator) {
        if (log.isDebugEnabled()) {
            log.debug("注入 验证码Service RedisCaptchaService.");
        }
        return new RedisCaptchaService(oAuth2ServerProperties, redisOperator);
    }

    /**
     * 密码解析器
     *
     * @return 返回一个密码解析器委托类，根据密码找到对应的解析器
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 基于内存的用户服务
     *  TODO 待修改
     *
     * @return UserDetailsService 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public UserDetailsService userDetailsService() {

        return username -> {
            DefaultAuthenticatedUser user = new DefaultAuthenticatedUser("user",
                    OAuth2AccountPlatformEnum.SYSTEM,
                    List.of(new SimpleGrantedAuthority("USER")));
            user.setId(123L);
            user.setPassword("{noop}123456");
            user.setSub(user.getUsername());
            return user;
        };
    }

    /**
     * 基于内存的客户端服务
     *
     * @return RegisteredClientRepository实例
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnInMemoryStorage
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
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

        RegisteredClient deviceClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("device-messaging-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.DEVICE_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("message.read")
                .scope("message.write")
                .build();
        InMemoryRegisteredClientRepository clientRepository = new InMemoryRegisteredClientRepository(oidcClient);
        clientRepository.save(deviceClient);

        RegisteredClient messagingClient = RegisteredClient.withId(UUID.randomUUID().toString())
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

        clientRepository.save(messagingClient);

        return clientRepository;
    }

    /**
     * 基于MybatisPlus的授权服务
     *
     * @return OAuth2AuthorizationConsentService实例
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnInMemoryStorage
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    /**
     * 基于MybatisPlus的授权确认服务
     *
     * @return OAuth2AuthorizationConsentService实例
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnInMemoryStorage
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }

    /**
     * 配置jwk源，使用非对称加密，公开用于检索匹配指定选择器的JWK的方法
     *  TODO 待优化
     *
     * @return JWKSource
     */
    @Bean
    @ConditionalOnMissingBean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 生成rsa密钥对，提供给jwk
     *
     * @return 密钥对
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * 配置jwt解析器
     *
     * @param jwkSource jwk源
     * @return JwtDecoder
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 配置认证服务跨域过滤器
     *
     * @return CorsConfigurationSource 实例
     */
//    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        Set<String> allowedOrigins = oAuth2ServerProperties.getAllowedOrigins();
        if (!ObjectUtils.isEmpty(allowedOrigins)) {
            // 设置允许跨域的域名,如果允许携带cookie的话,路径就不能写*号, *表示所有的域名都可以跨域访问
            allowedOrigins.forEach(config::addAllowedOrigin);
        }
        // 设置跨域访问可以携带cookie
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicOpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
        if (log.isDebugEnabled()) {
            log.debug("注入 自定义认证服务令牌自省 BasicOpaqueTokenIntrospector.");
        }
        return new BasicOpaqueTokenIntrospector(authorizationService);
    }

    @Bean
    @ConditionalOnMissingBean
    public DelegatingTokenAuthenticationResolver delegatingTokenAuthenticationResolver(ApplicationContext applicationContext) {
        if (log.isDebugEnabled()) {
            log.debug("注入 同时支持匿名token与jwt token的解析配置类 DelegatingTokenAuthenticationResolver.");
        }
        return new DelegatingTokenAuthenticationResolver(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationProvider authenticationConverter(JwtDecoder jwtDecoder,
                                                             BasicIdTokenCustomizer idTokenCustomizer) {
        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);

        authenticationProvider.setJwtAuthenticationConverter(new BasicJwtRedisAuthenticationConverter(idTokenCustomizer, redisOperator));
        if (log.isDebugEnabled()) {
            log.debug("注入 从redis中根据jwt获取认证信息的Jwt token解析器.");
        }
        return authenticationProvider;
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtIdTokenCustomizer() {
        if (log.isDebugEnabled()) {
            log.debug("注入 自定义Jwt token内容配置类 JwtIdTokenCustomizer.");
        }
        return new JwtIdTokenCustomizer(redisOperator);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> opaqueIdTokenCustomizer() {
        if (log.isDebugEnabled()) {
            log.debug("注入 自定义Opaque token内容配置类 OpaqueIdTokenCustomizer.");
        }
        return new OpaqueIdTokenCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestContextAuthorizationManager requestContextAuthorizationManager() {
        if (log.isDebugEnabled()) {
            log.debug("注入 基于Webmvc的自定义鉴权配置类 RequestContextAuthorizationManager.");
        }
        return new RequestContextAuthorizationManager(resourceServerProperties, permissionRedisOperator);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReactiveContextAuthorizationManager reactiveContextAuthorizationManager() {
        if (log.isDebugEnabled()) {
            log.debug("注入 基于Webflux的自定义鉴权配置类 ReactiveContextAuthorizationManager.");
        }
        return new ReactiveContextAuthorizationManager(resourceServerProperties, permissionRedisOperator);
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicIdTokenCustomizer basicIdTokenCustomizer() {
        if (log.isDebugEnabled()) {
            log.debug("注入 自定义token内容帮助类 BasicIdTokenCustomizer.");
        }
        return new BasicIdTokenCustomizer(scopePermissionOperator, permissionRedisOperator);
    }

    @PostConstruct
    public void postConstruct() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing OAuth2 AuthorizationServer Configuration.");
        }
    }

}
