package com.basic.cloud.monitor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面在未登录时会跳转到/login页面，配置中设置的登录页面路径不是/login，所以会触发oauth2的授权，
 * 授权完成以后会重新跳回/login，此时会跳转到此Controller，该接口转发至首页
 *
 * @author vains
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

    /**
     * 该接口转发至首页
     *
     * @return 请求转发至首页
     */
    @GetMapping(path = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {

        return "redirect:/";
    }

}
