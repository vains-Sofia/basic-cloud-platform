package com.basic.framework.oauth2.core.domain.oidc;

import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import com.basic.framework.core.enums.OAuth2AccountPlatformEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Collection;
import java.util.Map;

/**
 * OIDC 统一用户信息 默认实现
 *
 * @author vains
 */
public class DefaultOidcAuthenticatedUser extends DefaultAuthenticatedUser implements OidcAuthenticatedUser {

    private final OidcIdToken idToken;

    private final OidcUserInfo userInfo;

    public DefaultOidcAuthenticatedUser(String name, OAuth2AccountPlatformEnum accountPlatform, Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken,
                                        OidcUserInfo userInfo) {
        super(name, accountPlatform, authorities);
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.getAttributes();
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

}
