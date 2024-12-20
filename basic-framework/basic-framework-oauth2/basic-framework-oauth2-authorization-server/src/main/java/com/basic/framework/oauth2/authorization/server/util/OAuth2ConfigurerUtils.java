package com.basic.framework.oauth2.authorization.server.util;

import com.basic.framework.oauth2.core.customizer.BasicIdTokenCustomizer;
import com.basic.framework.oauth2.core.customizer.JwtIdTokenCustomizer;
import com.basic.framework.oauth2.core.customizer.OpaqueIdTokenCustomizer;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.handler.authentication.DeviceAuthorizationResponseHandler;
import com.basic.framework.oauth2.core.property.OAuth2ServerProperties;
import com.basic.framework.oauth2.authorization.server.email.EmailCaptchaLoginAuthenticationProvider;
import com.basic.framework.oauth2.authorization.server.grant.device.OAuth2DeviceClientAuthenticationConverter;
import com.basic.framework.oauth2.authorization.server.grant.device.OAuth2DeviceClientAuthenticationProvider;
import com.basic.framework.oauth2.authorization.server.grant.email.OAuth2EmailCaptchaAuthenticationConverter;
import com.basic.framework.oauth2.authorization.server.grant.email.OAuth2EmailCaptchaAuthenticationProvider;
import com.basic.framework.oauth2.authorization.server.grant.password.OAuth2PasswordAuthenticationConverter;
import com.basic.framework.oauth2.authorization.server.grant.password.OAuth2PasswordAuthenticationProvider;
import com.basic.framework.redis.support.RedisOperator;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * oauth2配置类工具类
 * (该工具类在Spring authorization server中是包级可访问的，提取一些用得到的工具方法出来)
 *
 * @author vains
 */
public class OAuth2ConfigurerUtils {

    /**
     * 从ioc中获取bean，如果ioc中没有则返回null
     *
     * @param builder httpSecurity实例
     * @param type    要获取bean的class
     * @param <T>     要获取的bean的具体类型
     * @param <B>     HttpSecurityBuilder具体的子类
     * @return bean的实例或null
     */
    public static <T, B extends HttpSecurityBuilder<B>> T getBeanOrNull(HttpSecurityBuilder<B> builder, Class<T> type) {
        ApplicationContext context = builder.getSharedObject(ApplicationContext.class);
        if (context != null) {
            String[] names = context.getBeanNamesForType(type);
            if (names.length == 1) {
                return context.getBean(type);
            }
        }
        return null;
    }

    /**
     * 从httpSecurity的缓存中获取 {@link OAuth2AuthorizationService}的实例，
     * 若获取失败从ioc中获取，若获取初始化一个基于内存的实例
     *
     * @param httpSecurity 实例
     * @return OAuth2AuthorizationService的实例
     */
    public static OAuth2AuthorizationService getAuthorizationService(HttpSecurity httpSecurity) {
        OAuth2AuthorizationService authorizationService = httpSecurity.getSharedObject(OAuth2AuthorizationService.class);
        if (authorizationService == null) {
            authorizationService = getOptionalBean(httpSecurity, OAuth2AuthorizationService.class);
            if (authorizationService == null) {
                authorizationService = new InMemoryOAuth2AuthorizationService();
            }
            httpSecurity.setSharedObject(OAuth2AuthorizationService.class, authorizationService);
        }
        return authorizationService;
    }

    /**
     * 获取{@link OAuth2TokenGenerator}的实例，依次从 HttpSecurity 的缓存、IOC中获取，若获取失败则初始化一个默认的实例返回
     *
     * @param httpSecurity 实例
     * @return {@link OAuth2TokenGenerator}的实例
     */
    @SuppressWarnings("unchecked")
    public static OAuth2TokenGenerator<? extends OAuth2Token> getTokenGenerator(HttpSecurity httpSecurity) {
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = httpSecurity.getSharedObject(OAuth2TokenGenerator.class);
        if (tokenGenerator == null) {
            tokenGenerator = getOptionalBean(httpSecurity, OAuth2TokenGenerator.class);
            if (tokenGenerator == null) {
                JwtGenerator jwtGenerator = getJwtGenerator(httpSecurity);
                OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
                OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer = getAccessTokenCustomizer(httpSecurity);
                BasicIdTokenCustomizer idTokenCustomizer = getOptionalBean(httpSecurity, BasicIdTokenCustomizer.class);
                accessTokenGenerator.setAccessTokenCustomizer(Objects.requireNonNullElseGet(accessTokenCustomizer, () -> new OpaqueIdTokenCustomizer(idTokenCustomizer)));
                OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
                if (jwtGenerator != null) {
                    tokenGenerator = new DelegatingOAuth2TokenGenerator(
                            jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
                } else {
                    tokenGenerator = new DelegatingOAuth2TokenGenerator(
                            accessTokenGenerator, refreshTokenGenerator);
                }
            }
            httpSecurity.setSharedObject(OAuth2TokenGenerator.class, tokenGenerator);
        }
        return tokenGenerator;
    }

    /**
     * 从ioc中获取bean的实例，当容器中对应的bean的实例数量大于1则抛出异常，若获取失败则返回null
     *
     * @param httpSecurity 配置实例
     * @param type         bean的class
     * @param <T>          bean的泛型
     * @return bean的实例
     */
    public static <T> T getOptionalBean(HttpSecurity httpSecurity, Class<T> type) {
        Map<String, T> beansMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                httpSecurity.getSharedObject(ApplicationContext.class), type);
        if (beansMap.size() > 1) {
            throw new NoUniqueBeanDefinitionException(type, beansMap.size(),
                    "Expected single matching bean of type '" + type.getName() + "' but found " +
                            beansMap.size() + ": " + StringUtils.collectionToCommaDelimitedString(beansMap.keySet()));
        }
        return (!beansMap.isEmpty() ? beansMap.values().iterator().next() : null);
    }

    /**
     * 获取jwt的生成器，依次从HttpSecurity的缓存、IOC中获取，获取失败则初始化一个默认的
     *
     * @param httpSecurity 配置实例
     * @return jwt的生成器
     */
    private static JwtGenerator getJwtGenerator(HttpSecurity httpSecurity) {
        JwtGenerator jwtGenerator = httpSecurity.getSharedObject(JwtGenerator.class);
        if (jwtGenerator == null) {
            JwtEncoder jwtEncoder = getJwtEncoder(httpSecurity);
            if (jwtEncoder != null) {
                jwtGenerator = new JwtGenerator(jwtEncoder);
                OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer = getJwtCustomizer(httpSecurity);
                if (jwtCustomizer != null) {
                    ResolvableType type = ResolvableType.forClassWithGenerics(RedisOperator.class, AuthenticatedUser.class);
                    RedisOperator<AuthenticatedUser> redisOperator = getOptionalBean(httpSecurity, type);
                    BasicIdTokenCustomizer idTokenCustomizer = getOptionalBean(httpSecurity, BasicIdTokenCustomizer.class);
                    jwtGenerator.setJwtCustomizer(Objects.requireNonNullElseGet(jwtCustomizer, () -> new JwtIdTokenCustomizer(idTokenCustomizer, redisOperator)));
                }
                httpSecurity.setSharedObject(JwtGenerator.class, jwtGenerator);
            }
        }
        return jwtGenerator;
    }

    /**
     * 获取jwt的生成器，依次从HttpSecurity的缓存、IOC中获取，获取失败则初始化一个默认的
     *
     * @param httpSecurity 配置实例
     * @return jwt的生成器
     */
    private static JwtEncoder getJwtEncoder(HttpSecurity httpSecurity) {
        JwtEncoder jwtEncoder = httpSecurity.getSharedObject(JwtEncoder.class);
        if (jwtEncoder == null) {
            jwtEncoder = getOptionalBean(httpSecurity, JwtEncoder.class);
            if (jwtEncoder == null) {
                JWKSource<SecurityContext> jwkSource = getJwkSource(httpSecurity);
                if (jwkSource != null) {
                    jwtEncoder = new NimbusJwtEncoder(jwkSource);
                }
            }
            if (jwtEncoder != null) {
                httpSecurity.setSharedObject(JwtEncoder.class, jwtEncoder);
            }
        }
        return jwtEncoder;
    }

    /**
     * 获取jwk源，依此从HttpSecurity缓存、IOC中获取，获取失败则返回null
     *
     * @return JWKSource
     */
    public static JWKSource<SecurityContext> getJwkSource(HttpSecurity httpSecurity) {
        @SuppressWarnings("unchecked")
        JWKSource<SecurityContext> jwkSource = httpSecurity.getSharedObject(JWKSource.class);
        if (jwkSource == null) {
            ResolvableType type = ResolvableType.forClassWithGenerics(JWKSource.class, SecurityContext.class);
            jwkSource = getOptionalBean(httpSecurity, type);
            if (jwkSource != null) {
                httpSecurity.setSharedObject(JWKSource.class, jwkSource);
            }
        }
        return jwkSource;
    }

    /**
     * 从ioc中获取bean的实例，当容器中对应的bean的实例数量大于1则抛出异常，若获取失败则返回null
     *
     * @param httpSecurity 配置实例
     * @param type         bean的class
     * @param <T>          bean的泛型
     * @return bean的实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getOptionalBean(HttpSecurity httpSecurity, ResolvableType type) {
        ApplicationContext context = httpSecurity.getSharedObject(ApplicationContext.class);
        String[] names = context.getBeanNamesForType(type);
        if (names.length > 1) {
            throw new NoUniqueBeanDefinitionException(type, names);
        }
        return names.length == 1 ? (T) context.getBean(names[0]) : null;
    }

    /**
     * 获取jwt自定义器实例
     *
     * @param httpSecurity 配置实例
     * @return jwt自定义器实例
     */
    private static OAuth2TokenCustomizer<JwtEncodingContext> getJwtCustomizer(HttpSecurity httpSecurity) {
        ResolvableType type = ResolvableType.forClassWithGenerics(OAuth2TokenCustomizer.class, JwtEncodingContext.class);
        return getOptionalBean(httpSecurity, type);
    }

    /**
     * 获取Access Token自定义器实例
     *
     * @param httpSecurity 配置实例
     * @return Access Token自定义器实例
     */
    private static OAuth2TokenCustomizer<OAuth2TokenClaimsContext> getAccessTokenCustomizer(HttpSecurity httpSecurity) {
        ResolvableType type = ResolvableType.forClassWithGenerics(OAuth2TokenCustomizer.class, OAuth2TokenClaimsContext.class);
        return getOptionalBean(httpSecurity, type);
    }

    /**
     * 配置自定义GrantType之密码模式
     *
     * @param builder           httpSecurity 实例
     * @param usernameParameter 账号参数名，可为空
     * @param passwordParameter 密码参数名，可为空
     */
    public static void configurePasswordGrantType(HttpSecurity builder, String usernameParameter, String passwordParameter) {
        SessionRegistry sessionRegistry = OAuth2ConfigurerUtils.getBeanOrNull(builder, SessionRegistry.class);
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(builder);
        DaoAuthenticationProvider authenticationProvider = OAuth2ConfigurerUtils.getOptionalBean(builder, DaoAuthenticationProvider.class);
        OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils.getAuthorizationService(builder);

        // 自定义密码模式认证转换器
        OAuth2PasswordAuthenticationConverter converter =
                new OAuth2PasswordAuthenticationConverter();
        OAuth2PasswordAuthenticationProvider provider =
                new OAuth2PasswordAuthenticationProvider(sessionRegistry, tokenGenerator, authenticationProvider, authorizationService);
        // 如果设置了账号密码参数名，则替换默认的
        if (!ObjectUtils.isEmpty(usernameParameter)) {
            converter.setUsernameParameter(usernameParameter);
            provider.setUsernameParameter(usernameParameter);
        }
        if (!ObjectUtils.isEmpty(passwordParameter)) {
            converter.setPasswordParameter(passwordParameter);
            provider.setPasswordParameter(passwordParameter);
        }

        builder.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 添加自定义grant_type——密码模式
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        .accessTokenRequestConverter(converter)
                        .authenticationProvider(provider));
    }

    /**
     * 配置自定义GrantType之邮件模式
     *
     * @param builder               httpSecurity 实例
     * @param emailParameter        邮箱地址参数名，可为空
     * @param emailCaptchaParameter 邮件验证码参数名，可为空
     */
    public static void configureEmailGrantType(HttpSecurity builder, String emailParameter, String emailCaptchaParameter) {
        SessionRegistry sessionRegistry = OAuth2ConfigurerUtils.getBeanOrNull(builder, SessionRegistry.class);
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(builder);
        EmailCaptchaLoginAuthenticationProvider authenticationProvider = OAuth2ConfigurerUtils.getOptionalBean(builder, EmailCaptchaLoginAuthenticationProvider.class);
        OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils.getAuthorizationService(builder);

        // 自定义邮件模式认证转换器
        OAuth2EmailCaptchaAuthenticationConverter converter =
                new OAuth2EmailCaptchaAuthenticationConverter();
        OAuth2EmailCaptchaAuthenticationProvider provider =
                new OAuth2EmailCaptchaAuthenticationProvider(sessionRegistry, tokenGenerator, authenticationProvider, authorizationService);
        // 如果设置了邮件、验证码参数名，则替换默认的
        if (!ObjectUtils.isEmpty(emailParameter)) {
            converter.setEmailParameter(emailParameter);
            provider.setEmailParameter(emailParameter);
        }
        if (!ObjectUtils.isEmpty(emailCaptchaParameter)) {
            converter.setEmailCaptchaParameter(emailCaptchaParameter);
            provider.setEmailCaptchaParameter(emailCaptchaParameter);
        }

        builder.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 添加自定义grant_type——邮件模式
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        .accessTokenRequestConverter(converter)
                        .authenticationProvider(provider));
    }

    /**
     * 配置自定义GrantType之设备码流程
     *
     * @param builder                httpSecurity 实例
     * @param oAuth2ServerProperties 认证服务自定义配置
     */
    public static void configureDeviceGrantType(HttpSecurity builder, OAuth2ServerProperties oAuth2ServerProperties) {
        RegisteredClientRepository registeredClientRepository = OAuth2ConfigurerUtils.getBeanOrNull(builder, RegisteredClientRepository.class);
        AuthorizationServerSettings authorizationServerSettings = OAuth2ConfigurerUtils.getBeanOrNull(builder, AuthorizationServerSettings.class);

        Assert.notNull(authorizationServerSettings, "认证服务配置不能为空.");

        // 自定义设备码转换器
        OAuth2DeviceClientAuthenticationConverter converter =
                new OAuth2DeviceClientAuthenticationConverter("/**" + authorizationServerSettings.getDeviceAuthorizationEndpoint());
        OAuth2DeviceClientAuthenticationProvider provider =
                new OAuth2DeviceClientAuthenticationProvider(registeredClientRepository);


        builder.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 设置设备码用户验证url(自定义用户验证页)
                .deviceAuthorizationEndpoint(deviceAuthorizationEndpoint ->
                        deviceAuthorizationEndpoint.verificationUri(oAuth2ServerProperties.getDeviceVerificationUri()))
                // 设置验证设备码用户确认页面
                .deviceVerificationEndpoint(deviceVerificationEndpoint -> deviceVerificationEndpoint
                        .consentPage(oAuth2ServerProperties.getConsentPageUri())
//                        .errorResponseHandler(new ConsentAuthenticationFailureHandler(serverProperties.getConsentPageUri()))
                        .deviceVerificationResponseHandler(new DeviceAuthorizationResponseHandler(oAuth2ServerProperties.getDeviceActivatedPageUri()))
                )
                .clientAuthentication(clientAuthentication ->
                        // 客户端认证添加设备码的converter和provider
                        clientAuthentication
                                .authenticationProvider(provider)
                                .authenticationConverter(converter)
                );
    }

}
