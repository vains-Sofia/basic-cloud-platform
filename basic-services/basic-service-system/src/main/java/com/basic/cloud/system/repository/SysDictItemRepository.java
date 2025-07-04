package com.basic.cloud.system.repository;

import com.basic.cloud.system.domain.SysDictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 字典项数据访问接口
 * 提供对字典项的基本CRUD操作以及根据字典类型编码查询字典项的功能。
 *
 * @author vains
 */
public interface SysDictItemRepository extends JpaRepository<SysDictItem, Long>, JpaSpecificationExecutor<SysDictItem> {

    /**
     * 根据字典类型编码查询字典项，并按排序顺序升序排列
     *
     * @param typeCode 字典类型编码
     * @return 字典项列表
     */
    List<SysDictItem> findByTypeCodeOrderBySortOrderAsc(String typeCode);

    /**
     * 根据字典类型编码删除所有相关的字典项
     *
     * @param typeCode 字典类型编码
     */
    void deleteByTypeCode(String typeCode);
}
