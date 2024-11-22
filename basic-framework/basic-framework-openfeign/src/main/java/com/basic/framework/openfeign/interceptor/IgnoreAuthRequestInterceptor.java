package com.basic.framework.openfeign.interceptor;

import com.basic.framework.core.constants.FeignConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 内部OpenFeign请求拦截器，添加忽略鉴权请求头
 *
 * @author vains
 */
public class IgnoreAuthRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 内部忽略鉴权请求头
        requestTemplate.header(FeignConstants.IGNORE_AUTH_HEADER_KEY, FeignConstants.IGNORE_AUTH_HEADER_VALUE);
    }

}
