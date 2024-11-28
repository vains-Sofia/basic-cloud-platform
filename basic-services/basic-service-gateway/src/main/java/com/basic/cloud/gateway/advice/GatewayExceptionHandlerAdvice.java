package com.basic.cloud.gateway.advice;

import com.basic.framework.core.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.resource.NoResourceFoundException;

/**
 * 网关异常处理
 *
 * @author vains
 */
@Slf4j
@RestControllerAdvice
public class GatewayExceptionHandlerAdvice {

    /**
     * 404 异常处理
     *
     * @param response 响应对象
     * @param ex       具体的异常实例
     * @return 统一响应类
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<String> exception(ServerHttpResponse response, NoResourceFoundException ex) {
        log.error(ex.getReason(), ex);
        response.setStatusCode(ex.getStatusCode());
        return Result.error(ex.getStatusCode().value(), ex.getReason());
    }

    /**
     * 503 服务不可用
     *
     * @param response 响应对象
     * @param ex       具体的异常实例
     * @return 统一响应类
     */
    @ExceptionHandler(NotFoundException.class)
    public Result<String> exception(ServerHttpResponse response, NotFoundException ex) {
        log.error(ex.getReason(), ex);
        response.setStatusCode(ex.getStatusCode());
        return Result.error(ex.getStatusCode().value(), ex.getReason());
    }

    /**
     * 所有其它异常
     *
     * @param ex 具体的异常实例
     * @return 统一响应类
     */
    @ExceptionHandler(Exception.class)
    public Result<String> exception(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

}
