package com.basic.framework.oauth2.core.converter;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.customizer.BasicIdTokenCustomizer;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 从redis中根据jwt获取认证信息的解析器
 *
 * @author vains
 */
public class BasicJwtRedisAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final BasicIdTokenCustomizer idTokenCustomizer;

    private final RedisOperator<AuthenticatedUser> redisOperator;

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public BasicJwtRedisAuthenticationConverter(BasicIdTokenCustomizer idTokenCustomizer, RedisOperator<AuthenticatedUser> redisOperator) {
        this.idTokenCustomizer = idTokenCustomizer;
        this.redisOperator = redisOperator;
        this.jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
    }

    @Override
    public AbstractAuthenticationToken convert(@Nullable Jwt jwt) {
        if (jwt == null) {
            return null;
        }
        Collection<GrantedAuthority> grantedAuthorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);
        // 获取用户的id
        Object userId = jwt.getClaim(AuthorizeConstants.USER_ID_KEY);
        // 从Redis中获取用户信息
        AuthenticatedUser authenticatedUser = redisOperator.get(AuthorizeConstants.USERINFO_PREFIX + userId);
        if (authenticatedUser == null) {
            // 如果用户信息不存在，可能是客户端模式
            Boolean isClientCredentials = jwt.getClaimAsBoolean(AuthorizeConstants.IS_CLIENT_CREDENTIALS);
            // 客户端模式
            if (isClientCredentials != null && isClientCredentials) {
                String principalClaimValue = jwt.getClaimAsString(JwtClaimNames.SUB);
                return new JwtAuthenticationToken(jwt, grantedAuthorities, principalClaimValue);
            }
            // 如果用户信息不存在并且不是客户端模式，可能是用户已登出或被下线

            // Jwt被正常解析但是无法获取到Redis的用户信息，这种情况一般是登出、管理平台下线后出现的问题
            // RFC6750规定字符只能是 %x21 / %x23-5B/ %x5D-7E，以%x20分割(https://datatracker.ietf.org/doc/rfc6750/)
            // %x21 表示 !   %x23-5B 表示 # 到 [, 包括：# $ % & ' ( ) * + , - . / 0-9 : ; < = > ? @ A-Z 和 [
            // %x5D-7E 表示 ] 到 ~，包括：] ^ _ ` a-z { | } 和 ~    %x20 表示空格，用于分隔多个 scope 值。
            BearerTokenError bearerTokenError = BearerTokenErrors.invalidToken("Access token is invalid or has been logged out.");
            throw new OAuth2AuthenticationException(bearerTokenError);
        }

        if (!ObjectUtils.isEmpty(grantedAuthorities)) {
            // 合并scope与用户权限
            Set<String> authorizedScopes = grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            // 动态从redis获取SCOPE对应的权限给用户
            idTokenCustomizer.transferScopesAuthorities(authenticatedUser, authorizedScopes, grantedAuthorities);
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
