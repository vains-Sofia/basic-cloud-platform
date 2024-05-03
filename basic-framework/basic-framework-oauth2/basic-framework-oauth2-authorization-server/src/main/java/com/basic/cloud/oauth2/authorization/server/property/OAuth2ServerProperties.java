package com.basic.cloud.oauth2.authorization.server.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 认证服务配置
 *
 * @author vains
 */
@Data
@ConfigurationProperties(prefix = OAuth2ServerProperties.PREFIX)
public class OAuth2ServerProperties {

    /**
     * 配置在yaml中的前缀
     */
    static final String PREFIX = "basic.cloud.oauth2";

    /**
     * 登录相关配置
     */
    private ServerProperties server = new ServerProperties();

    /**
     * 登录相关配置类
     *
     * @author vains
     */
    @Data
    public static class ServerProperties {

        /**
         * 设置允许跨域的域名,如果允许携带cookie的话,路径就不能写*号, *表示所有的域名都可以跨域访问
         */
        private Set<String> allowedOrigins = new HashSet<>();

        /**
         * 登录页面地址
         */
        private String loginPageUri = "/login";

        /**
         * 授权确认地址
         */
        private String consentPageUri = "/oauth2/consent";

        /**
         * 不需要鉴权的地址
         */
        private Set<String> ignoreUriPaths = new HashSet<>();

        /**
         * 设备码验证页面地址
         */
        private String deviceVerificationUri = "/activate";

        /**
         * 设备码验证成功地址
         */
        private String deviceActivatedPageUri = "/activated";

    }
}
