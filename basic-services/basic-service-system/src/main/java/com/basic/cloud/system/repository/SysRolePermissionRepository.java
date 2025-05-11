package com.basic.cloud.system.repository;

import com.basic.cloud.system.domain.SysRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 角色权限关联表Repository
 *
 * @author vains
 */
public interface SysRolePermissionRepository extends JpaRepository<SysRolePermission, Long> {

    /**
     * 根据角色id删除
     *
     * @param roleId 角色id
     * @return 删除的记录数
     */
    Long deleteByRoleId(Long roleId);

    /**
     * 根据角色id查询
     *
     * @param roleId 用户id
     * @return 角色权限列表
     */
    List<SysRolePermission> findByRoleId(Long roleId);

}
