package com.basic.framework.oauth2.resource.server.configure;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.manager.DelegatingReactiveTokenAuthenticationResolver;
import com.basic.framework.oauth2.core.manager.ReactiveContextAuthorizationManager;
import com.basic.framework.oauth2.core.util.ReactiveSecurityUtils;
import com.basic.framework.oauth2.resource.server.property.ResourceServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;

/**
 * webflux下的资源服务配置
 *
 * @author vains
 */
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ResourceServerProperties.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveResourceServerConfiguration {

    private final ResourceServerProperties resourceServerProperties;

    @Bean
    @ConditionalOnMissingBean
    public SecurityWebFilterChain defaultSecurityFilterChain(ServerHttpSecurity http,
                                                             ReactiveAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver) {

        // 禁用 csrf 与 cors
        http.cors(Customizer.withDefaults());
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        // 合并权限
        AuthorizeConstants.DEFAULT_IGNORE_PATHS.addAll(resourceServerProperties.getIgnoreUriPaths());
        http.authorizeExchange(authorize -> authorize
                // 忽略指定url的认证、鉴权
                .pathMatchers(AuthorizeConstants.DEFAULT_IGNORE_PATHS.toArray(new String[0])).permitAll()
                .anyExchange().access(new ReactiveContextAuthorizationManager())
        );
        http.oauth2ResourceServer(oauth2 -> oauth2
                // 添加未携带token和权限不足异常处理
                .accessDeniedHandler(ReactiveSecurityUtils::accessDeniedHandler)
                .authenticationEntryPoint(ReactiveSecurityUtils::authenticationEntryPoint)
                .authenticationManagerResolver(authenticationManagerResolver)
        );
        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReactiveAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver(ApplicationContext applicationContext) {
        return new DelegatingReactiveTokenAuthenticationResolver(applicationContext);
    }

}
