package com.basic.cloud.system.service;

import com.basic.cloud.system.api.domain.response.BasicUserResponse;

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

}
