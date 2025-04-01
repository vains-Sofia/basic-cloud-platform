package com.basic.cloud.authorization.server.controller;

import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.oauth2.storage.domain.model.ScopeWithDescription;
import com.basic.framework.oauth2.storage.service.OAuth2ScopeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

    private final RegisteredClientRepository registeredClientRepository;

    private final OAuth2AuthorizationConsentService authorizationConsentService;

    private static final String DEFAULT_DESCRIPTION = "UNKNOWN SCOPE - We cannot provide information about this permission, use caution when granting this.";

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

    @GetMapping(value = "/oauth2/consent")
    @Operation(summary = "授权确认页面", description = "渲染授权确认页面")
    public String consent(Principal principal, Model model,
                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                          @RequestParam(OAuth2ParameterNames.STATE) String state,
                          @RequestParam(name = OAuth2ParameterNames.USER_CODE, required = false) String userCode) {

        // Remove scopes that were already approved
        Set<String> scopesToApprove = new HashSet<>();
        Set<String> previouslyApprovedScopes = new HashSet<>();
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new CloudServiceException("客户端不存在");
        }

        OAuth2AuthorizationConsent currentAuthorizationConsent =
                this.authorizationConsentService.findById(registeredClient.getId(), principal.getName());
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

        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        model.addAttribute("scopes", withDescription(scopesToApprove));
        model.addAttribute("previouslyApprovedScopes", withDescription(previouslyApprovedScopes));
        model.addAttribute("principalName", principal.getName());
        model.addAttribute("userCode", userCode);
        ServerProperties.Servlet servlet = serverProperties.getServlet();
        model.addAttribute("contextPath", servlet.getContextPath());
        if (StringUtils.hasText(userCode)) {
            model.addAttribute("requestURI", "/oauth2/device_verification");
        } else {
            model.addAttribute("requestURI", "/oauth2/authorize");
        }

        return "consent";
    }

    /**
     * 查询scope描述并处理可能不存在于数据库中的scope，添加一个默认描述
     *
     * @param scopes scope
     * @return 参数对应的scope列表，带描述
     */
    private List<ScopeWithDescription> withDescription(Set<String> scopes) {
        // 数据库中的scope
        List<ScopeWithDescription> toApproveScope = scopeService.findByScopes(scopes);
        List<String> list = toApproveScope.stream().map(ScopeWithDescription::scope).toList();

        // 不存在于数据库中的scope
        List<ScopeWithDescription> unknownScopes = scopes
                .stream()
                .filter(e -> !list.contains(e))
                .map(e -> new ScopeWithDescription(e, DEFAULT_DESCRIPTION))
                .toList();

        if (!ObjectUtils.isEmpty(unknownScopes)) {
            // 合并
            toApproveScope.addAll(unknownScopes);
        }

        return toApproveScope;
    }

}
