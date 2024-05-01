package com.basic.cloud.oauth2.authorization.core;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
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
 * @author vains 2023/12/14
 */
public abstract class AbstractLoginFilterConfigurer<B extends HttpSecurityBuilder<B>, C extends AbstractLoginFilterConfigurer<B, C, F>, F extends AbstractAuthenticationProcessingFilter>
        extends AbstractHttpConfigurer<AbstractLoginFilterConfigurer<B, C, F>, B> {

    @Getter
    @Setter
    private F authFilter;

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    private final SavedRequestAwareAuthenticationSuccessHandler defaultSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();

    private AuthenticationSuccessHandler successHandler = this.defaultSuccessHandler;

    @Getter
    @Setter
    private LoginUrlAuthenticationEntryPoint authenticationEntryPoint;

    @Getter
    private boolean customLoginPage;

    @Getter
    private String loginPage;

    @Getter
    @Setter
    private String loginProcessingUrl;

    private AuthenticationFailureHandler failureHandler;

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

    public C loginProcessingUrl(String loginProcessingUrl) {
        this.loginProcessingUrl = loginProcessingUrl;
        this.authFilter.setRequiresAuthenticationRequestMatcher(createLoginProcessingUrlMatcher(loginProcessingUrl));
        return getSelf();
    }

    public C securityContextRepository(SecurityContextRepository securityContextRepository) {
        this.authFilter.setSecurityContextRepository(securityContextRepository);
        return getSelf();
    }

    protected abstract RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl);

    public C authenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
        return getSelf();
    }

    public C successHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return getSelf();
    }

    public C failureUrl(String authenticationFailureUrl) {
        C result = failureHandler(new SimpleUrlAuthenticationFailureHandler(authenticationFailureUrl));
        this.failureUrl = authenticationFailureUrl;
        return result;
    }

    public C failureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.failureUrl = null;
        this.failureHandler = authenticationFailureHandler;
        return getSelf();
    }

    @Override
    public void init(B http) throws Exception {
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

    protected C loginPage(String loginPage) {
        setLoginPage(loginPage);
        updateAuthenticationDefaults();
        this.customLoginPage = true;
        return getSelf();
    }

    protected <T> T getBeanOrNull(Class<T> type) {
        ApplicationContext context = getBuilder().getSharedObject(ApplicationContext.class);
        if (context != null) {
            String[] names = context.getBeanNamesForType(type);
            if (names.length == 1) {
                return context.getBean(type);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private C getSelf() {
        return (C) this;
    }

    public C loginFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.failureHandler = authenticationFailureHandler;
        return getSelf();
    }

    public C loginAuthenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
        return getSelf();
    }

    /**
     * Gets the Authentication Filter
     *
     * @return the Authentication Filter
     */
    protected final F getAuthenticationFilter() {
        return this.authFilter;
    }

    protected final void updateAuthenticationDefaults() {
        if (this.loginProcessingUrl == null) {
            loginProcessingUrl(this.loginPage);
        }
        if (this.failureHandler == null) {
            failureUrl(this.loginPage + "?error");
        }
    }

    protected void registerAuthenticationEntryPoint(B http, AuthenticationEntryPoint authenticationEntryPoint) {
        @SuppressWarnings("unchecked")
        ExceptionHandlingConfigurer<B> exceptionHandling = http.getConfigurer(ExceptionHandlingConfigurer.class);
        if (exceptionHandling == null) {
            return;
        }
        exceptionHandling.defaultAuthenticationEntryPointFor(postProcess(authenticationEntryPoint),
                getAuthenticationEntryPointMatcher(http));
    }

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

    private SecurityContextRepository getSecurityContextRepository() {
        SecurityContextRepository securityContextRepository = getBuilder()
                .getSharedObject(SecurityContextRepository.class);
        if (securityContextRepository == null) {
            securityContextRepository = new DelegatingSecurityContextRepository(
                    new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
        }
        return securityContextRepository;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
        this.authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginPage);
    }

}