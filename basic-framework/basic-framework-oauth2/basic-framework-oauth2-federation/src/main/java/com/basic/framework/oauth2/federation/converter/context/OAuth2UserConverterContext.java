package com.basic.framework.oauth2.federation.converter.context;

import com.basic.framework.oauth2.core.domain.thired.ThirdAuthenticatedUser;
import com.basic.framework.oauth2.federation.converter.OAuth2UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;

import static com.basic.framework.oauth2.core.constant.BasicOAuth2ParameterNames.OAUTH2_ACCOUNT_PLATFORM;

/**
 * 三方oauth2登录获取的用户信息转换处理
 *
 * @author vains
 */
@RequiredArgsConstructor
public class OAuth2UserConverterContext {

    /**
     * 注入所有实例，map的key是实例在ioc中的名字
     * 这里通过构造器注入所有Oauth2UserConverterStrategy的实例，实例名之前在编写时已经通过
     * {@link Component }注解指定过bean的名字，可以根据给定bean名称从map中获取对应的实例(如果存在)
     */
    private final Map<String, OAuth2UserConverter> userConverterMap;

    /**
     * 获取转换器实例
     *
     * @param loginType 三方登录方式
     * @return 转换器实例 {@link OAuth2UserConverter}
     */
    public OAuth2UserConverter getInstance(String loginType) {
        if (ObjectUtils.isEmpty(loginType)) {
            throw new UnsupportedOperationException("登录方式不能为空.");
        }
        OAuth2UserConverter userConverterStrategy = userConverterMap.get(loginType);
        if (userConverterStrategy == null) {
            throw new UnsupportedOperationException("不支持[" + loginType + "]登录方式获取用户信息转换器");
        }
        return userConverterStrategy;
    }

    /**
     * 根据登录方式获取转换器实例，使用转换器获取用户信息
     *
     * @param oAuth2User 三方登录获取到的认证信息
     * @return {@link ThirdAuthenticatedUser}
     */
    public ThirdAuthenticatedUser convert(OAuth2User oAuth2User) {
        return this.getInstance(oAuth2User.getAttribute(OAUTH2_ACCOUNT_PLATFORM))
                .convert(oAuth2User);
    }

}
