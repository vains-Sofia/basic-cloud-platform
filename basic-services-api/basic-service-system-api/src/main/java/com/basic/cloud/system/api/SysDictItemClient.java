package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.SysDictItemPageRequest;
import com.basic.cloud.system.api.domain.request.SysDictItemRequest;
import com.basic.cloud.system.api.domain.response.FindSysDictItemResponse;
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
 * 字典项 api 接口
 *
 * @author vains
 */
@RequestMapping("/dict/item")
@Tag(name = "字典 api 接口", description = "字典项 api 接口")
@FeignClient(name = FeignConstants.SYSTEM_APPLICATION, path = FeignConstants.SYSTEM_CONTEXT_PATH, contextId = "SysDictItemClient")
public interface SysDictItemClient {

    /**
     * 分页查询字典项
     *
     * @param request 分页请求参数
     * @return 分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询字典项", description = "分页查询字典项")
    Result<DataPageResult<FindSysDictItemResponse>> pageQuery(@SpringQueryMap SysDictItemPageRequest request);

    /**
     * 根据字典类型编码查询字典项
     *
     * @param typeCode 字典类型编码
     * @return 字典项列表
     */
    @GetMapping("/type/{typeCode}")
    @Operation(summary = "根据字典类型编码查询字典项", description = "根据字典类型编码查询字典项")
    Result<List<FindSysDictItemResponse>> listByType(@PathVariable String typeCode);

    /**
     * 根据字典项ID查询字典项
     *
     * @param id 字典项ID
     * @return 字典项信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据字典项ID查询字典项", description = "根据字典项ID查询字典项")
    Result<FindSysDictItemResponse> getById(@PathVariable Long id);

    /**
     * 创建字典项
     *
     * @param request 字典项请求参数
     * @return 创建结果
     */
    @PostMapping
    @Operation(summary = "创建字典项", description = "创建字典项")
    Result<FindSysDictItemResponse> create(@Valid @RequestBody SysDictItemRequest request);

    /**
     * 更新字典项
     *
     * @param id      字典项ID
     * @param request 字典项请求参数
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新字典项", description = "更新字典项")
    Result<FindSysDictItemResponse> update(@PathVariable Long id,
                                           @Valid @RequestBody SysDictItemRequest request);

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典项", description = "删除字典项")
    Result<String> delete(@PathVariable Long id);

}
