package com.basic.cloud.authorization.server.configuration;

import com.basic.framework.oauth2.authorization.server.email.EmailCaptchaLoginConfigurer;
import com.basic.framework.oauth2.core.handler.authentication.LoginFailureHandler;
import com.basic.framework.oauth2.core.handler.authentication.LoginSuccessHandler;
import com.basic.framework.oauth2.core.manager.RequestContextAuthorizationManager;
import com.basic.framework.oauth2.core.property.OAuth2ServerProperties;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Set;

/**
 * 资源服务器配置类
 *
 * @author Vains
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ResourceServerConfiguration {

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

    private final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

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
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        // 合并默认忽略鉴权的地址和配置文件中添加的忽略鉴权的地址
        Set<String> ignoreUriPaths = oAuth2ServerProperties.getIgnoreUriPaths();
        ignoreUriPaths.addAll(DEFAULT_IGNORE_PATHS);

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(ignoreUriPaths.toArray(new String[]{})).permitAll()
                .anyRequest().access(new RequestContextAuthorizationManager())
        );

        // Form login handles the redirect to the login page from the
        // authorization server filter chain
        http.formLogin(form -> form
                .loginPage(oAuth2ServerProperties.getLoginPageUri())
                .loginProcessingUrl(oAuth2ServerProperties.getLoginProcessingUri())
                .successHandler(new LoginSuccessHandler(oAuth2ServerProperties.getLoginPageUri()))
                .failureHandler(new LoginFailureHandler(oAuth2ServerProperties.getLoginPageUri()))
        );

        // 开启oauth2登录支持
        http.oauth2Login(oauth2Login -> oauth2Login
                .loginPage(oAuth2ServerProperties.getLoginPageUri())
        );

        // 添加BearerTokenAuthenticationFilter，将认证服务当做一个资源服务，解析请求头中的token
        http.oauth2ResourceServer((resourceServer) -> resourceServer
                .accessDeniedHandler(SecurityUtils::exceptionHandler)
                .authenticationEntryPoint(SecurityUtils::exceptionHandler)
                .authenticationManagerResolver(authenticationManagerResolver)
        );

        // 添加邮件登录过滤器
        http.with(new EmailCaptchaLoginConfigurer<>(), c -> c
                .loginPage(oAuth2ServerProperties.getLoginPageUri())
                .loginProcessingUrl(oAuth2ServerProperties.getEmailLoginProcessingUri())
                .successHandler(new LoginSuccessHandler(oAuth2ServerProperties.getLoginPageUri()))
                .failureHandler(new LoginFailureHandler(oAuth2ServerProperties.getLoginPageUri()))
        );

        return http.build();
    }

}
