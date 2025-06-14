package com.basic.framework.oauth2.federation.autoconfigure;

import com.basic.framework.oauth2.core.customizer.JwtIdTokenCustomizer;
import com.basic.framework.oauth2.core.customizer.OpaqueIdTokenCustomizer;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.federation.converter.OAuth2UserConverter;
import com.basic.framework.oauth2.federation.converter.context.OAuth2UserConverterContext;
import com.basic.framework.oauth2.federation.converter.impl.GiteeUserConverter;
import com.basic.framework.oauth2.federation.converter.impl.GithubUserConverter;
import com.basic.framework.oauth2.federation.converter.impl.WechatUserConverter;
import com.basic.framework.oauth2.federation.service.BasicOAuth2UserService;
import com.basic.framework.oauth2.federation.wechat.BasicAccessTokenResponseClient;
import com.basic.framework.oauth2.federation.wechat.BasicAuthorizationRequestResolver;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Map;

import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.*;

/**
 * 联合身份认证自动配置类
 *
 * @author vains
 */
@Slf4j
@RequiredArgsConstructor
public class FederatedIdentityAutoConfiguration {

    private final RedisOperator<Long> redisHashOperator;

    private final RedisOperator<AuthenticatedUser> redisOperator;

    @Bean
    @ConditionalOnMissingBean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtIdTokenCustomizer() {
        if (log.isDebugEnabled()) {
            log.debug("注入 自定义Jwt token内容配置类 JwtIdTokenCustomizer.");
        }
        return new JwtIdTokenCustomizer(redisHashOperator, redisOperator);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> opaqueIdTokenCustomizer() {
        if (log.isDebugEnabled()) {
            log.debug("注入 自定义Opaque token内容配置类 OpaqueIdTokenCustomizer.");
        }
        return new OpaqueIdTokenCustomizer(redisHashOperator, redisOperator);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        if (log.isDebugEnabled()) {
            log.debug("注入 自定义AccessToken响应处理器 BasicAccessTokenResponseClient，添加“text/plain”类型数据响应处理支持.");
        }
        return new BasicAccessTokenResponseClient();
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {
        if (log.isDebugEnabled()) {
            log.debug("注入 微信登录-授权申请请求发起适配处理 OAuth2AuthorizationRequestResolver.");
        }
        return new BasicAuthorizationRequestResolver(clientRegistrationRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2UserConverterContext userConverterContext(
            Map<String, OAuth2UserConverter> userConverterMap) {
        if (log.isDebugEnabled()) {
            log.debug("注入 三方登录用户信息转换器 OAuth2UserConverterContext，OAuth2UserConverter集合：{}", userConverterMap);
        }
        return new OAuth2UserConverterContext(userConverterMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            OAuth2UserConverterContext userConverterContext) {
        if (log.isDebugEnabled()) {
            log.debug("注入 自定义三方登录用户信息获取Service OAuth2UserService.");
        }
        return new BasicOAuth2UserService(userConverterContext);
    }

    @Bean(THIRD_LOGIN_GITEE)
    @ConditionalOnMissingBean(name = THIRD_LOGIN_GITEE)
    public OAuth2UserConverter giteeUserConverter() {
        if (log.isDebugEnabled()) {
            log.debug("注入 Gitee登录用户信息转换器 GiteeUserConverter.");
        }
        return new GiteeUserConverter();
    }

    @Bean(THIRD_LOGIN_GITHUB)
    @ConditionalOnMissingBean(name = THIRD_LOGIN_GITHUB)
    public OAuth2UserConverter githubUserConverter() {
        if (log.isDebugEnabled()) {
            log.debug("注入 Github登录用户信息转换器 GithubUserConverter.");
        }
        return new GithubUserConverter();
    }

    @Bean(THIRD_LOGIN_WECHAT)
    @ConditionalOnMissingBean(name = THIRD_LOGIN_WECHAT)
    public OAuth2UserConverter wechatUserConverter() {
        if (log.isDebugEnabled()) {
            log.debug("注入 微信登录用户信息转换器 WechatUserConverter.");
        }
        return new WechatUserConverter();
    }

    @PostConstruct
    public void postConstruct() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing OAuth2 Federated Identity Auto Configuration.");
        }
    }

}
