package com.basic.cloud.authorization.server.configuration;

import com.basic.framework.oauth2.authorization.server.customizer.AuthorizationServerMetadataCustomizer;
import com.basic.framework.oauth2.authorization.server.customizer.OidcConfigurerCustomizer;
import com.basic.framework.oauth2.authorization.server.util.OAuth2ConfigurerUtils;
import com.basic.framework.oauth2.core.handler.authentication.ConsentAuthenticationFailureHandler;
import com.basic.framework.oauth2.core.handler.authentication.ConsentAuthorizationResponseHandler;
import com.basic.framework.oauth2.core.handler.authentication.LoginTargetAuthenticationEntryPoint;
import com.basic.framework.oauth2.core.property.OAuth2ServerProperties;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

/**
 * 认证服务过滤器链配置
 *
 * @author vains
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {

    /**
     * 认证服务配置类
     */
    private final OAuth2ServerProperties oAuth2ServerProperties;

    private final AuthorizationServerSettings authorizationServerSettings;

    private final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

    /**
     * 认证服务oauth2端点配置
     *
     * @param http httpSecurity 实例
     * @return SecurityFilterChain认证服务过滤器链
     * @throws Exception 配置错误时抛出
     */
    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {

        // 禁用 csrf 与 cors
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        // 认证服务默认配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 获取认证服务配置
        OAuth2AuthorizationServerConfigurer configurer = http.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

        // 授权申请的/oauth2/authorize相关端点的自定义配置
        configurer.authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                // 设置自定义用户确认授权页
                .consentPage(oAuth2ServerProperties.getConsentPageUri())
                // 异常处理
                .errorResponseHandler(new ConsentAuthenticationFailureHandler(oAuth2ServerProperties.getConsentPageUri()))
                // 授权申请成功响应处理
                .authorizationResponseHandler(new ConsentAuthorizationResponseHandler(oAuth2ServerProperties.getConsentPageUri()))
        );

        // 获取access token的/oauth2/token端点的自定义配置
        configurer.tokenEndpoint(tokenEndpoint -> {
            // 请求access token端点异常处理
            tokenEndpoint.errorResponseHandler(SecurityUtils::exceptionHandler);
        });

        // 开启oidc并在 /.well-known/openid-configuration 和 /.well-known/oauth-authorization-server 端点中添加自定义grant type
        configurer.oidc(new OidcConfigurerCustomizer());
        configurer.authorizationServerMetadataEndpoint(new AuthorizationServerMetadataCustomizer());

        // 添加密码模式
        OAuth2ConfigurerUtils.configurePasswordGrantType(http, (null), (null));
        // 添加设备码流程
        OAuth2ConfigurerUtils.configureDeviceGrantType(http, oAuth2ServerProperties);

        // Redirect to the login page when not authenticated from the
        // authorization endpoint
        http.exceptionHandling((exceptions) -> exceptions
                .defaultAuthenticationEntryPointFor(
                        new LoginTargetAuthenticationEntryPoint(
                                oAuth2ServerProperties.getLoginPageUri(), oAuth2ServerProperties.getDeviceVerificationUri(), authorizationServerSettings.getDeviceVerificationEndpoint()),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
        );

        // Accept access tokens for User Info and/or Client Registration
        http.oauth2ResourceServer((resourceServer) -> resourceServer
                .accessDeniedHandler(SecurityUtils::exceptionHandler)
                .authenticationEntryPoint(SecurityUtils::exceptionHandler)
                .authenticationManagerResolver(authenticationManagerResolver)
        );
        return http.build();
    }

}
