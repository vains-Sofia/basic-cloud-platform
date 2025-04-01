package com.basic.framework.oauth2.storage.exception;

import com.basic.framework.core.exception.CloudServiceException;

/**
 * 客户端存储相关异常
 *
 * @author vains
 */
public class ApplicationStorageException extends CloudServiceException {

    public ApplicationStorageException(String msg) {
        super(msg);
    }
}
