package com.basic.framework.web.autoconfigure;

import com.basic.framework.core.domain.Result;
import com.basic.framework.core.exception.CloudServiceException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.DataTruncation;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author vains
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandlerAdvice {

    /**
     * 处理统一的类型转换异常
     *
     * @param e 转换异常
     * @return 返回处理后的异常信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            StringBuilder errors = new StringBuilder();
            List<JsonMappingException.Reference> path = invalidFormatException.getPath();
            for (JsonMappingException.Reference reference : path) {
                errors.append("参数[").append(reference.getFieldName()).append("]类型错误.");
            }
            log.error(errors.toString(), e);
            return Result.error(HttpStatus.BAD_REQUEST.value(), errors.toString());
        } else if (e.getCause() instanceof JsonParseException) {
            log.error(e.getMessage(), e);
            return Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        log.error("参数转换异常.", e);
        return Result.error(HttpStatus.BAD_REQUEST.value(), "参数转换异常");
    }

    /**
     * 请求类型不支持
     *
     * @param e 请求类型不支持异常
     * @return 统一响应类
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Result<String> handleNotSupportedHttpMethodException(HttpRequestMethodNotSupportedException e) {
        log.error("{}.", e.getMessage(), e);
        return Result.error(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }

    /**
     * media type not support
     *
     * @param e media type not support 异常
     * @return 统一响应类
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public Result<String> handleNotSupportedHttpMethodException(HttpMediaTypeNotSupportedException e) {
        log.error("{}.", e.getMessage(), e);
        return Result.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), e.getMessage());
    }

    /**
     * Sql 异常：数据长度超过字段最大长度问题
     *
     * @param e 具体地校验异常
     * @return 返回处理后的异常信息
     */
    @ExceptionHandler(DataTruncation.class)
    public Result<String> dataTruncation(DataTruncation e) {
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * 项目业务异常
     *
     * @param e 具体地校验异常
     * @return 返回处理后的异常信息
     */
    @ExceptionHandler(CloudServiceException.class)
    public Result<String> cloudServiceException(CloudServiceException e) {
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * 捕获其它异常
     *
     * @param e 具体地校验异常
     * @return 返回处理后的异常信息
     */
    @ExceptionHandler(Exception.class)
    public Result<String> exception(Exception e) {
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

}