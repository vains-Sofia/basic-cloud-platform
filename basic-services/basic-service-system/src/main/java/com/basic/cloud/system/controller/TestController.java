package com.basic.cloud.system.controller;

import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "测试接口", description = "测试接口")
public class TestController {

    @GetMapping("/test01")
    @Operation(summary = "测试接口01")
    public Result<String> test01() {
        return Result.success();
    }

}
