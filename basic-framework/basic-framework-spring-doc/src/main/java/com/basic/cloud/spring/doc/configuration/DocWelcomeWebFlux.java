package com.basic.cloud.spring.doc.configuration;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.webflux.ui.SwaggerWelcomeWebFlux;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class DocWelcomeWebFlux extends SwaggerWelcomeWebFlux {


    /**
     * Instantiates a new Swagger welcome web flux.
     *
     * @param swaggerUiConfig           the swagger ui config
     * @param springDocConfigProperties the spring doc config properties
     * @param swaggerUiConfigParameters the swagger ui config parameters
     * @param springWebProvider         the spring web provider
     */
    public DocWelcomeWebFlux(SwaggerUiConfigProperties swaggerUiConfig, SpringDocConfigProperties springDocConfigProperties, SwaggerUiConfigParameters swaggerUiConfigParameters, SpringWebProvider springWebProvider) {
        super(swaggerUiConfig, springDocConfigProperties, swaggerUiConfigParameters, springWebProvider);
    }

    @Override
    protected String buildUrl(String contextPath, String docsUrl) {
        if (docsUrl.startsWith(contextPath)) {
            // 如果以ContextPath开头，则直接返回，无需再次拼接
            return docsUrl;
        }
        return super.buildUrl(contextPath, docsUrl);
    }
}