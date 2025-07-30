package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.FindApiScanRecordPageRequest;
import com.basic.cloud.system.api.domain.response.FindApiScanRecordResponse;
import com.basic.framework.core.domain.DataPageResult;

/**
 * API扫描记录Service
 *
 * @author vains
 */
public interface SysApiScanRecordService {

    /**
     * 分页查询API扫描记录列表
     *
     * @param request 分页查询API扫描记录列表入参
     * @return API扫描记录信息
     */
    DataPageResult<FindApiScanRecordResponse> findByPage(FindApiScanRecordPageRequest request);

    /**
     * 查询API扫描记录详情
     *
     * @param id 记录id
     * @return API扫描记录信息
     */
    FindApiScanRecordResponse recordDetails(Long id);

    /**
     * 删除API扫描记录信息
     *
     * @param id 记录id
     */
    void removeById(Long id);
}
