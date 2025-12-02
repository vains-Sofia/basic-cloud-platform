package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.SysDictTypePageRequest;
import com.basic.cloud.system.api.domain.request.SysDictTypeRequest;
import com.basic.cloud.system.api.domain.response.FindSysDictTypeResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典类型 api 接口
 *
 * @author vains
 */
@RequestMapping("/dict/type")
@Tag(name = "字典 api 接口", description = "字典类型 api 接口")
@FeignClient(name = FeignConstants.SYSTEM_APPLICATION, path = FeignConstants.SYSTEM_CONTEXT_PATH, contextId = "SysDictTypeClient")
public interface SysDictTypeClient {

    /**
     * 查询所有字典项
     *
     * @return 字典类型列表
     */
    @GetMapping("/all")
    @Operation(summary = "查询所有字典项", description = "查询所有字典项")
    Result<List<FindSysDictTypeResponse>> listAll();

    /**
     * 分页查询所有字典类型
     *
     * @return 字典类型列表
     */
    @GetMapping("/page")
    @Operation(summary = "查询所有字典类型", description = "查询所有字典类型")
    Result<DataPageResult<FindSysDictTypeResponse>> pageQuery(@Valid @SpringQueryMap SysDictTypePageRequest request);

    /**
     * 根据字典类型ID查询字典类型
     *
     * @param id 字典类型ID
     * @return 字典类型信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据字典类型ID查询字典类型", description = "根据字典类型ID查询字典类型")
    Result<FindSysDictTypeResponse> getById(@PathVariable Long id);

    /**
     * 创建字典类型
     *
     * @param request 字典类型请求参数
     * @return 创建结果
     */
    @PostMapping
    @Operation(summary = "创建字典类型", description = "创建字典类型")
    Result<FindSysDictTypeResponse> create(@Valid @RequestBody SysDictTypeRequest request);

    /**
     * 更新字典类型
     *
     * @param id      字典类型ID
     * @param request 字典类型请求参数
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新字典类型", description = "更新字典类型")
    Result<FindSysDictTypeResponse> update(@PathVariable Long id,
                                           @Valid @RequestBody SysDictTypeRequest request);

    /**
     * 删除字典类型
     *
     * @param id 字典类型ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典类型", description = "删除字典类型")
    Result<String> delete(@PathVariable Long id);

}
