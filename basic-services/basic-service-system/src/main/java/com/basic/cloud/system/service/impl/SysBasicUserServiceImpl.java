package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.*;
import com.basic.cloud.system.api.domain.response.AuthenticatedUserResponse;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.cloud.system.api.domain.response.FindBasicUserResponse;
import com.basic.cloud.system.domain.SysBasicUser;
import com.basic.cloud.system.domain.SysRole;
import com.basic.cloud.system.domain.SysUserRole;
import com.basic.cloud.system.repository.SysBasicUserRepository;
import com.basic.cloud.system.repository.SysUserRoleRepository;
import com.basic.cloud.system.service.CommonService;
import com.basic.cloud.system.service.SysBasicUserService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import com.basic.framework.core.util.RandomUtils;
import com.basic.framework.core.util.Sequence;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.core.domain.AuthenticatedUser;
import com.basic.framework.oauth2.core.domain.oauth2.DefaultAuthenticatedUser;
import com.basic.framework.oauth2.core.domain.security.BasicGrantedAuthority;
import com.basic.framework.oauth2.core.enums.OAuth2AccountPlatformEnum;
import com.basic.framework.oauth2.core.util.SecurityUtils;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class SysBasicUserServiceImpl implements SysBasicUserService {

    private final CommonService commonService;

    private final PasswordEncoder passwordEncoder;

    private final RedisOperator<String> redisOperator;

    private final SysUserRoleRepository userRoleRepository;

    private final Sequence sequence = new Sequence((null));

    private final SysBasicUserRepository basicUserRepository;

    private final String EMAIL_CAPTCHA_KEY = "captcha:email:";

    @Override
    public BasicUserResponse getBasicUserByEmail(String email) {
        // 查询用户信息
        Optional<SysBasicUser> basicUserOptional = basicUserRepository.findByEmail(email);
        // 转为响应bean
        return userEntity2Response(basicUserOptional);
    }

    @Override
    public PageResult<FindBasicUserResponse> findByPage(FindBasicUserPageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(SysBasicUser::getCreateTime));
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
    public FindBasicUserResponse getById(Long id) {
        return basicUserRepository.findById(id).map(u -> {
            FindBasicUserResponse basicUserResponse = new FindBasicUserResponse();
            BeanUtils.copyProperties(u, basicUserResponse);
            return basicUserResponse;
        }).orElse(null);
    }

    @Override
    public void userRegister(UserRegisterRequest request) {
        // 获取缓存的验证码
        String emailCaptcha = redisOperator.get(EMAIL_CAPTCHA_KEY.concat(request.getEmail()));
        if (ObjectUtils.isEmpty(emailCaptcha)) {
            throw new CloudIllegalArgumentException("验证码已过期，请重新获取。");
        }
        // 验证邮件验证码
        if (!emailCaptcha.equalsIgnoreCase(request.getEmailCaptcha())) {
            throw new CloudIllegalArgumentException("验证码错误。");
        }
        // 检查邮箱是否已被绑定
        SpecificationBuilder<SysBasicUser> builder = new SpecificationBuilder<>();
        builder.eq(SysBasicUser::getEmail, request.getEmail());
        boolean exists = basicUserRepository.exists(builder);
        if (exists) {
            throw new CloudIllegalArgumentException("邮箱已被注册。");
        }
        // 组装用户信息
        SysBasicUser sysBasicUser = new SysBasicUser();
        sysBasicUser.setId(sequence.nextId());
        sysBasicUser.setNickname(request.getNickname());
        sysBasicUser.setEmail(request.getEmail());
        sysBasicUser.setEmailVerified(Boolean.TRUE);
        sysBasicUser.setPassword(passwordEncoder.encode(request.getPassword()));
        sysBasicUser.setDeleted(Boolean.FALSE);
        sysBasicUser.setAccountPlatform(OAuth2AccountPlatformEnum.SYSTEM);

        // 构建默认认证用户为当前注册用户
        DefaultAuthenticatedUser authenticatedUser = new DefaultAuthenticatedUser(
                sysBasicUser.getNickname(), sysBasicUser.getAccountPlatform(), null);
        authenticatedUser.setId(sysBasicUser.getId());
        // 填充SecurityContextHolder，让审计信息自动填充可以获取到需要的数据
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(authenticatedUser, sysBasicUser.getPassword(), null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        basicUserRepository.save(sysBasicUser);
    }

    @Override
    public String getRegisterEmailCode(String email) {
        int randomLength = 4;
        String captcha = RandomUtils.randomNumber(randomLength);
        MailSenderRequest request = new MailSenderRequest();
        request.setFrom("Basic Cloud Platform");
        request.setSubject("注册验证码");
        request.setMailTo(Set.of(email));
        request.setContent("Your verification code is: " + captcha);
        String mailSenderResult = commonService.mailSender(request);
        if (!ObjectUtils.isEmpty(mailSenderResult)) {
            log.warn("给[{}]发送验证码失败，原因：{}.", email, mailSenderResult);
            return mailSenderResult;
        }

        // 缓存验证码至redis，5分钟
        redisOperator.set(EMAIL_CAPTCHA_KEY.concat(email), captcha, (5 * 60));
        log.debug("[{}]获取验证码成功，验证码：{}.", email, captcha);
        return null;
    }

    @Override
    public void saveBasicUser(SaveBasicUserRequest request) {
        boolean hasId = request.getId() != null;
        // 检查邮箱是否已被绑定
        SpecificationBuilder<SysBasicUser> builder = new SpecificationBuilder<>();
        builder.eq(SysBasicUser::getEmail, request.getEmail());
        // 修改需排除当前数据
        builder.ne(hasId, SysBasicUser::getId, request.getId());
        boolean exists = basicUserRepository.exists(builder);
        if (exists) {
            throw new CloudIllegalArgumentException("邮箱已被注册。");
        }

        // 组装用户信息
        SysBasicUser sysBasicUser = new SysBasicUser();
        BeanUtils.copyProperties(request, sysBasicUser);

        // 插入时初始化id与密码
        if (!hasId) {
            sysBasicUser.setId(sequence.nextId());
            // 密码加密
            if (ObjectUtils.isEmpty(request.getPassword())) {
                // 无密码
                sysBasicUser.setPassword("{noop}");
            } else {
                sysBasicUser.setPassword(passwordEncoder.encode(request.getPassword()));
            }
        } else {
            // 设置插入相关的审计信息
            Optional<SysBasicUser> basicUserOptional = basicUserRepository.findById(request.getId());
            if (basicUserOptional.isPresent()) {
                SysBasicUser existsBasicUser = basicUserOptional.get();
                // 不修改
                sysBasicUser.setRoles(existsBasicUser.getRoles());
                sysBasicUser.setPicture(existsBasicUser.getPicture());
                sysBasicUser.setDeleted(existsBasicUser.getDeleted());
                sysBasicUser.setPassword(existsBasicUser.getPassword());
                sysBasicUser.setCreateBy(existsBasicUser.getCreateBy());
                sysBasicUser.setCreateName(existsBasicUser.getCreateName());
                sysBasicUser.setCreateTime(existsBasicUser.getCreateTime());
                sysBasicUser.setAccountPlatform(existsBasicUser.getAccountPlatform());
                // 默认邮箱正确
                sysBasicUser.setEmailVerified(!ObjectUtils.isEmpty(existsBasicUser.getEmail()));
                // 默认手机号码正确
                sysBasicUser.setPhoneNumberVerified(!ObjectUtils.isEmpty(existsBasicUser.getPhoneNumber()));
            }
        }

        // 初始化默认信息
        sysBasicUser.setDeleted(Boolean.FALSE);
        sysBasicUser.setEmailVerified(Boolean.FALSE);
        if (sysBasicUser.getAccountPlatform() == null) {
            sysBasicUser.setAccountPlatform(OAuth2AccountPlatformEnum.SYSTEM);
        }

        basicUserRepository.save(sysBasicUser);
    }

    @Override
    public void removeById(Long id) {
        if (id == null) {
            throw new CloudIllegalArgumentException("用户id不能为空.");
        }
        basicUserRepository.deleteById(id);
    }

    @Override
    public AuthenticatedUserResponse getLoginUserinfo() {
        AuthenticatedUserResponse userResponse = new AuthenticatedUserResponse();
        AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser();
        if (authenticatedUser != null) {
            log.debug("当前登录用户信息为：{}", authenticatedUser);
            BeanUtils.copyProperties(authenticatedUser, userResponse);
            // 获取用户角色
            Optional<SysBasicUser> basicUser = basicUserRepository.findById(authenticatedUser.getId());
            if (basicUser.isPresent()) {
                SysBasicUser sysBasicUser = basicUser.get();
                userResponse.setUsername(sysBasicUser.getUsername());
                List<SysRole> roles = sysBasicUser.getRoles();
                if (!ObjectUtils.isEmpty(roles)) {
                    List<String> list = roles.stream().map(SysRole::getCode).toList();
                    userResponse.setRoles(list);
                }
            }
        }
        return userResponse;
    }

    @Override
    public BasicUserResponse getBasicUserByUsername(String username) {
        // 查询用户信息
        Optional<SysBasicUser> basicUserOptional = basicUserRepository.findByUsername(username);
        // 转为响应bean
        return userEntity2Response(basicUserOptional);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRoles(UpdateUserRolesRequest request) {
        boolean exists = basicUserRepository.existsById(request.getUserId());
        if (!exists) {
            throw new CloudIllegalArgumentException("用户不存在.");
        }
        // 删除用户原有的角色
        userRoleRepository.deleteByUserId(request.getUserId());
        // 转为用户角色关联实体
        List<SysUserRole> userRoles = request.getRoleIds().stream().map(id -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setId(sequence.nextId());
            userRole.setUserId(request.getUserId());
            userRole.setRoleId(id);
            return userRole;
        }).toList();
        if (ObjectUtils.isEmpty(userRoles)) {
            return;
        }
        userRoleRepository.saveAll(userRoles);
    }

    /**
     * 实体转换为响应bean
     *
     * @param basicUserOptional 查询实体
     * @return 响应bean
     */
    private BasicUserResponse userEntity2Response(Optional<SysBasicUser> basicUserOptional) {
        return basicUserOptional.map(u -> {
            BasicUserResponse basicUserResponse = new BasicUserResponse();
            BeanUtils.copyProperties(u, basicUserResponse);
            List<SysRole> roles = u.getRoles();
            if (!ObjectUtils.isEmpty(roles)) {
                // 提取用户权限
                Set<BasicGrantedAuthority> authorities = roles.stream()
                        .filter(role -> !ObjectUtils.isEmpty(role.getPermissions()))
                        .flatMap(role -> role
                                .getPermissions()
                                .stream()
                                .map(e -> {
                                    BasicGrantedAuthority authority = new BasicGrantedAuthority();
                                    authority.setId(e.getId());
                                    authority.setPath(e.getPath());
                                    authority.setAuthority(e.getPermission());
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

}
