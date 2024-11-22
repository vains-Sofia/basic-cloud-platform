package com.basic.framework.openfeign.configuration;

import feign.MethodMetadata;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static feign.Util.emptyToNull;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

/**
 * 自定义feign的contract，解决feign调用时不允许使用@RequestMapping的问题。
 *
 * @author vains
 */
public class BasicSpringMvcContract extends SpringMvcContract {

    private final boolean decodeSlash;

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    public BasicSpringMvcContract(List<AnnotatedParameterProcessor> annotatedParameterProcessors,
                                  ConversionService conversionService,
                                  boolean decodeSlash) {
        super(annotatedParameterProcessors, conversionService, decodeSlash);
        this.decodeSlash = decodeSlash;
    }

    @Override
    protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
        if (clz.getInterfaces().length == 0) {
            RequestMapping classAnnotation = findMergedAnnotation(clz, RequestMapping.class);
            if (classAnnotation != null) {
                // Prepend path from class annotation if specified
                if (classAnnotation.value().length > 0) {
                    String pathValue = emptyToNull(classAnnotation.value()[0]);
                    pathValue = resolve(pathValue);
                    if (!pathValue.startsWith("/")) {
                        pathValue = "/" + pathValue;
                    }
                    data.template().uri(pathValue);
                    if (data.template().decodeSlash() != decodeSlash) {
                        data.template().decodeSlash(decodeSlash);
                    }
                }
            }
        }
    }

    private String resolve(String value) {
        if (StringUtils.hasText(value) && resourceLoader instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) resourceLoader).getEnvironment().resolvePlaceholders(value);
        }
        return value;
    }
}