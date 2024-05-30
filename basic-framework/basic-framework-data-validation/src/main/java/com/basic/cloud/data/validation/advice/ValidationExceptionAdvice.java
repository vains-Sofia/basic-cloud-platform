package com.basic.cloud.data.validation.advice;

import com.basic.cloud.core.domain.Result;
import com.basic.cloud.data.validation.resolver.ValidationExceptionResolver;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

/**
 * 参数校验异常处理
 *
 * @author vains
 */
@Slf4j
@RestControllerAdvice
public class ValidationExceptionAdvice {

    /**
     * 处理Json请求参数异常
     *
     * @param e 具体地校验异常
     * @return 返回处理后的异常信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = ValidationExceptionResolver.resolveFiledErrors(e.getBindingResult().getFieldErrors());
        log.error("参数校验失败：{}", errors, e);
        return Result.error(HttpStatus.BAD_REQUEST.value(), errors);
    }

    /**
     * 处理Form请求参数异常
     *
     * @param e 具体地校验异常
     * @return 返回处理后的异常信息
     */
    @ExceptionHandler(BindException.class)
    public Result<String> bindException(BindException e) {
        String errors = ValidationExceptionResolver.resolveFiledErrors(e.getFieldErrors());
        log.error("Form参数校验失败：{}", errors, e);
        return Result.error(HttpStatus.BAD_REQUEST.value(), errors);
    }

    /**
     * 验证异常处理 - @Validated加在controller类上，且在参数列表中直接指定constraints时触发
     *
     * @param e 具体地校验异常
     * @return 返回处理后的异常信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> methodArgumentNotValidException(ConstraintViolationException e) {
        String errors = ValidationExceptionResolver.resolveConstraintViolations(e);
        log.error("参数校验失败：{}", errors, e);
        return Result.error(HttpStatus.BAD_REQUEST.value(), errors);
    }

    /**
     * 验证异常处理 - 在控制器中对参数进行校验
     *
     * @param e 具体地校验异常
     * @return 返回处理后的异常信息
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Result<String> exception(HandlerMethodValidationException e) {
        List<? extends MessageSourceResolvable> allErrors = e.getAllErrors();
        String errors = ValidationExceptionResolver.resolveMessageErrors(allErrors);

        return Result.error(HttpStatus.BAD_REQUEST.value(), errors);
    }

}
