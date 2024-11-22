package com.basic.framework.core.constants;

/**
 * Feign常量
 *
 * @author vains
 */
public class FeignConstants {

    /**
     * 忽略认证请求头-key
     */
    public static final String IGNORE_AUTH_HEADER_KEY = "X-Ignore-Auth-Header";

    /**
     * 忽略认证请求头-value
     */
    public static final String IGNORE_AUTH_HEADER_VALUE = "X-Ignore-Auth-Header-Value";

    /**
     * system模块的application name
     */
    public static final String SYSTEM_APPLICATION = "system";

    /**
     * system模块的ContextPath
     */
    public static final String SYSTEM_CONTEXT_PATH = "${basic.cloud.api.system.path:/system}";

}
