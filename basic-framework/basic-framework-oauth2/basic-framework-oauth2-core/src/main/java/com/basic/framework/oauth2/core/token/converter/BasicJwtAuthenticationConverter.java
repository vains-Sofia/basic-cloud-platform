package com.basic.framework.oauth2.core.token.converter;

import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * jwt token转换器
 *
 * @author vains
 */
public class BasicJwtAuthenticationConverter extends JwtAuthenticationConverter {

    public BasicJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 设置解析权限信息的前缀，设置为空是去掉前缀
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        // 设置权限信息在jwt claims中的key
        grantedAuthoritiesConverter.setAuthoritiesClaimName(AuthorizeConstants.AUTHORITIES);

        super.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    }
}
