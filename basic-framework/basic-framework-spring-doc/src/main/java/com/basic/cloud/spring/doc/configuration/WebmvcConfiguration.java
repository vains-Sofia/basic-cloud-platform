package com.basic.cloud.spring.doc.configuration;

import com.basic.framework.core.enums.BasicEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 添加自定义枚举转换配置
 *
 * @author vains
 */
@RequiredArgsConstructor
public class WebmvcConfiguration implements WebMvcConfigurer {

    private final ConverterFactory<String, ? extends BasicEnum<?, ?>> enumConverterFactory;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(enumConverterFactory);
    }
}
