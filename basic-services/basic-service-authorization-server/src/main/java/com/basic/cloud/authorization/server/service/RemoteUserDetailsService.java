package com.basic.cloud.authorization.server.service;

import com.basic.framework.oauth2.core.domain.security.PermissionGrantedAuthority;
import com.basic.cloud.system.api.SysBasicUserClient;
import com.basic.cloud.system.api.domain.response.BasicUserResponse;
import com.basic.framework.core.constants.HttpCodeConstants;
import com.basic.framework.core.domain.Result;
import com.basic.framework.oauth2.core.domain.oauth2.BasicAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 远程用户信息Service实现
 *
 * @author vains
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RemoteUserDetailsService implements UserDetailsService {

    private final SysBasicUserClient basicUserClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这里认为username(账号)是邮箱
        Result<BasicUserResponse> responseResult = basicUserClient.getByEmail(username);
        if (responseResult == null) {
            log.debug("调用api根据邮箱{}查询用户信息失败，响应null.", username);
            throw new UsernameNotFoundException(username);
        }
        if (responseResult.getCode() != HttpCodeConstants.HTTP_OK) {
            log.debug("调用api根据邮箱{}查询用户信息失败，状态码：{}，原因：{}.", username, responseResult.getCode(), responseResult.getMessage());
            throw new AuthenticationServiceException(responseResult.getMessage());
        }
        // 用户信息
        BasicUserResponse userResponse = responseResult.getData();
        if (userResponse == null) {
            log.debug("调用api根据邮箱{}查询用户信息失败，用户不存在.", username);
            throw new UsernameNotFoundException(username);
        }
        // 转移数据至统一认证用户信息类
        BasicAuthenticatedUser authenticatedUser = new BasicAuthenticatedUser();
        BeanUtils.copyProperties(userResponse, authenticatedUser);
        authenticatedUser.setSub(userResponse.getNickname());
        authenticatedUser.setName(userResponse.getNickname());
        if (!ObjectUtils.isEmpty(userResponse.getAuthorities())) {
            // 权限信息特殊处理
            Set<PermissionGrantedAuthority> authorities = userResponse.getAuthorities()
                    .stream()
                    .map(e -> {
                        PermissionGrantedAuthority authority = new PermissionGrantedAuthority();
                        authority.setPath(e.getPath());
                        authority.setAuthority(e.getPermission());
                        authority.setRequestMethod(e.getRequestMethod());
                        authority.setNeedAuthentication(e.getNeedAuthentication());
                        return authority;
                    })
                    .collect(Collectors.toSet());
            authenticatedUser.setAuthorities(authorities);
        }
        return authenticatedUser;
    }

}
