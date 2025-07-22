package com.basic.framework.oauth2.authorization.server.login.qrcode;

import com.basic.framework.oauth2.authorization.server.core.AbstractLoginFilterConfigurer;
import com.basic.framework.oauth2.authorization.server.util.OAuth2ConfigurerUtils;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.security.QrCodeStatus;
import com.basic.framework.redis.support.RedisOperator;
import lombok.Setter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.ObjectUtils;

/**
 * 邮箱认证登录配置
 *
 * @author vains
 */
@Setter
public class QrCodeLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractLoginFilterConfigurer<H, QrCodeLoginConfigurer<H>, QrCodeLoginAuthenticationFilter> {

    private UserDetailsService userDetailsService;

    private RedisOperator<QrCodeStatus> redisOperator;

    public QrCodeLoginConfigurer() {
        super(new QrCodeLoginAuthenticationFilter(), null);
        qrCodeParameter(AuthorizeConstants.QR_CODE_PARAMETER);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    /**
     * 设置获取二维码唯一标识参数名
     *
     * @param qrCodeParameter 二维码唯一标识参数名
     * @return 返回当前对象，链式调用
     */
    public QrCodeLoginConfigurer<H> qrCodeParameter(String qrCodeParameter) {
        getAuthenticationFilter().setQrCodeParameter(qrCodeParameter);
        return this;
    }

    @Override
    public void init(H http) throws Exception {
        if (ObjectUtils.isEmpty(super.getLoginProcessingUrl())) {
            super.loginProcessingUrl("/login/qr-code");
        } else {
            super.loginProcessingUrl(super.getLoginProcessingUrl());
        }

        if (userDetailsService == null) {
            this.userDetailsService = OAuth2ConfigurerUtils.getBeanOrNull(http, UserDetailsService.class);
        }

        if (redisOperator == null) {
            ResolvableType type = ResolvableType.forClassWithGenerics(RedisOperator.class, QrCodeStatus.class);
            this.redisOperator = OAuth2ConfigurerUtils.getOptionalBean(http, type);
        }

        super.init(http);
    }

    @Override
    protected AuthenticationProvider authenticationProvider(H http) {
        return new QrCodeLoginAuthenticationProvider(userDetailsService, redisOperator);
    }

    /**
     * 设置查询用户信息的service
     *
     * @param userDetailsService 获取用户信息的service
     * @return 当前对象实例
     */
    protected QrCodeLoginConfigurer<H> userDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        return this;
    }

    /**
     * 设置Redis操作对象
     *
     * @param redisOperator Redis操作对象
     * @return 当前对象实例
     */
    public QrCodeLoginConfigurer<H> redisOperator(RedisOperator<QrCodeStatus> redisOperator) {
        this.redisOperator = redisOperator;
        return this;
    }
}
