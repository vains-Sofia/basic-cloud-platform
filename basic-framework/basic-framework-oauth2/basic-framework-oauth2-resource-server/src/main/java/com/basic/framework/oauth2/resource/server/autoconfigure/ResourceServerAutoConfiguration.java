package com.basic.framework.oauth2.resource.server.autoconfigure;

import com.basic.framework.oauth2.core.converter.BasicJwtAuthenticationConverter;
import com.basic.framework.oauth2.resource.server.configure.ReactiveResourceServerConfiguration;
import com.basic.framework.oauth2.resource.server.configure.ResourceServerConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * 资源服务自动配置类
 *
 * @author vains
 */
@Import({
        ResourceServerConfiguration.class,
        ReactiveResourceServerConfiguration.class
})
@Configuration(proxyBeanMethods = false)
public class ResourceServerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationConverter authenticationConverter() {
        return new BasicJwtAuthenticationConverter();
    }

}
