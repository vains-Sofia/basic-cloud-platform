package com.basic.cloud.resource.server.configuration;

import com.basic.cloud.oauth2.authorization.converter.BasicJwtAuthenticationConverter;
import com.basic.cloud.oauth2.authorization.manager.DelegatingTokenAuthenticationResolver;
import com.basic.cloud.oauth2.authorization.manager.RequestContextAuthorizationManager;
import com.basic.cloud.oauth2.authorization.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 通用资源服务配置
 *
 * @author vains
 */
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class ResourceServerConfiguration {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .anyRequest().access(new RequestContextAuthorizationManager())
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
    public AuthenticationManagerResolver<HttpServletRequest> delegatingTokenAuthenticationResolver(OpaqueTokenIntrospector opaqueTokenIntrospector,
                                                                                                   ApplicationContext applicationContext) {
        return new DelegatingTokenAuthenticationResolver(opaqueTokenIntrospector, applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationConverter authenticationConverter() {
        return new BasicJwtAuthenticationConverter();
    }

}
