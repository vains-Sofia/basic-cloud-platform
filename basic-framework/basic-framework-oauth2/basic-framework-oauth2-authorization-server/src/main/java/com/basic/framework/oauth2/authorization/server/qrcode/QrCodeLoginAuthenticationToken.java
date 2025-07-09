package com.basic.framework.oauth2.authorization.server.qrcode;

import com.basic.framework.oauth2.authorization.server.core.AbstractLoginAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 二维码认证登录 token
 *
 * @author vains
 */
public class QrCodeLoginAuthenticationToken extends AbstractLoginAuthenticationToken {

    public QrCodeLoginAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public QrCodeLoginAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    /**
     * This factory method can be safely used by any code that wishes to create a
     * unauthenticated <code>UsernamePasswordAuthenticationToken</code>.
     *
     * @param principal 二维码唯一标识或其他可作为认证主体的信息
     * @return QrCodeLoginAuthenticationToken with a false isAuthenticated() result
     * @since 5.7
     */
    public static QrCodeLoginAuthenticationToken unauthenticated(Object principal) {
        return new QrCodeLoginAuthenticationToken(principal, (null));
    }

    /**
     * This factory method can be safely used by any code that wishes to create a
     * authenticated <code>UsernamePasswordAuthenticationToken</code>.
     *
     * @param principal 邮箱地址
     * @return QrCodeLoginAuthenticationToken with a true isAuthenticated() result
     * @since 5.7
     */
    public static QrCodeLoginAuthenticationToken authenticated(
            Object principal, Collection<? extends GrantedAuthority> authorities) {
        return new QrCodeLoginAuthenticationToken(principal, (null), authorities);
    }

}
