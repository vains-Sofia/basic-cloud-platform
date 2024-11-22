package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.domain.SysBasicUser;
import com.basic.cloud.system.repository.SysBasicUserRepository;
import com.basic.cloud.system.service.SysBasicUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 基础用户信息Service实现
 *
 * @author vains
 */
@Service
@RequiredArgsConstructor
public class SysBasicUserServiceImpl implements SysBasicUserService {

    private final SysBasicUserRepository basicUserRepository;

    @Override
    public BasicUserResponse getBasicUserByEmail(String email) {
        // 查询用户信息
        Optional<SysBasicUser> basicUserOptional = basicUserRepository.findByEmail(email);
        // 转为响应bean
        return basicUserOptional.map(u -> {
            BasicUserResponse basicUserResponse = new BasicUserResponse();
            BeanUtils.copyProperties(u, basicUserResponse);
            return basicUserResponse;
        }).orElse(null);
    }

}
