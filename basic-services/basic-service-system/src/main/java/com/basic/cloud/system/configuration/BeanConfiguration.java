package com.basic.cloud.system.configuration;

import com.basic.cloud.system.domain.SysPermission;
import com.basic.cloud.system.repository.SysPermissionRepository;
import com.basic.framework.core.domain.PermissionModel;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.redis.support.RedisOperator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * bean注入配置
 *
 * @author vains
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class BeanConfiguration {

    private final SysPermissionRepository permissionRepository;

    private final RedisOperator<Map<String, List<PermissionModel>>> redisOperator;

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
    public void cacheAllPermissions() {
        // 查询需要鉴权的接口
        SpecificationBuilder<SysPermission> specificationBuilder = new SpecificationBuilder<>();
        SpecificationBuilder<SysPermission> builder = specificationBuilder
                // 只查询接口权限
                .eq(SysPermission::getPermissionType, 1)
                .eq(SysPermission::getNeedAuthentication, Boolean.TRUE);
        List<SysPermission> permissions = permissionRepository.findAll(builder);

        // 根据path分组后缓存至redis
        if (!ObjectUtils.isEmpty(permissions)) {
            Map<String, List<PermissionModel>> permissionPathMap = permissions.stream()
                    // 排除path为空的权限
                    .filter(e -> !ObjectUtils.isEmpty(e.getPath()))
                    .map(e -> {
                        PermissionModel authority = new PermissionModel();
                        authority.setPath(e.getPath());
                        authority.setPermission(e.getPermission());
                        authority.setRequestMethod(e.getRequestMethod());
                        authority.setNeedAuthentication(e.getNeedAuthentication());
                        return authority;
                    })
                    .collect(Collectors.groupingBy(PermissionModel::getPath));
            redisOperator.set(AuthorizeConstants.ALL_PERMISSIONS, permissionPathMap);
        }
    }

}
