package com.basic.framework.openfeign.decoder;

import com.basic.framework.core.exception.CloudServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * 基础异常解析器
 *
 * @author vains
 */
@Slf4j
public class BasicErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.warn("OpenFeign调用失败，methodKey：{}, reason：{}", methodKey, response.reason());
        return switch (response.status()) {
            case 400 -> new CloudServiceException(HttpStatus.BAD_REQUEST.getReasonPhrase());
            case 401 -> new CloudServiceException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            case 403 -> new CloudServiceException(HttpStatus.FORBIDDEN.getReasonPhrase());
            case 404 -> new CloudServiceException(HttpStatus.NOT_FOUND.getReasonPhrase());
            case 500 -> new CloudServiceException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            default -> new CloudServiceException(response.reason());
        };
    }

}
