package com.basic.cloud.system.converter;

import com.basic.cloud.system.api.domain.request.SysDictItemRequest;
import com.basic.cloud.system.api.domain.response.FindSysDictItemResponse;
import com.basic.cloud.system.domain.SysDictItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典项转换器
 *
 * @author vains
 */
@Component
public class SysDictItemConverter {

    /**
     * 实体转换为响应对象
     *
     * @param dictItem 字典项实体
     * @return 字典项响应对象
     */
    public FindSysDictItemResponse convertToResponse(SysDictItem dictItem) {
        if (dictItem == null) {
            return null;
        }

        FindSysDictItemResponse response = new FindSysDictItemResponse();
        response.setId(dictItem.getId());
        response.setTypeCode(dictItem.getTypeCode());
        response.setItemCode(dictItem.getItemCode());
        response.setItemName(dictItem.getItemName());
        response.setSortOrder(dictItem.getSortOrder());
        response.setStatus(dictItem.getStatus());
        response.setI18nJson(dictItem.getI18nJson());
        response.setCreateBy(dictItem.getCreateBy());
        response.setUpdateBy(dictItem.getUpdateBy());
        response.setCreateTime(dictItem.getCreateTime());
        response.setUpdateTime(dictItem.getUpdateTime());
        response.setCreateName(dictItem.getCreateName());
        response.setUpdateName(dictItem.getUpdateName());

        return response;
    }

    /**
     * 实体列表转换为响应对象列表
     *
     * @param dictItems 字典项实体列表
     * @return 字典项响应对象列表
     */
    public List<FindSysDictItemResponse> convertToResponseList(List<SysDictItem> dictItems) {
        if (dictItems == null) {
            return Collections.emptyList();
        }

        return dictItems.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 请求对象转换为实体
     *
     * @param request 字典项请求对象
     * @return 字典项实体
     */
    public SysDictItem convertFromRequest(SysDictItemRequest request) {
        if (request == null) {
            return null;
        }

        SysDictItem dictItem = new SysDictItem();
        dictItem.setTypeCode(request.getTypeCode());
        dictItem.setItemCode(request.getItemCode());
        dictItem.setItemName(request.getItemName());
        dictItem.setSortOrder(request.getSortOrder());
        dictItem.setStatus(request.getStatus());
        dictItem.setI18nJson(request.getI18nJson());

        return dictItem;
    }
}
