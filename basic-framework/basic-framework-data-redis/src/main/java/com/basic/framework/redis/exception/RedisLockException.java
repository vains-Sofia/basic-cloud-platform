package com.basic.framework.redis.exception;

import com.basic.framework.core.exception.CloudServiceException;

/**
 * Redis 分布式锁异常
 *
 * @author vains
 */
public class RedisLockException extends CloudServiceException {

    public RedisLockException(String msg) {
        super(msg);
    }
}
