package com.basic.cloud.oauth2.authorization.server.customizer;

import com.basic.cloud.oauth2.authorization.server.core.BasicAuthorizationGrantType;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcConfigurer;

/**
 * Oidc配置封装
 *
 * @author vains
 */
public class OidcConfigurerCustomizer implements Customizer<OidcConfigurer> {

    @Override
    public void customize(OidcConfigurer oidcConfigurer) {
        oidcConfigurer.providerConfigurationEndpoint(providerEndpoint -> providerEndpoint
                .providerConfigurationCustomizer(customizer -> customizer
                        // 为OIDC端点添加密码模式的grant type
                        .grantType(BasicAuthorizationGrantType.PASSWORD.getValue())
                        .grantType(BasicAuthorizationGrantType.EMAIL.getValue())
                )
        );
    }
}
