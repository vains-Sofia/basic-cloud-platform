package com.basic.cloud.web.autoconfigure;

import com.basic.cloud.core.domain.Result;
import com.basic.cloud.core.exception.CloudServiceException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.DataTruncation;
import java.util.List;

/**
 * 全局异常处理
 *
 * @author vains
 */
@RestControllerAdvice
public class DefaultExceptionHandlerAdvice {

    /**
     * 处理统一的类型转换异常
     *
     * @param e 转换异常
     * @return 返回处理后的异常信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            StringBuilder errors = new StringBuilder();
            List<JsonMappingException.Reference> path = invalidFormatException.getPath();
            for (JsonMappingException.Reference reference : path) {
                errors.append("参数名：").append(reference.getFieldName()).append(" 输入不合法，需要的是 ").append(invalidFormatException.getTargetType().getName()).append(" 类型，").append("提交的值是：").append(invalidFormatException.getValue());
            }
            return Result.error(HttpStatus.BAD_REQUEST.value(), errors.toString());
        } else if (e.getCause() instanceof JsonParseException) {
            return Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return Result.error(HttpStatus.BAD_REQUEST.value(), "参数转换异常");
    }

    /**
     * 处理URL的类型转换异常
     *
     * @param e 转换异常
     * @return 返回处理后的异常信息
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<String> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = String.format("类型转换失败，参数%s需要的类型是：%s, 请传入正确的参数类型", e.getName(), e.getRequiredType());
        return Result.error(HttpStatus.BAD_REQUEST.value(), message);
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