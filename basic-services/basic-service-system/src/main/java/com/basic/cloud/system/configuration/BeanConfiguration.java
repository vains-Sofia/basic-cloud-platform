package com.basic.cloud.system.configuration;

import com.basic.cloud.system.service.SysPermissionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * bean注入配置
 *
 * @author vains
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class BeanConfiguration {

    private final SysPermissionService permissionService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 缓存所有权限信息
     * system模块启动时初始化所有权限信息至redis，
     * 资源模块在请求时从redis获取所有权限判断当前请求是否需要鉴权，
     * 只针对在system模块中管理并且需要鉴权的请求
     */
    @PostConstruct
    public void initPermissionsCache() {
        // 刷新权限缓存
        permissionService.refreshPermissionCache();
    }

}
