package com.basic.cloud.resource.server.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 资源服务配置
 *
 * @author vains
 */
@Data
@ConfigurationProperties(prefix = ResourceServerProperties.PREFIX)
public class ResourceServerProperties {

    /**
     * 配置在yaml中的前缀
     */
    static final String PREFIX = "basic.cloud.oauth2.resource.server";

    /**
     * 不需要鉴权的地址
     */
    private Set<String> ignoreUriPaths = new HashSet<>();

}
