package com.basic.framework.oauth2.core.property;

import com.basic.framework.oauth2.core.enums.CoreServiceStorageEnum;
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
@ConfigurationProperties(prefix = AuthorizationServerProperties.PREFIX)
public class AuthorizationServerProperties {

    /**
     * 配置在yaml中的前缀
     */
    static final String PREFIX = "basic.cloud.oauth2.server";

    /**
     * 设置允许跨域的域名,如果允许携带cookie的话,路径就不能写*号, *表示所有的域名都可以跨域访问
     */
    private Set<String> allowedOrigins = new HashSet<>();

    /**
     * 授权确认地址
     */
    private String consentPageUri = "/oauth2/consent";

    /**
     * 授权错误地址
     */
    private String authorizeErrorUri;

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

    /**
     * 核心服务实现类型，默认MybatisPlus的实现
     */
    private CoreServiceStorageEnum coreServiceStorage = CoreServiceStorageEnum.JPA;

}
