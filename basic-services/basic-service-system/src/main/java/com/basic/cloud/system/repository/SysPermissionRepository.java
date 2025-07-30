package com.basic.cloud.system.repository;

import com.basic.cloud.system.domain.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 权限信息Jpa Repository
 *
 * @author vains
 */
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long>, JpaSpecificationExecutor<SysPermission> {

    /**
     * 根据模块名称查询权限列表
     *
     * @param moduleName 模块名称
     * @return 权限列表
     */
    List<SysPermission> findByModuleName(String moduleName);
}
