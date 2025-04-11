package com.basic.cloud.system.repository;

import com.basic.cloud.system.domain.SysBasicUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 基础用户repository
 *
 * @author vains
 */
public interface SysBasicUserRepository extends JpaRepository<SysBasicUser, Long>, JpaSpecificationExecutor<SysBasicUser> {

    /**
     * 根据用户邮件地址获取用户基础信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    Optional<SysBasicUser> findByEmail(@Email @Size(max = 50) String email);

    /**
     * 根据用户账号获取用户基础信息
     *
     * @param username 用户账号
     * @return 用户信息
     */
    Optional<SysBasicUser> findByUsername(@Size(max = 255) String username);
}
