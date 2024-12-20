package com.basic.cloud.authorization.server.configuration;

import com.basic.framework.core.domain.ScopePermissionModel;
import com.basic.framework.oauth2.authorization.server.email.EmailCaptchaLoginConfigurer;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.handler.authentication.LoginFailureHandler;
import com.basic.framework.oauth2.core.handler.authentication.LoginSuccessHandler;
import com.basic.framework.oauth2.core.property.OAuth2ServerProperties;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2ScopePermissionRepository;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;

/**
 * 资源服务器配置类
 *
 * @author Vains
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ResourceServerConfiguration {

    /**
     * 认证服务配置类
     */
    private final OAuth2ServerProperties oAuth2ServerProperties;

    private final RedisOperator<List<ScopePermissionModel>> redisOperator;

    private final OAuth2ScopePermissionRepository scopePermissionRepository;

    private final OAuth2AuthorizationRequestResolver authorizationRequestResolver;

    private final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

    private final AuthorizationManager<RequestAuthorizationContext> requestContextAuthorizationManager;

    private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;

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

        // 设置忽略鉴权路径与自定义鉴权配置
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AuthorizeConstants.DEFAULT_IGNORE_PATHS.toArray(new String[]{})).permitAll()
                .anyRequest().access(requestContextAuthorizationManager)
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
                // 针对微信的特殊处理，自动替换为微信需要的授权申请参数
                .authorizationEndpoint(authorization -> authorization
                        .authorizationRequestResolver(authorizationRequestResolver)
                )
                // 针对微信的特殊处理，添加“text/plain”格式的响应支持
                .tokenEndpoint(token -> token
                        .accessTokenResponseClient(accessTokenResponseClient)
                )
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
