package com.basic.framework.oauth2.federation.wechat;

import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.endpoint.DefaultOAuth2TokenRequestParametersConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.util.MultiValueMap;

import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.WECHAT_PARAMETER_APPID;
import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.WECHAT_PARAMETER_SECRET;

/**
 * 微信登录请求token入参处理类
 *
 * @author vains
 */
public class WechatCodeGrantRequestParametersConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, MultiValueMap<String, String>> {

    private final DefaultOAuth2TokenRequestParametersConverter<OAuth2AuthorizationCodeGrantRequest> parametersConverter = new DefaultOAuth2TokenRequestParametersConverter<>();

    @Override
    public MultiValueMap<String, String> convert(@Nullable OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        if (authorizationCodeGrantRequest == null) {
            return null;
        }
        MultiValueMap<String, String> parameters = parametersConverter.convert(authorizationCodeGrantRequest);
        String registrationId = authorizationCodeGrantRequest.getClientRegistration().getRegistrationId();
        if (OAuth2AccountPlatformEnum.WECHAT.getValue().equals(registrationId)) {
            // 如果当前是微信登录，携带appid和secret
            parameters.add(WECHAT_PARAMETER_APPID, authorizationCodeGrantRequest.getClientRegistration().getClientId());
            parameters.add(WECHAT_PARAMETER_SECRET, authorizationCodeGrantRequest.getClientRegistration().getClientSecret());
        }
        return parameters;
    }

}
