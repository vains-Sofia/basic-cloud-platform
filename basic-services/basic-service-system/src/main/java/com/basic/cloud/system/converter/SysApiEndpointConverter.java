package com.basic.cloud.system.converter;

import com.basic.cloud.system.api.domain.request.SaveApiEndpointRequest;
import com.basic.cloud.system.api.domain.response.FindApiEndpointResponse;
import com.basic.cloud.system.domain.SysApiEndpoint;
import org.springframework.beans.BeanUtils;

/**
 * API接口详情转换器
 *
 * @author vains
 */
public class SysApiEndpointConverter {

    /**
     * 实体转响应DTO
     *
     * @param entity 实体
     * @return 响应DTO
     */
    public FindApiEndpointResponse toResponse(SysApiEndpoint entity) {
        if (entity == null) {
            return null;
        }
        FindApiEndpointResponse response = new FindApiEndpointResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    /**
     * 请求DTO更新实体
     *
     * @param entity  实体
     * @param request 请求DTO
     */
    public void updateFromRequest(SysApiEndpoint entity, SaveApiEndpointRequest request) {
        if (entity == null || request == null) {
            return;
        }
        BeanUtils.copyProperties(request, entity);
    }

    /**
     * 请求DTO转实体
     *
     * @param request 请求DTO
     * @return 实体
     */
    public SysApiEndpoint toEntity(SaveApiEndpointRequest request) {
        if (request == null) {
            return null;
        }
        SysApiEndpoint entity = new SysApiEndpoint();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }
}
