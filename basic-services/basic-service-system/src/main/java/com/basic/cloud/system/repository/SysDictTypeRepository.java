package com.basic.cloud.system.repository;

import com.basic.cloud.system.domain.SysDictType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 字典类型数据访问接口
 *
 * @author vains
 */
public interface SysDictTypeRepository extends JpaRepository<SysDictType, Long>, JpaSpecificationExecutor<SysDictType> {

    /**
     * 根据字典类型编码查询字典类型
     *
     * @param typeCode 字典类型编码
     * @return Optional<SysDictType>
     */
    Optional<SysDictType> findByTypeCode(String typeCode);
}
