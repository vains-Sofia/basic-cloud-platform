package com.basic.framework.oauth2.core.util;

import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.core.util.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;

/**
 * web工具类
 *
 * @author vains
 */
@Slf4j
@UtilityClass
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

    /**
     * 获取当前请求
     *
     * @return 当前请求，获取失败会返回null
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return sra == null ? null : sra.getRequest();
    }

    /**
     * 获取当前请求
     *
     * @return 当前请求，获取失败会返回null
     */
    public static String getRequestPath() {
        HttpServletRequest request = ServletUtils.getRequest();
        if (request == null) {
            log.debug("获取HttpServletRequest失败，无法获取请求信息.");
            throw new CloudServiceException("获取HttpServletRequest失败，无法获取请求信息.");
        }
        // 取出当前路径和ContextPath，如果有ContextPath则替换为空
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        // 替换ContextPath
        String requestPath;
        if (!ObjectUtils.isEmpty(contextPath)) {
            requestPath = requestURI.replaceFirst(contextPath, "");
        } else {
            requestPath = requestURI;
        }
        return requestPath;
    }



}
