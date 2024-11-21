package com.basic.framework.openfeign.autoconfigure;

import com.basic.framework.openfeign.decoder.BasicErrorDecoder;
import com.basic.framework.openfeign.interceptor.IgnoreAuthRequestInterceptor;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * OpenFeign自动配置类
 *
 * @author vains
 */
@EnableFeignClients
public class OpenFeignAutoConfiguration {

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

}
