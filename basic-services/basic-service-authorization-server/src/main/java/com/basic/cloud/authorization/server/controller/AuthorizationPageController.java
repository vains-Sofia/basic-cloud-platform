package com.basic.cloud.authorization.server.controller;

import com.basic.cloud.authorization.server.domain.response.OAuth2ConsentResponse;
import com.basic.framework.core.domain.Result;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.property.OAuth2ServerProperties;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import com.basic.framework.oauth2.storage.domain.model.ScopeWithDescription;
import com.basic.framework.oauth2.storage.domain.security.BasicApplication;
import com.basic.framework.oauth2.storage.service.BasicApplicationService;
import com.basic.framework.oauth2.storage.service.OAuth2ScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 认证服务页面渲染接口
 *
 * @author vains
 */
@Controller
@RequiredArgsConstructor
@Tag(name = "认证服务页面渲染", description = "渲染认证服务需要的页面")
public class AuthorizationPageController {

    private final OAuth2ScopeService scopeService;

    private final ServerProperties serverProperties;

    private final BasicApplicationService applicationService;

    private final OAuth2ServerProperties oauth2ServerProperties;

    private final OAuth2AuthorizationConsentService authorizationConsentService;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @GetMapping("/login")
    @Operation(summary = "登录页面", description = "渲染登录页面")
    public String login(Model model, HttpSession session) {
        Object attribute = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (attribute instanceof AuthenticationException exception) {
            model.addAttribute("error", exception.getMessage());
        }
        return "login-new";
    }

    @GetMapping("/activate")
    @Parameter(name = "user_code", description = "发起设备码授权申请时生成的用户码，当传入该参数时直接提交认证")
    @Operation(summary = "设备码-设备码验证页面", description = "渲染设备码验证页面，当传入'user_code'参数时直接提交认证")
    public String activate(@RequestParam(value = "user_code", required = false) String userCode) {
        if (userCode != null) {
            return "redirect:/oauth2/device_verification?user_code=" + userCode;
        }
        return "device-activate";
    }

    @GetMapping("/activated")
    @Operation(summary = "设备码-设备码验证成功页面", description = "渲染设备码验证成功页面")
    public String activated() {
        return "device-activated";
    }

    @GetMapping(value = "/", params = "success")
    @Operation(summary = "设备码-设备码验证成功页面", description = "渲染设备码验证成功页面")
    public String success() {
        return "device-activated";
    }

    @ResponseBody
    @GetMapping(value = "/oauth2/consent/parameters")
    @Schema(name = "获取授权确认页面所需的参数", description = "授权确认页面所需的参数")
    public Result<OAuth2ConsentResponse> consentParameters(@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                                                           @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                                                           @RequestParam(OAuth2ParameterNames.STATE) String state,
                                                           @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode) {

        // 获取consent页面所需的参数
        OAuth2ConsentResponse consentParameters = this.getConsentParameters(scope, state, clientId, userCode);

        return Result.success(consentParameters);
    }

    /**
     * 根据授权确认相关参数获取授权确认与未确认的scope相关参数
     *
     * @param scope    scope权限
     * @param state    state
     * @param clientId 客户端id
     * @param userCode 设备码授权流程中的用户码
     * @return 页面所需数据
     */
    private OAuth2ConsentResponse getConsentParameters(String scope, String state, String clientId, String userCode) {
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        if (authenticatedUser == null) {
            throw new CloudServiceException("用户未登录");
        }
        OAuth2ConsentResponse consentParameters = new OAuth2ConsentResponse();
        Set<String> scopesToApprove = new HashSet<>();
        Set<String> previouslyApprovedScopes = new HashSet<>();
        BasicApplication application = this.applicationService.findByClientId(clientId);
        if (application == null) {
            throw new CloudServiceException("客户端不存在");
        }

        OAuth2AuthorizationConsent currentAuthorizationConsent =
                this.authorizationConsentService.findById(String.valueOf(application.getId()), authenticatedUser.getId() + "");
        Set<String> authorizedScopes;
        if (currentAuthorizationConsent != null) {
            authorizedScopes = currentAuthorizationConsent.getScopes();
        } else {
            authorizedScopes = Collections.emptySet();
        }
        for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
            if (OidcScopes.OPENID.equals(requestedScope)) {
                continue;
            }
            if (authorizedScopes.contains(requestedScope)) {
                previouslyApprovedScopes.add(requestedScope);
            } else {
                scopesToApprove.add(requestedScope);
            }
        }

        consentParameters.setClientId(clientId);
        consentParameters.setClientName(application.getClientName());
        consentParameters.setClientLogo(application.getClientLogo());
        consentParameters.setState(state);
        consentParameters.setScopes(withDescription(scopesToApprove));
        consentParameters.setPreviouslyApprovedScopes(withDescription(previouslyApprovedScopes));
        consentParameters.setPrincipalName(authenticatedUser.getNickname());
        consentParameters.setUserCode(userCode);
        ServerProperties.Servlet servlet = serverProperties.getServlet();
        consentParameters.setContextPath(servlet.getContextPath());
        if (StringUtils.hasText(userCode)) {
            consentParameters.setRequestURI("/oauth2/device_verification");
        } else {
            consentParameters.setRequestURI("/oauth2/authorize");
        }
        return consentParameters;
    }

    @GetMapping(value = "/oauth2/consent")
    @Operation(summary = "授权确认页面", description = "渲染授权确认页面")
    public String consent(Model model,
                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                          @RequestParam(OAuth2ParameterNames.STATE) String state,
                          @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode) {

        // 获取consent页面所需的参数
        OAuth2ConsentResponse consentParameters = this.getConsentParameters(scope, state, clientId, userCode);

        model.addAttribute("parameters", consentParameters);

        return "consent";
    }

    @ResponseBody
    @GetMapping(value = "/check/login")
    @Operation(summary = "检查是否登录过", description = "检查是否登录过")
    public Result<String> checkLogin() {

        return Result.success();
    }

    @SneakyThrows
    @ResponseBody
    @GetMapping(value = "/oauth2/consent/redirect")
    public Result<String> consentRedirect(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                                          @RequestParam(OAuth2ParameterNames.STATE) String state,
                                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                                          @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode) {

        // 携带当前请求参数重定向至前端页面
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(oauth2ServerProperties.getConsentPageUri())
                .queryParam(OAuth2ParameterNames.SCOPE, UriUtils.encode(scope, StandardCharsets.UTF_8))
                .queryParam(OAuth2ParameterNames.STATE, UriUtils.encode(state, StandardCharsets.UTF_8))
                .queryParam(OAuth2ParameterNames.CLIENT_ID, clientId)
                .queryParam(OAuth2ParameterNames.USER_CODE, userCode);

        String uriString = uriBuilder.build(Boolean.TRUE).toUriString();
        if (ObjectUtils.isEmpty(userCode) || !UrlUtils.isAbsoluteUrl(oauth2ServerProperties.getDeviceVerificationUri())) {
            // 不是设备码模式或者设备码验证页面不是前后端分离的，无需返回json，直接重定向
            this.redirectStrategy.sendRedirect(request, response, uriString);
            return null;
        }
        // 兼容设备码，需响应JSON，由前端进行跳转
        return Result.success(uriString);
    }

    /**
     * 查询scope描述并处理可能不存在于数据库中的scope，添加一个默认描述
     *
     * @param scopes scope
     * @return 参数对应的scope列表，带描述
     */
    private Set<ScopeWithDescription> withDescription(Set<String> scopes) {
        // 数据库中的scope
        return scopeService.findByScopes(scopes);
    }

}
