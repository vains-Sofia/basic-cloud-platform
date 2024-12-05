package com.basic.cloud.system.repository;

import com.basic.cloud.system.domain.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 权限信息Jpa Repository
 *
 * @author vains
 */
public interface SysRoleRepository extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

}
