package com.basic.framework.oauth2.storage.core.exception;

import com.basic.framework.core.exception.CloudServiceException;

/**
 * 客户端存储相关异常
 *
 * @author vains
 */
public class BasicApplicationStorageException extends CloudServiceException {

    public BasicApplicationStorageException(String msg) {
        super(msg);
    }
}
