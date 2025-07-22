package com.basic.framework.oauth2.resource.server.configure;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.token.resolver.DelegatingTokenAuthenticationResolver;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 * 通用资源服务配置
 *
 * @author vains
 */
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ResourceServerConfiguration {

    private final AuthorizationManager<RequestAuthorizationContext> requestContextAuthorizationManager;

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver) throws Exception {

        // 禁用 csrf 与 cors
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        // 合并权限
        http.authorizeHttpRequests(authorize -> authorize
                // 忽略指定url的认证、鉴权
                .requestMatchers(AuthorizeConstants.DEFAULT_IGNORE_PATHS.toArray(new String[0])).permitAll()
                .anyRequest().access(requestContextAuthorizationManager)
        );
        http.oauth2ResourceServer(oauth2 -> oauth2
                // 添加未携带token和权限不足异常处理
                .accessDeniedHandler(SecurityUtils::exceptionHandler)
                .authenticationEntryPoint(SecurityUtils::exceptionHandler)
                .authenticationManagerResolver(authenticationManagerResolver)
        );
        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManagerResolver<HttpServletRequest> delegatingTokenAuthenticationResolver(ApplicationContext applicationContext) {
        return new DelegatingTokenAuthenticationResolver(applicationContext);
    }

}
