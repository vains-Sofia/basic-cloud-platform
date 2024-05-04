package com.basic.cloud.oauth2.authorization.util;

import com.basic.cloud.core.util.JsonUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

/**
 * web工具类
 *
 * @author vains
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServletUtils {

    /**
     * 通过response写回json
     *
     * @param response 响应对象实例
     * @param data     要写回的数据
     */
    @SneakyThrows
    public static void renderJson(final HttpServletResponse response, final Object data) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JsonUtils.toJson(data));
        response.getWriter().flush();
    }

}
