package com.basic.example.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Redis分布式锁测试服务
 *
 * @author vains
 */
@SpringBootApplication
public class RedisLockExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisLockExampleApplication.class, args);
    }

}
