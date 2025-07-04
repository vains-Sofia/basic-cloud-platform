package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.SysDictTypePageRequest;
import com.basic.cloud.system.api.domain.request.SysDictTypeRequest;
import com.basic.cloud.system.api.domain.response.FindSysDictTypeResponse;
import com.basic.framework.core.domain.DataPageResult;

import java.util.List;

/**
 * 字典类型Service接口
 *
 * @author vains
 */
public interface SysDictTypeService {

    /**
     * 查询所有字典类型
     *
     * @return 字典类型列表
     */
    List<FindSysDictTypeResponse> listAll();

    /**
     * 根据ID查询字典类型
     *
     * @param id 字典类型ID
     * @return 字典类型信息
     */
    FindSysDictTypeResponse getById(Long id);

    /**
     * 创建字典类型
     *
     * @param request 字典类型请求参数
     * @return 创建的字典类型信息
     */
    FindSysDictTypeResponse create(SysDictTypeRequest request);

    /**
     * 更新字典类型
     *
     * @param id 字典类型ID
     * @param request 字典类型请求参数
     * @return 更新后的字典类型信息
     */
    FindSysDictTypeResponse update(Long id, SysDictTypeRequest request);

    /**
     * 删除字典类型
     *
     * @param id 字典类型ID
     */
    void delete(Long id);

    /**
     * 根据字典类型编码查询字典类型
     *
     * @param typeCode 字典类型编码
     * @return 字典类型信息
     */
    FindSysDictTypeResponse getByTypeCode(String typeCode);

    /**
     * 分页查询字典类型
     *
     * @param request 分页请求参数
     * @return 分页结果
     */
    DataPageResult<FindSysDictTypeResponse> pageQuery(SysDictTypePageRequest request);
}
