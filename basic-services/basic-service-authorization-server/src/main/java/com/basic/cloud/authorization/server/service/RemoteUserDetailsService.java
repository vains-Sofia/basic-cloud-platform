package com.basic.cloud.authorization.server.service;

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

import java.util.regex.Pattern;

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
        Result<BasicUserResponse> responseResult;
        if (this.isValidEmail(username)) {
            // 这里认为username(账号)是邮箱
            responseResult = basicUserClient.getByEmail(username);
        } else {
            // 这里认为username(账号)就是账号
            responseResult = basicUserClient.getByUsername(username);
        }
        if (responseResult == null) {
            log.debug("调用api根据{}查询用户信息失败，响应null.", username);
            throw new UsernameNotFoundException(username);
        }
        if (responseResult.getCode() != HttpCodeConstants.HTTP_OK) {
            log.debug("调用api根据{}查询用户信息失败，状态码：{}，原因：{}.", username, responseResult.getCode(), responseResult.getMessage());
            throw new AuthenticationServiceException(responseResult.getMessage());
        }
        // 用户信息
        BasicUserResponse userResponse = responseResult.getData();
        if (userResponse == null) {
            log.debug("调用api根据{}查询用户信息失败，用户不存在.", username);
            throw new UsernameNotFoundException(username);
        }
        // 转移数据至统一认证用户信息类
        BasicAuthenticatedUser authenticatedUser = new BasicAuthenticatedUser();
        BeanUtils.copyProperties(userResponse, authenticatedUser);
        authenticatedUser.setSub(userResponse.getNickname());
        authenticatedUser.setName(userResponse.getNickname());
        authenticatedUser.setUsername(userResponse.getNickname());
        authenticatedUser.setAuthorities(userResponse.getAuthorities());
        return authenticatedUser;
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, email);
    }

}
