package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.request.FindBasicUserPageRequest;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.framework.core.domain.PageResult;

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
    BasicUserResponse getBasicUserByEmail(String email);

    /**
     * 分页查询基础用户信息列表
     *
     * @param request 分页查询基础用户信息列表入参
     * @return 用户信息
     */
    PageResult<FindBasicUserResponse> findByPage(FindBasicUserPageRequest request);

    /**
     * 根据用户id查询用户信息
     *
     * @param id 用户id
     * @return 返回用户详情
     */
    BasicUserResponse getById(Long id);
}
