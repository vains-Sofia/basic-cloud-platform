package com.basic.cloud.authorization.server.controller;

import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口
 *
 * @author YuJx
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    @PostMapping("/login/admin")
    public Result<OAuth2Token> login() {
        OAuth2Token token = tokenGenerator.generate(null);
        return Result.success(token);
    }

}
