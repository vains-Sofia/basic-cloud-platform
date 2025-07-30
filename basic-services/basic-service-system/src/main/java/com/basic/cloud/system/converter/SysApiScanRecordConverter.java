package com.basic.cloud.system.converter;

import com.basic.cloud.system.api.domain.response.FindApiScanRecordResponse;
import com.basic.cloud.system.domain.SysApiScanRecord;
import org.springframework.beans.BeanUtils;

/**
 * API扫描记录转换器
 *
 * @author vains
 */
public class SysApiScanRecordConverter {

    /**
     * 实体转响应DTO
     *
     * @param entity 实体
     * @return 响应DTO
     */
    public FindApiScanRecordResponse toResponse(SysApiScanRecord entity) {
        if (entity == null) {
            return null;
        }
        FindApiScanRecordResponse response = new FindApiScanRecordResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
