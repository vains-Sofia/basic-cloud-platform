package com.basic.framework.core.exception;

/**
 * Spring Cloud 统一异常
 *
 * @author vains
 */
public class CloudServiceException extends RuntimeException {

    public CloudServiceException(String msg) {
        super(msg);
    }

}
