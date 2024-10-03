package com.basic.example.redis.controller;

import com.basic.framework.core.domain.Result;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.redis.annotation.RedisLock;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁测试接口
 *
 * @author YuJx
 */
@Slf4j
@RestController
@RequestMapping("/redis/lock")
public class RedisLockController {

    private int publicResource = 10;

    @RedisLock
    @SneakyThrows
    @GetMapping("/test01")
    public Result<Integer> test01() {
        if (publicResource <= 0) {
            throw new CloudServiceException("已售罄");
        }
        TimeUnit.MILLISECONDS.sleep(50L);
        return Result.success(--publicResource);
    }

    @RedisLock
    @SneakyThrows
    @GetMapping("/test02")
    public Result<Integer> test02() {
        if (publicResource >= 20) {
            throw new CloudServiceException("超过限额");
        }
        TimeUnit.MILLISECONDS.sleep(50L);
        return Result.success(++publicResource);
    }

}
