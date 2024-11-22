package com.basic.cloud.system.api.autoconfigure;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * system api模块自动配置类
 *
 * @author vains
 */
@Slf4j
@EnableFeignClients(basePackages = {"com.basic.cloud.system.api"})
public class SystemApiAutoConfiguration {

    @PostConstruct
    public void postConstruct() {
        log.debug("Initializing the System API.");
    }

}
