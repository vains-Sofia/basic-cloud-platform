package com.basic.framework.oauth2.core.util;

import com.basic.framework.core.util.JsonUtils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * webflux工具类
 *
 * @author vains
 */
@UtilityClass
public class ReactiveUtils {

    /**
     * 通过response写回json
     *
     * @param response 响应对象实例
     * @param data     要写回的数据
     */
    @SneakyThrows
    public static Mono<Void> renderJson(final ServerHttpResponse response, final Object data) {
        response.getHeaders().set("Content-Type", "application/json");
        DataBufferFactory bufferFactory = response.bufferFactory();
        byte[] bytes = JsonUtils.getMapper().writeValueAsBytes(data);
        DataBuffer wrap = bufferFactory.wrap(bytes);
        return response.writeWith(Mono.fromSupplier(() -> wrap));
    }
}
