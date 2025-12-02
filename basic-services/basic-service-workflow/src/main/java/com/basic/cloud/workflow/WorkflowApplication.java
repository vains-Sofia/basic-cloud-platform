package com.basic.cloud.workflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 流程引擎启动类
 *
 * @author vains
 */
@MapperScan("com.basic.cloud.workflow.mapper")
@SpringBootApplication(proxyBeanMethods = false)
public class WorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }

}
