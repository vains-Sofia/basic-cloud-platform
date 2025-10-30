package com.basic.cloud.authorization.server.configuration;

import com.basic.framework.oauth2.authorization.server.login.email.EmailCaptchaLoginConfigurer;
import com.basic.framework.oauth2.authorization.server.login.qrcode.QrCodeLoginConfigurer;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.security.QrCodeStatus;
import com.basic.framework.oauth2.core.domain.security.ScopePermissionModel;
import com.basic.framework.oauth2.core.handler.LoginFailureHandler;
import com.basic.framework.oauth2.core.handler.LoginSuccessHandler;
import com.basic.framework.oauth2.core.property.BasicLoginProperties;
import com.basic.framework.oauth2.core.token.generator.StandardOAuth2TokenGenerator;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import com.basic.framework.oauth2.storage.repository.OAuth2ScopePermissionRepository;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源服务器配置类
 *
 * @author Vains
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ResourceServerConfiguration {

    private final BasicLoginProperties basicLoginProperties;

    private final RedisOperator<QrCodeStatus> qrCodeRedisOperator;

    private final RedisOperator<List<ScopePermissionModel>> redisOperator;

    private final OAuth2ScopePermissionRepository scopePermissionRepository;

    private final StandardOAuth2TokenGenerator standardOAuth2TokenGenerator;

    private final OAuth2AuthorizationRequestResolver authorizationRequestResolver;

    private final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

    private final AuthorizationManager<RequestAuthorizationContext> requestContextAuthorizationManager;

    private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;

    /**
     * 认证与鉴权相关的过滤器链配置
     *
     * @param http httpSecurity 实例
     * @return 认证鉴权相关的过滤器链
     * @throws Exception 配置错误时抛出
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {

        // 禁用 csrf 与 cors
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        // 设置忽略鉴权路径与自定义鉴权配置
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AuthorizeConstants.DEFAULT_IGNORE_PATHS.toArray(new String[]{})).permitAll()
                .anyRequest().access(requestContextAuthorizationManager)
        );

        // Form login handles the redirect to the login page from the
        // authorization server filter chain
        http.formLogin(form -> form
                .loginPage(basicLoginProperties.getLoginPageUri())
                .loginProcessingUrl(basicLoginProperties.getLoginProcessingUri())
                .successHandler(new LoginSuccessHandler(basicLoginProperties.getLoginPageUri(), standardOAuth2TokenGenerator))
                .failureHandler(new LoginFailureHandler(basicLoginProperties.getLoginPageUri()))
        );

        // 开启oauth2登录支持
        http.oauth2Login(oauth2Login -> oauth2Login
                .loginPage(basicLoginProperties.getLoginPageUri())
                // 针对微信的特殊处理，自动替换为微信需要的授权申请参数
                .authorizationEndpoint(authorization -> authorization
                        .authorizationRequestResolver(authorizationRequestResolver)
                )
                // 针对微信的特殊处理，添加“text/plain”格式的响应支持
                .tokenEndpoint(token -> token
                        .accessTokenResponseClient(accessTokenResponseClient)
                )
        );

        // 添加BearerTokenAuthenticationFilter，将认证服务当做一个资源服务，解析请求头中的token
        http.oauth2ResourceServer((resourceServer) -> resourceServer
                .accessDeniedHandler(SecurityUtils::exceptionHandler)
                .authenticationEntryPoint(SecurityUtils::exceptionHandler)
                .authenticationManagerResolver(authenticationManagerResolver)
        );

        // 添加邮件登录过滤器
        http.with(new EmailCaptchaLoginConfigurer<>(), configurer -> configurer
                .loginPage(basicLoginProperties.getLoginPageUri())
                .loginProcessingUrl(basicLoginProperties.getEmailLoginProcessingUri())
                .successHandler(new LoginSuccessHandler(basicLoginProperties.getLoginPageUri(), standardOAuth2TokenGenerator))
                .failureHandler(new LoginFailureHandler(basicLoginProperties.getLoginPageUri()))
        );

        // 添加二维码登录过滤器
        http.with(new QrCodeLoginConfigurer<>(), configurer -> configurer
                .redisOperator(qrCodeRedisOperator)
                .loginPage(basicLoginProperties.getLoginPageUri())
                .loginProcessingUrl(basicLoginProperties.getQrCodeLoginProcessingUri())
                .successHandler(new LoginSuccessHandler(basicLoginProperties.getLoginPageUri(), standardOAuth2TokenGenerator))
                .failureHandler(new LoginFailureHandler(basicLoginProperties.getLoginPageUri()))
        );

        return http.build();
    }

    /**
     * 初始化scope的权限至缓存中
     */
    @PostConstruct
    public void initScopePermissionCache() {
        // 查询所有数据并转换
        List<ScopePermissionModel> permissionModelList = this.scopePermissionRepository.findAll()
                .stream()
                .map(e -> {
                    ScopePermissionModel model = new ScopePermissionModel();
                    BeanUtils.copyProperties(e, model);
                    return model;
                }).toList();

        // 删除缓存
        redisOperator.delete(AuthorizeConstants.SCOPE_PERMISSION_KEY);
        // 刷新缓存
        redisOperator.set(AuthorizeConstants.SCOPE_PERMISSION_KEY, new ArrayList<>(permissionModelList));
    }

}
