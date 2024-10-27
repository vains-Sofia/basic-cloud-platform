package com.basic.cloud.spring.doc.customizer;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpRequest;
import org.springframework.util.ObjectUtils;

/**
 * 生成Servers的url时默认没有ContextPath处理，添加处理
 *
 * @author vains
 */
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebMvcServerBaseUrlCustomizer implements ServerBaseUrlCustomizer {

    private final ServerProperties serverProperties;

    @Override
    public String customize(String serverBaseUrl, HttpRequest request) {
        if (serverProperties != null && serverProperties.getServlet() != null) {
            if (!ObjectUtils.isEmpty(serverProperties.getServlet().getContextPath())) {
                // 在有ContextPath的情况下且后缀不是以ContextPath结尾的，拼接上ContextPath
                if (!serverBaseUrl.endsWith(serverProperties.getServlet().getContextPath())) {
                    return serverBaseUrl + serverProperties.getServlet().getContextPath();
                }
            }
        }
        return serverBaseUrl;
    }

}
