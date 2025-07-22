package com.basic.framework.oauth2.authorization.server.customizer;

import com.basic.framework.oauth2.core.constant.BasicAuthorizationGrantType;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerMetadataEndpointConfigurer;

/**
 * 认证服务元数据自定义内容
 *
 * @author vains
 */
public class AuthorizationServerMetadataCustomizer implements Customizer<OAuth2AuthorizationServerMetadataEndpointConfigurer> {

    @Override
    public void customize(OAuth2AuthorizationServerMetadataEndpointConfigurer metadataEndpointConfigurer) {
        // 让认证服务器元数据中有自定义的认证方式
        metadataEndpointConfigurer.authorizationServerMetadataCustomizer(customizer -> customizer
                .grantType(BasicAuthorizationGrantType.PASSWORD.getValue())
        );
    }
}
