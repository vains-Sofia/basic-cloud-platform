package com.basic.framework.oauth2.authorization.server.login.qrcode;

import com.basic.framework.oauth2.authorization.server.core.AbstractLoginAuthenticationProvider;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.security.QrCodeStatus;
import com.basic.framework.oauth2.core.enums.QrCodeStatusEnum;
import com.basic.framework.redis.support.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ott.InvalidOneTimeTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.ObjectUtils;

/**
 * 二维码认证登录 provider
 *
 * @author vains
 */
@Slf4j
public class QrCodeLoginAuthenticationProvider extends AbstractLoginAuthenticationProvider {

    private final RedisOperator<QrCodeStatus> redisOperator;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public QrCodeLoginAuthenticationProvider(UserDetailsService userDetailsService, RedisOperator<QrCodeStatus> redisOperator) {
        super(userDetailsService);
        this.redisOperator = redisOperator;
    }

    @Override
    protected void additionalAuthenticationChecks(AbstractAuthenticationToken authentication) throws AuthenticationException {
        log.debug("校验二维码状态...");

        // 验证二维码状态
        QrCodeStatus statusCache = redisOperator.get(AuthorizeConstants.QR_STATUS_CACHE + authentication.getPrincipal());
        if (statusCache == null) {
            log.debug("二维码状态已失效，请重新生成二维码.");
            throw new InvalidOneTimeTokenException("二维码状态已失效，请重新生成二维码.");
        }

        if (statusCache.getStatus() == null || statusCache.getStatus() != QrCodeStatusEnum.CONFIRMED) {
            log.debug("二维码未被扫描，请扫描二维码进行登录.");
            throw new InvalidOneTimeTokenException("二维码未被扫描，请扫描二维码进行登录.");
        }

        if (ObjectUtils.isEmpty(statusCache.getEmail())) {
            log.debug("二维码状态校验失败，未获取到用户信息.");
            throw new InvalidOneTimeTokenException("二维码状态校验失败，未获取到用户信息.");
        }
        // 更新二维码状态缓存，确保下边能获取到
        redisOperator.set(AuthorizeConstants.QR_STATUS_CACHE + authentication.getPrincipal(), statusCache, AuthorizeConstants.EXPIRE_SECONDS);
        log.debug("二维码状态校验成功.");
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
        QrCodeLoginAuthenticationToken result = QrCodeLoginAuthenticationToken.authenticated(
                principal, this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        log.debug("Authenticated user");
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return QrCodeLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected UserDetails retrieveUser(String principal, AbstractAuthenticationToken authentication) throws AuthenticationException {
        QrCodeStatus statusCache = redisOperator.getAndDelete(AuthorizeConstants.QR_STATUS_CACHE + authentication.getPrincipal());
        // 通过缓存的邮箱获取用户信息
        return super.retrieveUser(statusCache.getEmail(), authentication);
    }
}
