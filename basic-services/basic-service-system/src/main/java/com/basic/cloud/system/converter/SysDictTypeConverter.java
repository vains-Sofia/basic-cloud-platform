package com.basic.cloud.system.converter;

import com.basic.cloud.system.api.domain.request.SysDictTypeRequest;
import com.basic.cloud.system.api.domain.response.FindSysDictTypeResponse;
import com.basic.cloud.system.domain.SysDictType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典类型转换器
 *
 * @author vains
 */
@Component
public class SysDictTypeConverter {

    /**
     * 实体转换为响应对象
     *
     * @param dictType 字典类型实体
     * @return 字典类型响应对象
     */
    public FindSysDictTypeResponse convertToResponse(SysDictType dictType) {
        if (dictType == null) {
            return null;
        }

        FindSysDictTypeResponse response = new FindSysDictTypeResponse();
        response.setId(dictType.getId());
        response.setTypeCode(dictType.getTypeCode());
        response.setName(dictType.getName());
        response.setDescription(dictType.getDescription());
        response.setStatus(dictType.getStatus());
        response.setCreateTime(dictType.getCreateTime());
        response.setUpdateTime(dictType.getUpdateTime());
        response.setCreateBy(dictType.getCreateBy());
        response.setUpdateBy(dictType.getUpdateBy());
        response.setCreateName(dictType.getCreateName());
        response.setUpdateName(dictType.getUpdateName());

        return response;
    }

    /**
     * 实体列表转换为响应对象列表
     *
     * @param dictTypes 字典类型实体列表
     * @return 字典类型响应对象列表
     */
    public List<FindSysDictTypeResponse> convertToResponseList(List<SysDictType> dictTypes) {
        if (dictTypes == null) {
            return null;
        }

        return dictTypes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 请求对象转换为实体
     *
     * @param request 字典类型请求对象
     * @return 字典类型实体
     */
    public SysDictType convertFromRequest(SysDictTypeRequest request) {
        if (request == null) {
            return null;
        }

        SysDictType dictType = new SysDictType();
        dictType.setTypeCode(request.getTypeCode());
        dictType.setName(request.getName());
        dictType.setDescription(request.getDescription());
        dictType.setStatus(request.getStatus());

        return dictType;
    }
}
