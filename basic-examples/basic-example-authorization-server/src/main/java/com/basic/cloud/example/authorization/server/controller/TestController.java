package com.basic.cloud.example.authorization.server.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @author vains
 */
@RestController
@RequestMapping("/test")
@Tag(name = "认证服务测试接口", description = "提供认证服务集成效果")
public class TestController {

    @GetMapping("/test01")
    @PreAuthorize("hasAnyAuthority('SCOPE_profile')")
    public String test01() {
        return "Hello, test01";
    }

}
