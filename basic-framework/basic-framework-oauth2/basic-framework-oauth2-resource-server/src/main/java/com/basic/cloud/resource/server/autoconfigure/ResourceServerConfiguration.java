package com.basic.cloud.resource.server.autoconfigure;

import com.basic.cloud.oauth2.authorization.converter.BasicJwtAuthenticationConverter;
import com.basic.cloud.oauth2.authorization.manager.DelegatingTokenAuthenticationResolver;
import com.basic.cloud.oauth2.authorization.manager.RequestContextAuthorizationManager;
import com.basic.cloud.oauth2.authorization.util.SecurityUtils;
import com.basic.cloud.resource.server.property.ResourceServerProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

/**
 * 通用资源服务配置
 *
 * @author vains
 */
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties({ResourceServerProperties.class})
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class ResourceServerConfiguration {

    /**
     * 默认忽略鉴权的地址
     */
    private final Set<String> DEFAULT_IGNORE_PATHS = Set.of(
            "/login",
            "/error",
            "/assets/**",
            "/favicon.ico",
            "/login/email",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    private final ResourceServerProperties resourceServerProperties;

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver) throws Exception {

        // 禁用 csrf 与 cors
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorize -> authorize
                // 忽略指定url的认证、鉴权
                .requestMatchers(DEFAULT_IGNORE_PATHS.toArray(new String[0])).permitAll()
                .requestMatchers(resourceServerProperties.getIgnoreUriPaths().toArray(new String[0])).permitAll()
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
    public AuthenticationManagerResolver<HttpServletRequest> delegatingTokenAuthenticationResolver(ApplicationContext applicationContext) {
        return new DelegatingTokenAuthenticationResolver(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationConverter authenticationConverter() {
        return new BasicJwtAuthenticationConverter();
    }

}
