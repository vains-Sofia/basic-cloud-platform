package com.basic.framework.oauth2.authorization.server.customizer;

import com.basic.framework.oauth2.core.core.BasicAuthorizationGrantType;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.handler.authentication.BasicOidcLogoutAuthenticationSuccessHandler;
import com.basic.framework.oauth2.core.oidc.BasicOidcUserInfoMapper;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcConfigurer;

/**
 * Oidc配置封装
 *
 * @author vains
 */
@RequiredArgsConstructor
public class OidcConfigurerCustomizer implements Customizer<OidcConfigurer> {

    private final RedisOperator<AuthenticatedUser> redisOperator;

    @Override
    public void customize(OidcConfigurer oidcConfigurer) {
        oidcConfigurer.providerConfigurationEndpoint(providerEndpoint -> providerEndpoint
                        .providerConfigurationCustomizer(customizer -> customizer
                                // 为OIDC端点添加密码模式的grant type
                                .grantType(BasicAuthorizationGrantType.PASSWORD.getValue())
                        )
                )
                .logoutEndpoint(logout -> logout
                        .logoutResponseHandler(new BasicOidcLogoutAuthenticationSuccessHandler(redisOperator))
                )
                // 自定义oidc用户信息响应配置
                .userInfoEndpoint(userInfo -> userInfo
                        .userInfoMapper(new BasicOidcUserInfoMapper())
                );
    }
}
