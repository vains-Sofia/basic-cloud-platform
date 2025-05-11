package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.SysBasicUserClient;
import com.basic.cloud.system.api.domain.request.*;
import com.basic.cloud.system.api.domain.response.AuthenticatedUserResponse;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.cloud.system.domain.SysBasicUser;
import com.basic.cloud.system.repository.SysBasicUserRepository;
import com.basic.cloud.system.service.SysBasicUserService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 基础用户信息接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class SysBasicUserController implements SysBasicUserClient {

    private final PasswordEncoder passwordEncoder;

    private final SysBasicUserService basicUserService;

    private final SysBasicUserRepository sysBasicUserRepository;

    @Override
    public Result<BasicUserResponse> getByUsername(String username) {
        BasicUserResponse basicUser = basicUserService.getBasicUserByUsername(username);
        return Result.success(basicUser);
    }

    @Override
    public Result<BasicUserResponse> getByEmail(String email) {
        BasicUserResponse basicUser = basicUserService.getBasicUserByEmail(email);
        return Result.success(basicUser);
    }

    @Override
    public Result<PageResult<FindBasicUserResponse>> findByPage(FindBasicUserPageRequest request) {
        PageResult<FindBasicUserResponse> pageResult = basicUserService.findByPage(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<FindBasicUserResponse> userDetails(Long id) {
        FindBasicUserResponse basicUser = basicUserService.getById(id);
        return Result.success(basicUser);
    }

    @Override
    public Result<String> getRegisterEmailCode(String email) {
        String errorMessage = basicUserService.getRegisterEmailCode(email);
        return ObjectUtils.isEmpty(errorMessage) ? Result.success() : Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
    }

    @Override
    public Result<String> userRegister(UserRegisterRequest request) {
        basicUserService.userRegister(request);
        return Result.success();
    }

    @Override
    public Result<String> resetPassword(ResetPasswordRequest request) {
        Optional<SysBasicUser> basicUserOptional = sysBasicUserRepository.findById(request.getUserId());
        if (basicUserOptional.isEmpty()) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户不存在.");
        }
        basicUserOptional.ifPresent(sysBasicUser -> {
            // 密码加密
            String password = passwordEncoder.encode(request.getPassword());
            sysBasicUser.setPassword(password);
            sysBasicUserRepository.save(sysBasicUser);
        });
        return Result.success();
    }

    @Override
    public Result<String> insertBasicUser(SaveBasicUserRequest request) {
        // 置空id，防止插入变修改
        request.setId(null);
        basicUserService.saveBasicUser(request);
        return Result.success();
    }

    @Override
    public Result<String> updateBasicUser(SaveBasicUserRequest request) {
        basicUserService.saveBasicUser(request);
        return Result.success();
    }

    @Override
    public Result<String> removeById(Long id) {
        basicUserService.removeById(id);
        return Result.success();
    }

    @Override
    public Result<AuthenticatedUserResponse> loginUserinfo() {
        AuthenticatedUserResponse userInfoResponse = basicUserService.getLoginUserinfo();
        return Result.success(userInfoResponse);
    }

    @Override
    public Result<String> updateUserRoles(UpdateUserRolesRequest request) {
        basicUserService.updateUserRoles(request);
        return Result.success();
    }

}
