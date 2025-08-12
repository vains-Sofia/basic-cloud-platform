
package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.FindApiEndpointPageRequest;
import com.basic.cloud.system.api.domain.request.FindApiEndpointRequest;  
import com.basic.cloud.system.api.domain.request.SaveApiEndpointRequest;
import com.basic.cloud.system.api.domain.response.FindApiEndpointResponse;
import com.basic.framework.core.domain.DataPageResult;

import java.util.List;

/**
 * API接口详情Service
 *
 * @author vains
 */
public interface SysApiEndpointService {

    /**
     * 分页查询API接口列表
     *
     * @param request 分页查询API接口列表入参
     * @return API接口信息
     */
    DataPageResult<FindApiEndpointResponse> findByPage(FindApiEndpointPageRequest request);

    /**
     * 查询API接口详情
     *
     * @param id 接口id
     * @return API接口信息
     */
    FindApiEndpointResponse endpointDetails(Long id);

    /**
     * 添加或修改API接口信息
     *
     * @param request API接口信息
     */
    void saveApiEndpoint(SaveApiEndpointRequest request);

    /**
     * 删除API接口信息
     *
     * @param id 接口id
     */
    void removeById(Long id);

    /**
     * 查询API接口信息列表
     *
     * @param request 查询API接口信息列表入参
     * @return API接口信息
     */
    List<FindApiEndpointResponse> findApiEndpoints(FindApiEndpointRequest request);

    /**
     * 根据扫描批次ID查询接口列表
     *
     * @param scanBatchId 扫描批次ID
     * @return 接口列表
     */
    List<FindApiEndpointResponse> findByScanBatchId(Long scanBatchId);

    /**
     * 扫描系统接口
     *
     * @param applications 应用名称
     * @return 扫描批次ID
     */
    Long scanApiEndpoints(List<String> applications);

    /**
     * 批量导入接口到权限表
     *
     * @param endpointIds 接口ID列表
     */
    void importToPermissions(List<Long> endpointIds);

    /**
     * 根据扫描批次ID导入接口
     *
     * @param scanBatchId 扫描批次ID
     */
    void importByScanBatchId(Long scanBatchId);

    /**
     * 忽略接口到权限表
     *
     * @param endpointIds 接口ID列表
     */
    void ignoreToPermissions(List<Long> endpointIds);

}
