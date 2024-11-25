package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.SysBasicUserClient;
import com.basic.cloud.system.api.domain.request.FindBasicUserPageRequest;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.cloud.system.service.SysBasicUserService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 基础用户信息接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class SysBasicUserController implements SysBasicUserClient {

    private final SysBasicUserService basicUserService;

    @Override
    public Result<BasicUserResponse> getByEmail(String email) {
        BasicUserResponse basicUser = basicUserService.getBasicUserByEmail(email);
        if (basicUser != null) {
            basicUser.setAuthorities(Set.of("USER"));
        }
        return Result.success(basicUser);
    }

    @Override
    public Result<PageResult<FindBasicUserResponse>> findByPage(FindBasicUserPageRequest request) {
        PageResult<FindBasicUserResponse> pageResult = basicUserService.findByPage(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<BasicUserResponse> userDetails(Long id) {
        BasicUserResponse basicUser = basicUserService.getById(id);
        return Result.success(basicUser);
    }

}
