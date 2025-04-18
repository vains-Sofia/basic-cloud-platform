package com.basic.cloud.monitor.configuration;

import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 自定义配置
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ClientSecurityConfiguration {

    private final OAuth2ClientProperties oauth2ClientProperties;

    private final OAuth2AuthorizedClientService authorizedClientService;

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    /**
     * 自定义代理请求头
     *
     * @return HttpHeadersProvider
     */
    @Bean
    public HttpHeadersProvider customHttpHeadersProvider() {
        return (instance) -> {
            HttpHeaders headers = new HttpHeaders();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            // 用户上下文下的 token（浏览器访问时）
            if (auth instanceof OAuth2AuthenticationToken oauth2Token) {
                String clientRegistrationId = oauth2Token.getAuthorizedClientRegistrationId();
                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                        clientRegistrationId, oauth2Token.getName());

                if (client != null && client.getAccessToken() != null) {
                    String token = client.getAccessToken().getTokenValue();
                    headers.setBearerAuth(token);
                    return headers;
                }
            }

            // 后台线程 fallback：client_credentials 获取 token
            Map<String, OAuth2ClientProperties.Registration> registrationMap = oauth2ClientProperties.getRegistration();
            if (!ObjectUtils.isEmpty(registrationMap)) {
                registrationMap.forEach((clientRegistrationId, registration) -> {
                    // 筛选出 client_credentials 类型的注册信息
                    if (Objects.equals(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue(), registration.getAuthorizationGrantType())) {
                        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                                .withClientRegistrationId(clientRegistrationId)
                                .principal("admin-server")
                                .build();

                        OAuth2AuthorizedClient fallbackClient = authorizedClientManager.authorize(request);
                        if (fallbackClient != null && fallbackClient.getAccessToken() != null) {
                            headers.setBearerAuth(fallbackClient.getAccessToken().getTokenValue());
                        }
                    }
                });
            }

            return headers;
        };
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults());
        // 添加BearerTokenAuthenticationFilter，解析请求头中的token
        http.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }

}
