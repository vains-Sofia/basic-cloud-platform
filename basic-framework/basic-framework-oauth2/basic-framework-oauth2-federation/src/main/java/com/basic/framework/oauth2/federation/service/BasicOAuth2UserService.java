package com.basic.framework.oauth2.federation.service;

import com.basic.cloud.system.api.SysThirdUserBindClient;
import com.basic.cloud.system.api.domain.request.EnhancedThirdUserRequest;
import com.basic.cloud.system.api.domain.response.EnhancedUserResponse;
import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.core.domain.thired.ThirdAuthenticatedUser;
import com.basic.framework.oauth2.federation.converter.context.OAuth2UserConverterContext;
import com.basic.framework.oauth2.federation.wechat.WechatUserRequestEntityConverter;
import com.basic.framework.oauth2.federation.wechat.WechatUserResponseConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.WebAttributes;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.OAUTH2_ACCESS_TOKEN;
import static com.basic.framework.oauth2.core.core.BasicOAuth2ParameterNames.OAUTH2_ACCOUNT_PLATFORM;

/**
 * 自定义三方oauth2登录获取用户信息服务
 *
 * @author vains
 */
@Slf4j
public class BasicOAuth2UserService extends DefaultOAuth2UserService {

    private final SysThirdUserBindClient thirdUserBindClient;

    private final OAuth2UserConverterContext userConverterContext;

    public BasicOAuth2UserService(OAuth2UserConverterContext userConverterContext, SysThirdUserBindClient thirdUserBindClient) {
        this.userConverterContext = userConverterContext;
        // 初始化时添加微信用户信息请求处理，oidcUserService本质上是调用该类获取用户信息的，不用添加
        super.setRequestEntityConverter(new WechatUserRequestEntityConverter());
        // 设置用户信息转换器
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        List<HttpMessageConverter<?>> messageConverters = List.of(
                new StringHttpMessageConverter(),
                // 获取微信用户信息时使其支持“text/plain”
                new WechatUserResponseConverter(),
                new ResourceHttpMessageConverter(),
                new ByteArrayHttpMessageConverter(),
                new AllEncompassingFormHttpMessageConverter()
        );
        restTemplate.setMessageConverters(messageConverters);
        super.setRestOperations(restTemplate);
        this.thirdUserBindClient = thirdUserBindClient;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);
            // 获取三方登录配置的registrationId，这里将他当做登录方式
            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            // 自定义用户属性，传递oauth2认证相关数据
            Map<String, Object> attributes = oAuth2User.getAttributes();
            LinkedHashMap<String, Object> attributeMap = new LinkedHashMap<>(attributes);
            // 设置三方登录提供商类型
            attributeMap.put(OAUTH2_ACCOUNT_PLATFORM, registrationId);
            // 设置三方access token信息
            attributeMap.put(OAUTH2_ACCESS_TOKEN, userRequest.getAccessToken());
            // 获取三方账号字段
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                    .getUserNameAttributeName();
            OAuth2User fullOAuth2User = new DefaultOAuth2User(oAuth2User.getAuthorities(), attributeMap, userNameAttributeName);

            // 转为项目中的统一用户信息
            ThirdAuthenticatedUser authenticatedUser = userConverterContext.convert(fullOAuth2User);
            try {
                // 查询增强的三方用户信息
                EnhancedThirdUserRequest enhancedRequest = new EnhancedThirdUserRequest();
                enhancedRequest.setProvider(authenticatedUser.getAccountPlatform());
                enhancedRequest.setProviderUserId(authenticatedUser.getId() == null ? authenticatedUser.getSub() : authenticatedUser.getId() + "");
                Result<EnhancedUserResponse> enhancedThirdUserResponse = thirdUserBindClient.enhancedThirdUser(enhancedRequest);
                if (enhancedThirdUserResponse == null || enhancedThirdUserResponse.getData() == null) {
                    if (log.isWarnEnabled()) {
                        log.warn("No enhanced third-party user information found for provider: {}, userId: {}",
                                authenticatedUser.getAccountPlatform(), authenticatedUser.getId());
                    }
                    authenticatedUser.setBindBasicUserChecked(false);
                } else {
                    // 将增强的三方用户信息转换为AuthenticatedUserResponse
                    EnhancedUserResponse enhancedUser = enhancedThirdUserResponse.getData();
                    // 将增强的三方用户信息设置到authenticatedUser中
                    BeanUtils.copyProperties(enhancedUser, authenticatedUser);

                    // 如果原始的authenticatedUser没有权限信息，则直接设置增强的三方用户权限信息
                    if (ObjectUtils.isEmpty(authenticatedUser.getAuthorities())) {
                        authenticatedUser.setAuthorities(authenticatedUser.getAuthorities());
                    } else {
                        // 如果原始的authenticatedUser有权限信息，则将增强的三方用户权限信息合并到原有权限信息中
                        Set<GrantedAuthority> authorities = new HashSet<>(authenticatedUser.getAuthorities());
                        if (!ObjectUtils.isEmpty(enhancedUser.getAuthorities())) {
                            authorities.addAll(enhancedUser.getAuthorities());
                        }
                        authenticatedUser.setAuthorities(authorities);
                    }
                    // 经过检查已经确认绑定基本用户信息
                    authenticatedUser.setBindBasicUserChecked(true);
                }
                return authenticatedUser;
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Failed to get enhanced third-party user information for provider: {}, userId: {}",
                            authenticatedUser.getAccountPlatform(), authenticatedUser.getId(), e);
                }
                // 如果查询增强的三方用户信息失败，则直接返回原始的三方用户信息
                return authenticatedUser;
            }
        } catch (Exception e) {
            // 获取当前request
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                throw new OAuth2AuthenticationException("Failed to get the current request.");
            }
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            // 将异常放入session中
            request.getSession(Boolean.FALSE).setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, e);
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}
