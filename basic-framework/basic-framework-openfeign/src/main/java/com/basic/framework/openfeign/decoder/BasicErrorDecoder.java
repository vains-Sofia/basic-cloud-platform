package com.basic.framework.openfeign.decoder;

import com.basic.framework.core.domain.Result;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.core.util.JsonUtils;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;

/**
 * 基础异常解析器
 *
 * @author vains
 */
@Slf4j
public class BasicErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            // 读取响应体内容
            String body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));

            // 尝试解析为 Result 对象
            Result<Object> result = JsonUtils.toObject(body, Result.class, Object.class);
            if (result != null) {
                log.warn("OpenFeign调用失败，methodKey：{}, reason：{}", methodKey, result.getMessage());
                // 如果解析成功，返回自定义异常
                return new CloudServiceException(result.getMessage());
            }
            log.warn("OpenFeign调用失败，methodKey：{}, reason：{}", methodKey, body);
            // 如果解析失败，返回默认异常
            return new CloudServiceException(body);
        } catch (Exception e) {
            log.debug("OpenFeign调用失败，methodKey：{}, 读取响应体内容失败，reason：{}", methodKey, e.getMessage());
        }
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
