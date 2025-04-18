package com.basic.cloud.monitor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

/**
 * 自定义OAuth2AuthorizedClientManager，让它不依赖Servlet
 *
 * @author vains
 */
@Configuration
public class OAuth2ClientConfiguration {

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clients,
            OAuth2AuthorizedClientService clientService) {

        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .clientCredentials()
                .build();

        // 替换掉默认的 repository（它不会依赖 servlet）
        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clients, clientService);

        manager.setAuthorizedClientProvider(provider);

        return manager;
    }
}
