package com.basic.example.mybatis.plus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.basic.framework.core.domain.Result;
import com.basic.example.mybatis.plus.domain.request.PageRequest;
import com.basic.example.mybatis.plus.entity.ExampleUser;
import com.basic.example.mybatis.plus.service.IExampleUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/page")
    @Operation(summary = "分页查询接口", description = "示例分页查询用户")
    public Result<IPage<ExampleUser>> page(@Validated PageRequest userRequest) {
        LambdaQueryWrapper<ExampleUser> wrapper = Wrappers.lambdaQuery(ExampleUser.class)
                .eq(userRequest.getId() != null, ExampleUser::getId, userRequest.getId());
        IPage<ExampleUser> page = Page.of(userRequest.getCurrent(), userRequest.getSize());
        return Result.success(exampleUserService.page(page, wrapper));
    }

    @PostMapping("/insert")
    @Operation(summary = "插入接口", description = "示例添加用户")
    public Result<Boolean> insert(@Validated @RequestBody ExampleUser exampleUser) {
        return Result.success(exampleUserService.save(exampleUser));
    }

    @PutMapping("/update")
    @Operation(summary = "修改接口", description = "示例修改用户")
    public Result<Boolean> update(@Validated @RequestBody ExampleUser exampleUser) {
        return Result.success(exampleUserService.updateById(exampleUser));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除接口", description = "示例删除用户")
    public Result<Boolean> deleteUser(@Validated @PathVariable Long id) {
        return Result.success(exampleUserService.removeById(id));
    }

}
