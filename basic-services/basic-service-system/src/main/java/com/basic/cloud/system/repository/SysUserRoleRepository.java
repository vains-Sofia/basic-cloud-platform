package com.basic.cloud.system.repository;

import com.basic.cloud.system.domain.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户角色关联表Repository
 *
 * @author vains
 */
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {

    /**
     * 根据用户id删除
     *
     * @param userId 用户id
     * @return 删除的记录数
     */
    Long deleteByUserId(Long userId);

    /**
     * 根据用户id查询
     *
     * @param userId 用户id
     * @return 用户角色列表
     */
    List<SysUserRole> findByUserId(Long userId);

    /**
     * 根据角色id查询
     *
     * @param roleId 角色id
     * @return 用户角色列表
     */
    List<SysUserRole> findByRoleId(Long roleId);
}
