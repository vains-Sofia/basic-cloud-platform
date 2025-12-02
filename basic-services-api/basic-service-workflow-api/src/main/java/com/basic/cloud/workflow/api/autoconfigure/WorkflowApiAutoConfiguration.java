package com.basic.cloud.workflow.api.autoconfigure;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Workflow api模块自动配置类
 *
 * @author vains
 */
@Slf4j
@ConditionalOnMissingBean(WorkflowApiAutoConfiguration.class)
@EnableFeignClients(basePackages = {"com.basic.cloud.workflow.api"})
public class WorkflowApiAutoConfiguration {

    @PostConstruct
    public void postConstruct() {
        log.debug("Workflow API FeignClients initialized successfully.");
    }

}
