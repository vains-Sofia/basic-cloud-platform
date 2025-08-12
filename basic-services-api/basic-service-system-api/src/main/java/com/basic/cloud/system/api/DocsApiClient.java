package com.basic.cloud.system.api;

import com.basic.framework.core.constants.FeignConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Swagger OpenApi接口
 *
 * @author vains
 */
@Tag(name = "Swagger OpenApi接口", description = "获取Swagger OpenApi接口文档")
@FeignClient(name = FeignConstants.GATEWAY_APPLICATION, contextId = "DocsApiClient")
public interface DocsApiClient {

    /**
     * 获取指定应用的OpenAPI文档
     *
     * @param application 应用名称
     * @return OpenAPI文档
     */
    @GetMapping("/{application}/v3/api-docs")
    byte[] getOpenApi(@PathVariable String application);

}
