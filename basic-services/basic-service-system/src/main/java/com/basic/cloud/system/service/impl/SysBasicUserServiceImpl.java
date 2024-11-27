package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.FindBasicUserPageRequest;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.cloud.system.api.domain.security.PermissionAuthority;
import com.basic.cloud.system.domain.SysBasicUser;
import com.basic.cloud.system.domain.SysRole;
import com.basic.cloud.system.repository.SysBasicUserRepository;
import com.basic.cloud.system.service.SysBasicUserService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
            List<SysRole> roles = u.getRoles();
            if (!ObjectUtils.isEmpty(roles)) {
                // 提取用户权限
                Set<PermissionAuthority> authorities = roles.stream()
                        .filter(role -> !ObjectUtils.isEmpty(role.getPermissions()))
                        .flatMap(role -> role
                                .getPermissions()
                                .stream()
                                .map(e -> {
                                    PermissionAuthority authority = new PermissionAuthority();
                                    authority.setPath(e.getPath());
                                    authority.setPermission(e.getPermission());
                                    authority.setRequestMethod(e.getRequestMethod());
                                    authority.setNeedAuthentication(e.getNeedAuthentication());
                                    return authority;
                                })
                        ).collect(Collectors.toSet());
                basicUserResponse.setAuthorities(authorities);
            }
            return basicUserResponse;
        }).orElse(null);
    }

    @Override
    public PageResult<FindBasicUserResponse> findByPage(FindBasicUserPageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(SysBasicUser::getUpdateTime));
        // 分页
        PageRequest pageQuery = PageRequest.of(request.current(), request.size(), sort);

        // 条件构造器
        SpecificationBuilder<SysBasicUser> builder = new SpecificationBuilder<>();
        builder.like(!ObjectUtils.isEmpty(request.getNickname()), SysBasicUser::getNickname,
                request.getNickname());
        builder.like(!ObjectUtils.isEmpty(request.getEmail()), SysBasicUser::getEmail,
                request.getEmail());
        builder.eq(!ObjectUtils.isEmpty(request.getGender()), SysBasicUser::getGender,
                request.getGender());

        // 执行查询
        Page<SysBasicUser> findPageResult = basicUserRepository.findAll(builder, pageQuery);
        // 转为响应bean
        List<FindBasicUserResponse> authorizationList = findPageResult.getContent()
                .stream()
                .map(e -> {
                    FindBasicUserResponse basicUserResponse = new FindBasicUserResponse();
                    BeanUtils.copyProperties(e, basicUserResponse);
                    return basicUserResponse;
                })
                .toList();

        return DataPageResult.of(findPageResult.getNumber(), findPageResult.getSize(), findPageResult.getTotalElements(), authorizationList);
    }

    @Override
    public BasicUserResponse getById(Long id) {
        return basicUserRepository.findById(id).map(u -> {
            BasicUserResponse basicUserResponse = new BasicUserResponse();
            BeanUtils.copyProperties(u, basicUserResponse);
            return basicUserResponse;
        }).orElse(null);
    }

}
