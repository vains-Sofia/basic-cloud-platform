package com.basic.framework.oauth2.federation.autoconfigure;

import com.basic.framework.oauth2.core.customizer.JwtIdTokenCustomizer;
import com.basic.framework.oauth2.federation.converter.OAuth2UserConverter;
import com.basic.framework.oauth2.federation.converter.context.OAuth2UserConverterContext;
import com.basic.framework.oauth2.federation.converter.impl.GiteeUserConverter;
import com.basic.framework.oauth2.federation.converter.impl.GithubUserConverter;
import com.basic.framework.oauth2.federation.converter.impl.WechatUserConverter;
import com.basic.framework.oauth2.federation.service.BasicOAuth2UserService;
import com.basic.framework.oauth2.federation.wechat.BasicAccessTokenResponseClient;
import com.basic.framework.oauth2.federation.wechat.BasicAuthorizationRequestResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Map;

import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.*;

/**
 * 联合身份认证自动配置类
 *
 * @author vains
 */
@RequiredArgsConstructor
public class FederatedIdentityAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtIdTokenCustomizer() {
        return new JwtIdTokenCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new BasicAccessTokenResponseClient();
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {
        return new BasicAuthorizationRequestResolver(clientRegistrationRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2UserConverterContext userConverterContext(
            Map<String, OAuth2UserConverter> userConverterMap) {
        return new OAuth2UserConverterContext(userConverterMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            OAuth2UserConverterContext userConverterContext) {
        return new BasicOAuth2UserService(userConverterContext);
    }

    @Bean(THIRD_LOGIN_GITEE)
    @ConditionalOnMissingBean(name = THIRD_LOGIN_GITEE)
    public OAuth2UserConverter giteeUserConverter() {
        return new GiteeUserConverter();
    }

    @Bean(THIRD_LOGIN_GITHUB)
    @ConditionalOnMissingBean(name = THIRD_LOGIN_GITHUB)
    public OAuth2UserConverter githubUserConverter() {
        return new GithubUserConverter();
    }

    @Bean(THIRD_LOGIN_WECHAT)
    @ConditionalOnMissingBean(name = THIRD_LOGIN_WECHAT)
    public OAuth2UserConverter wechatUserConverter() {
        return new WechatUserConverter();
    }

}
