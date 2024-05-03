package com.basic.cloud.oauth2.authorization.server.core;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.PortMapper;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.*;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import java.util.Arrays;
import java.util.Collections;

/**
 * 抽象验证码登录配置类
 *
 * @param <B> HttpSecurityBuilder 子类
 * @param <C> 当前配置类，实现链式调用的子类
 * @param <F> 过滤器
 * @author vains
 */
public abstract class AbstractLoginFilterConfigurer<B extends HttpSecurityBuilder<B>, C extends AbstractLoginFilterConfigurer<B, C, F>, F extends AbstractAuthenticationProcessingFilter>
        extends AbstractHttpConfigurer<AbstractLoginFilterConfigurer<B, C, F>, B> {

    /**
     * 执行登录的过滤器
     */
    @Getter
    @Setter
    private F authFilter;

    /**
     * 根据当前请求生成本次认证信息的详情信息
     */
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    /**
     * 登录成功后会取出在跳转登录页之前访问的请求，并重定向至该请求
     */
    private final SavedRequestAwareAuthenticationSuccessHandler defaultSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    /**
     * 认证成功处理，默认使用{@link SavedRequestAwareAuthenticationSuccessHandler}
     */
    private AuthenticationSuccessHandler successHandler = this.defaultSuccessHandler;

    /**
     * 未登录处理
     */
    @Getter
    @Setter
    private LoginUrlAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 是否使用自定义登录页
     */
    @Getter
    private boolean customLoginPage;

    /**
     * 登录页面地址
     */
    @Getter
    private String loginPage;

    /**
     * 处理登录地址
     */
    @Getter
    @Setter
    private String loginProcessingUrl;

    /**
     * 登录失败处理
     */
    private AuthenticationFailureHandler failureHandler;

    /**
     * 登录失败跳转的url
     */
    @Getter
    @Setter
    private String failureUrl;

    protected AbstractLoginFilterConfigurer() {
        setLoginPage("/login");
    }

    protected AbstractLoginFilterConfigurer(F authenticationFilter, String defaultLoginProcessingUrl) {
        this();
        this.authFilter = authenticationFilter;
        if (defaultLoginProcessingUrl != null) {
            loginProcessingUrl(defaultLoginProcessingUrl);
        }
    }

    /**
     * 设置处理登录的地址
     *
     * @param loginProcessingUrl 处理登录
     * @return 当前配置类，实现链式调用
     */
    public C loginProcessingUrl(String loginProcessingUrl) {
        this.loginProcessingUrl = loginProcessingUrl;
        this.authFilter.setRequiresAuthenticationRequestMatcher(createLoginProcessingUrlMatcher(loginProcessingUrl));
        return getSelf();
    }

    /**
     * 设置保存认证信息的repository
     *
     * @param securityContextRepository 登录后保存认证信息的repository
     * @return 当前配置类，实现链式调用
     */
    public C securityContextRepository(SecurityContextRepository securityContextRepository) {
        this.authFilter.setSecurityContextRepository(securityContextRepository);
        return getSelf();
    }

    /**
     * 创建处理登录的 RequestMatcher 实例，子类需重写该类以完成实例化
     *
     * @param loginProcessingUrl 处理登录地址
     * @return 处理登录的 RequestMatcher 实例
     */
    protected abstract RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl);

    /**
     * 设置生成认证信息web详情的生成方式
     *
     * @param authenticationDetailsSource 认证信息实例
     * @return 当前配置类，实现链式调用
     */
    public C authenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
        return getSelf();
    }

    /**
     * 自定义登录成功处理
     *
     * @param successHandler AuthenticationSuccessHandler及其子类实例
     * @return 当前配置类，实现链式调用
     */
    public C successHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return getSelf();
    }

    /**
     * 设置登录失败跳转地址，并自动设置登录失败处理为{@link SimpleUrlAuthenticationFailureHandler}的实例
     *
     * @param authenticationFailureUrl 登录失败跳转地址
     * @return 当前配置类，实现链式调用
     */
    public C failureUrl(String authenticationFailureUrl) {
        C result = failureHandler(new SimpleUrlAuthenticationFailureHandler(authenticationFailureUrl));
        this.failureUrl = authenticationFailureUrl;
        return result;
    }

    /**
     * 设置登录失败处理器，并自动将 {@link AbstractLoginFilterConfigurer#failureUrl}属性置空
     *
     * @param authenticationFailureHandler 登录失败处理器
     * @return 当前配置类，实现链式调用
     */
    public C failureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.failureUrl = null;
        this.failureHandler = authenticationFailureHandler;
        return getSelf();
    }

    @Override
    public void init(B http) throws Exception {
        // 登录过滤器默认配置
        updateAuthenticationDefaults();
        registerAuthenticationEntryPoint(http, this.authenticationEntryPoint);

        // 获取子类实现提供的provider
        AuthenticationProvider authenticationProvider = authenticationProvider(http);
        // 添加到核心配置中
        http.authenticationProvider(postProcess(authenticationProvider));
    }

    @Override
    public void configure(B http) {
        PortMapper portMapper = http.getSharedObject(PortMapper.class);
        if (portMapper != null) {
            this.authenticationEntryPoint.setPortMapper(portMapper);
        }
        RequestCache requestCache = http.getSharedObject(RequestCache.class);
        if (requestCache != null) {
            this.defaultSuccessHandler.setRequestCache(requestCache);
        }
        this.authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        this.authFilter.setAuthenticationSuccessHandler(this.successHandler);
        this.authFilter.setAuthenticationFailureHandler(this.failureHandler);
        if (this.authenticationDetailsSource != null) {
            this.authFilter.setAuthenticationDetailsSource(this.authenticationDetailsSource);
        }
        SessionAuthenticationStrategy sessionAuthenticationStrategy = http
                .getSharedObject(SessionAuthenticationStrategy.class);
        if (sessionAuthenticationStrategy != null) {
            this.authFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        }
        RememberMeServices rememberMeServices = http.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            this.authFilter.setRememberMeServices(rememberMeServices);
        }
        SecurityContextRepository securityContextRepository = getSecurityContextRepository();
        this.authFilter.setSecurityContextRepository(securityContextRepository);
        this.authFilter.setSecurityContextHolderStrategy(getSecurityContextHolderStrategy());
        F filter = postProcess(this.authFilter);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 自定义验证码登录时需要提供authenticationProvider
     *
     * @param http 配置实例
     * @return AuthenticationProvider
     */
    protected abstract AuthenticationProvider authenticationProvider(B http);

    /**
     * 设置登录地址
     *
     * @param loginPage 登录地址
     * @return 当前配置类，实现链式调用
     */
    protected C loginPage(String loginPage) {
        setLoginPage(loginPage);
        updateAuthenticationDefaults();
        this.customLoginPage = true;
        return getSelf();
    }

    /**
     * 获取当前配置类
     *
     * @return 当前配置类
     */
    @SuppressWarnings("unchecked")
    private C getSelf() {
        return (C) this;
    }

    /**
     * 设置登录失败处理器
     *
     * @param authenticationFailureHandler 登录失败处理器
     * @return 当前配置类，实现链式调用
     */
    public C loginFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.failureHandler = authenticationFailureHandler;
        return getSelf();
    }

    /**
     * 过去当前处理登录的过滤器
     *
     * @return the Authentication Filter
     */
    protected final F getAuthenticationFilter() {
        return this.authFilter;
    }

    /**
     * 设置默认登录处理地址
     */
    protected final void updateAuthenticationDefaults() {
        if (this.loginProcessingUrl == null) {
            loginProcessingUrl(this.loginPage);
        }
        if (this.failureHandler == null) {
            failureUrl(this.loginPage + "?error");
        }
    }

    /**
     * 注册未登录处理
     *
     * @param http                     httpSecurity实例
     * @param authenticationEntryPoint 未登录处理
     */
    protected void registerAuthenticationEntryPoint(B http, AuthenticationEntryPoint authenticationEntryPoint) {
        @SuppressWarnings("unchecked")
        ExceptionHandlingConfigurer<B> exceptionHandling = http.getConfigurer(ExceptionHandlingConfigurer.class);
        if (exceptionHandling == null) {
            return;
        }
        exceptionHandling.defaultAuthenticationEntryPointFor(postProcess(authenticationEntryPoint),
                getAuthenticationEntryPointMatcher(http));
    }

    /**
     * 获取登录失败 RequestMatcher，当不是Xhr请求是跳转页面，否则响应json
     *
     * @param http httpSecurity实例
     * @return 登录失败 RequestMatcher
     */
    protected RequestMatcher getAuthenticationEntryPointMatcher(B http) {
        ContentNegotiationStrategy contentNegotiationStrategy = http.getSharedObject(ContentNegotiationStrategy.class);
        if (contentNegotiationStrategy == null) {
            contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
        }
        MediaTypeRequestMatcher mediaMatcher = new MediaTypeRequestMatcher(contentNegotiationStrategy,
                MediaType.APPLICATION_XHTML_XML, new MediaType("image", "*"), MediaType.TEXT_HTML,
                MediaType.TEXT_PLAIN);
        mediaMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        RequestMatcher notXRequestedWith = new NegatedRequestMatcher(
                new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
        return new AndRequestMatcher(Arrays.asList(notXRequestedWith, mediaMatcher));
    }

    /**
     * 获取登录成功后保存认证信息的repository
     *
     * @return SecurityContextRepository实例
     */
    private SecurityContextRepository getSecurityContextRepository() {
        SecurityContextRepository securityContextRepository = getBuilder()
                .getSharedObject(SecurityContextRepository.class);
        if (securityContextRepository == null) {
            securityContextRepository = new DelegatingSecurityContextRepository(
                    new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
        }
        return securityContextRepository;
    }

    /**
     * 设置登录地址，并自动设置未登录时跳转登录的处理器为 {@link LoginUrlAuthenticationEntryPoint}
     *
     * @param loginPage 登录页面地址
     */
    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
        this.authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginPage);
    }

}