package com.basic.cloud.example.mybatis.plus.controller;

import com.basic.cloud.core.domain.Result;
import com.basic.cloud.example.mybatis.plus.entity.ExampleUser;
import com.basic.cloud.example.mybatis.plus.service.IExampleUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author vains
 * @since 2024-05-15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/exampleUser")
public class ExampleUserController {

    private final IExampleUserService exampleUserService;

    @GetMapping("/select")
    @Operation(summary = "查询接口", description = "示例查询用户")
    public Result<List<ExampleUser>> select() {
        return Result.success(exampleUserService.list());
    }

}
