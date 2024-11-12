package com.basic.cloud.authorization.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 在线文档使用示例服务
 *
 * @author vains
 */
@SpringBootApplication
@MapperScan("com.basic.cloud.authorization.server.mapper")
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}
