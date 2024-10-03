package com.basic.example.doc.controller;

import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文档示例接口
 *
 * @author vains
 */
@RestController
@RequestMapping("/example/doc")
@Tag(name = "在线文档示例接口", description = "提供示例内容展示SpringDoc集成效果")
public class DocExampleController {

    @GetMapping
    @Parameter(name = "data", description = "url参数")
    @Operation(summary = "查询参数示例", description = "原样返回入参")
    public Result<String> get(String data) {
        return Result.success(data);
    }

}
