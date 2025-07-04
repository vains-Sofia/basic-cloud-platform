package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.SysDictItemPageRequest;
import com.basic.cloud.system.api.domain.request.SysDictItemRequest;
import com.basic.cloud.system.api.domain.response.FindSysDictItemResponse;
import com.basic.framework.core.domain.DataPageResult;

import java.util.List;

/**
 * 字典项Service接口
 *
 * @author vains
 */
public interface SysDictItemService {

    /**
     * 查询所有字典项
     *
     * @return 字典项列表
     */
    List<FindSysDictItemResponse> listAll();

    /**
     * 根据字典类型编码查询字典项
     *
     * @param typeCode 字典类型编码
     * @return 字典项列表
     */
    List<FindSysDictItemResponse> listByType(String typeCode);

    /**
     * 根据ID查询字典项
     *
     * @param id 字典项ID
     * @return 字典项信息
     */
    FindSysDictItemResponse getById(Long id);

    /**
     * 创建字典项
     *
     * @param request 字典项请求参数
     * @return 创建的字典项信息
     */
    FindSysDictItemResponse create(SysDictItemRequest request);

    /**
     * 更新字典项
     *
     * @param id 字典项ID
     * @param request 字典项请求参数
     * @return 更新后的字典项信息
     */
    FindSysDictItemResponse update(Long id, SysDictItemRequest request);

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     */
    void delete(Long id);

    /**
     * 根据字典类型编码和字典项键查询字典项
     *
     * @param typeCode 字典类型编码
     * @param itemKey 字典项键
     * @return 字典项信息
     */
    FindSysDictItemResponse getByTypeCodeAndKey(String typeCode, String itemKey);

    /**
     * 分页查询字典项
     *
     * @param request 分页请求参数
     * @return 分页结果
     */
    DataPageResult<FindSysDictItemResponse> pageQuery(SysDictItemPageRequest request);
}
