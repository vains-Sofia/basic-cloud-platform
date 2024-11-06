package com.basic.framework.oauth2.core.converter;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.DefaultAuthenticatedUser;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 从redis中根据jwt获取认证信息的解析器
 *
 * @author vains
 */
public class BasicJwtRedisAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final RedisOperator<AuthenticatedUser> redisOperator;

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public BasicJwtRedisAuthenticationConverter(RedisOperator<AuthenticatedUser> redisOperator) {
        this.redisOperator = redisOperator;
        this.jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
    }

    @Override
    public AbstractAuthenticationToken convert(@Nullable Jwt jwt) {
        if (jwt == null) {
            return null;
        }
        Collection<GrantedAuthority> grantedAuthorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);
        // 获取jwt的id
        String jti = jwt.getId();
        AuthenticatedUser authenticatedUser = redisOperator.get(AuthorizeConstants.USERINFO_PREFIX + jti);
        if (authenticatedUser == null) {
            // Jwt被正常解析但是无法获取到Redis的用户信息，这种情况一般是登出、管理平台下线后出现的问题
            throw new OAuth2AuthenticationException("Jwt异常，access token已失效或已下线.");
        }
        // 合并scope与用户权限
        if (!ObjectUtils.isEmpty(grantedAuthorities)) {
            Collection<? extends GrantedAuthority> authorities = authenticatedUser.getAuthorities();
            Set<GrantedAuthority> authoritySet = new HashSet<>(authorities);
            authoritySet.addAll(grantedAuthorities);
            authenticatedUser.setAuthorities(authoritySet);
        }

        // 返回BearerTokenAuthentication的实例
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, jwt.getTokenValue(),
                jwt.getIssuedAt(), jwt.getExpiresAt());
        if (authenticatedUser instanceof DefaultAuthenticatedUser user) {
            user.setSub(jwt.getSubject());
        }
        return new BearerTokenAuthentication(authenticatedUser, accessToken, authenticatedUser.getAuthorities());
    }
}
