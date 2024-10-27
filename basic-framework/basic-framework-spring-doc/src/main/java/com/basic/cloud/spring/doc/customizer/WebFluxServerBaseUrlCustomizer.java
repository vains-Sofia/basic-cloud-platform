package com.basic.cloud.spring.doc.customizer;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.http.HttpRequest;
import org.springframework.util.ObjectUtils;

/**
 * 生成Servers的url时默认没有ContextPath处理，添加处理
 *
 * @author vains
 */
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebFluxServerBaseUrlCustomizer implements ServerBaseUrlCustomizer {

    private final WebFluxProperties webFluxProperties;

    @Override
    public String customize(String serverBaseUrl, HttpRequest request) {
        if (webFluxProperties != null) {
            // WebFlux没有ContextPath，有个类似的BasePath
            if (!ObjectUtils.isEmpty(webFluxProperties.getBasePath())) {
                // 在有ContextPath的情况下且后缀不是以ContextPath结尾的，拼接上ContextPath
                if (!serverBaseUrl.endsWith(webFluxProperties.getBasePath())) {
                    return serverBaseUrl + webFluxProperties.getBasePath();
                }
            }
        }
        return serverBaseUrl;
    }

}
