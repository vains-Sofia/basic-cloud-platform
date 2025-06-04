package com.basic.cloud.system.api.autoconfigure;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * system api模块自动配置类
 *
 * @author vains
 */
@Slf4j
@ConditionalOnMissingBean(SystemApiAutoConfiguration.class)
@EnableFeignClients(basePackages = {"com.basic.cloud.system.api"})
public class SystemApiAutoConfiguration {

    @PostConstruct
    public void postConstruct() {
        log.debug("System API FeignClients initialized successfully.");
    }

}
