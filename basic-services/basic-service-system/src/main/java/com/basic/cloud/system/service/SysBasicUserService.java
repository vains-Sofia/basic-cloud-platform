package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.FindBasicUserPageRequest;
import com.basic.cloud.system.api.domain.request.SaveBasicUserRequest;
import com.basic.cloud.system.api.domain.request.UpdateUserRolesRequest;
import com.basic.cloud.system.api.domain.request.UserRegisterRequest;
import com.basic.cloud.system.api.domain.response.AuthenticatedUserResponse;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.framework.core.domain.PageResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

/**
 * 基础用户信息Service接口
 *
 * @author vains
 */
public interface SysBasicUserService {

    /**
     * 根据用户邮件地址获取用户基础信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    BasicUserResponse getBasicUserByEmail(@Email String email);

    /**
     * 分页查询基础用户信息列表
     *
     * @param request 分页查询基础用户信息列表入参
     * @return 用户信息
     */
    PageResult<FindBasicUserResponse> findByPage(@Valid FindBasicUserPageRequest request);

    /**
     * 根据用户id查询用户信息
     *
     * @param id 用户id
     * @return 返回用户详情
     */
    FindBasicUserResponse getById(Long id);

    /**
     * 用户注册
     *
     * @param request 用户注册入参
     */
    void userRegister(@Valid UserRegisterRequest request);

    /**
     * 根据邮箱获取验证码
     *
     * @param email 邮箱
     */
    void getRegisterEmailCode(@Email String email);

    /**
     * 添加/修改用户信息(如果id不为空则是修改，否则是添加)
     *
     * @param request 用户信息
     */
    void saveBasicUser(SaveBasicUserRequest request);

    /**
     * 删除用户信息
     *
     * @param id 用户id
     */
    void removeById(Long id);

    /**
     * 获取登录用户信息
     *
     * @return 登录用户信息
     */
    AuthenticatedUserResponse getLoginUserinfo();

    /**
     * 根据用户账号获取用户基础信息
     *
     * @param username 用户账号
     * @return 用户信息
     */
    BasicUserResponse getBasicUserByUsername(@Valid String username);

    /**
     * 更新用户角色
     *
     * @param request 更新用户角色入参
     */
    void updateUserRoles(@Valid UpdateUserRolesRequest request);
}
