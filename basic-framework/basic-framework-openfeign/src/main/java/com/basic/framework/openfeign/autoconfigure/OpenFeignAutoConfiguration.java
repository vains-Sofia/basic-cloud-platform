package com.basic.framework.openfeign.autoconfigure;

import com.basic.framework.openfeign.configuration.BasicSpringMvcContract;
import com.basic.framework.openfeign.decoder.BasicErrorDecoder;
import com.basic.framework.openfeign.interceptor.IgnoreAuthRequestInterceptor;
import feign.Contract;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.util.List;

/**
 * OpenFeign自动配置类
 * 这里使用 <span>@EnableFeignClients(basePackages = {"com.basic.cloud.*.api"})}</span>
 * 也可以成功注入FeignClient，但是idea会提示找不到bean，虽然不影响启动，但是看着报错总会觉得是配置错误，
 * 所以放弃该方式
 *
 * @author vains
 */
@RequiredArgsConstructor
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class OpenFeignAutoConfiguration {

    private final FeignClientProperties feignClientProperties;

    private final List<AnnotatedParameterProcessor> parameterProcessors;

    private final List<FeignFormatterRegistrar> feignFormatterRegistrars;

    /**
     * OpenFeign忽略认证拦截器
     *
     * @return IgnoreAuthRequestInterceptor
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new IgnoreAuthRequestInterceptor();
    }

    /**
     * 基础异常解析器
     *
     * @return BasicErrorDecoder
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new BasicErrorDecoder();
    }

    /**
     * 解决@RequestMapping annotation not allowed on @FeignClient interfaces
     *
     * @return BasicSpringMvcContract
     */
    @Bean
    public Contract feignContract(ConversionService feignConversionService) {
        return new BasicSpringMvcContract(parameterProcessors, feignConversionService, feignClientProperties);
    }

    /**
     * OpenFeign转换器实现
     *
     * @return FormattingConversionService
     */
    @Bean
    public FormattingConversionService feignConversionService() {
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        for (FeignFormatterRegistrar feignFormatterRegistrar : feignFormatterRegistrars) {
            feignFormatterRegistrar.registerFormatters(conversionService);
        }
        return conversionService;
    }

}
