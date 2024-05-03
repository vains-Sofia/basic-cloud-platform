package com.basic.cloud.authorization.server.config;

import com.basic.cloud.oauth2.authorization.property.OAuth2ServerProperties;
import com.basic.cloud.oauth2.authorization.server.customizer.AuthorizationServerMetadataCustomizer;
import com.basic.cloud.oauth2.authorization.server.customizer.OidcConfigurerCustomizer;
import com.basic.cloud.oauth2.authorization.server.email.EmailCaptchaLoginConfigurer;
import com.basic.cloud.oauth2.authorization.server.util.OAuth2ConfigurerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.util.List;
import java.util.Set;

/**
 * 认证服务过滤器链配置
 *
 * @author vains
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {

    /**
     * 默认忽略鉴权的地址
     */
    private final List<String> DEFAULT_IGNORE_PATHS = List.of(
            "/login",
            "/error",
            "/assets/**",
            "/favicon.ico",
            "/login/email",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    /**
     * 认证服务配置类
     */
    private final OAuth2ServerProperties oAuth2ServerProperties;

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
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        // 认证服务默认配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 获取认证服务配置
        OAuth2AuthorizationServerConfigurer configurer = http.getConfigurer(OAuth2AuthorizationServerConfigurer.class);

        // 开启oidc并在 /.well-known/openid-configuration 和 /.well-known/oauth-authorization-server 端点中添加自定义grant type
        configurer.oidc(new OidcConfigurerCustomizer());
        configurer.authorizationServerMetadataEndpoint(new AuthorizationServerMetadataCustomizer());

        // 添加邮件模式
        OAuth2ConfigurerUtils.configureEmailGrantType(http, (null), (null));
        // 添加密码模式
        OAuth2ConfigurerUtils.configurePasswordGrantType(http, (null), (null));
        // 添加设备码流程
        OAuth2ConfigurerUtils.configureDeviceGrantType(http, oAuth2ServerProperties);

        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

    /**
     * 认证与鉴权相关的过滤器链配置
     *
     * @param http httpSecurity 实例
     * @return 认证鉴权相关的过滤器链
     * @throws Exception 配置错误时抛出
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        // 禁用 csrf 与 cors
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        // 合并默认忽略鉴权的地址和配置文件中添加的忽略鉴权的地址
        Set<String> ignoreUriPaths = oAuth2ServerProperties.getServer().getIgnoreUriPaths();
        ignoreUriPaths.addAll(DEFAULT_IGNORE_PATHS);

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(ignoreUriPaths.toArray(new String[]{})).permitAll()
                .anyRequest().authenticated()
        );

        // Form login handles the redirect to the login page from the
        // authorization server filter chain
        http.formLogin(form -> form
                .loginPage(oAuth2ServerProperties.getServer().getLoginPageUri())
                .loginProcessingUrl("/login")
        );

        // 开启oauth2登录支持
        http.oauth2Login(oauth2Login -> oauth2Login
                .loginPage(oAuth2ServerProperties.getServer().getLoginPageUri())
        );

        // 添加BearerTokenAuthenticationFilter，将认证服务当做一个资源服务，解析请求头中的token
        http.oauth2ResourceServer((resourceServer) -> resourceServer
                .jwt(Customizer.withDefaults())
        );

        // 添加邮件登录过滤器
        http.with(new EmailCaptchaLoginConfigurer<>(), c -> c
                .successHandler((request, response, authentication) -> {
                    response.setContentType("text/html;charset=utf-8");
                    response.getWriter().write("登录成功");
                    response.getWriter().flush();
                })
        );

        return http.build();
    }

}
