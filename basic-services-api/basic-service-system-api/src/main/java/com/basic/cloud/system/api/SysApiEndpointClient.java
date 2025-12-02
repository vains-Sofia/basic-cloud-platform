package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.FindApiEndpointPageRequest;
import com.basic.cloud.system.api.domain.request.FindApiEndpointRequest;
import com.basic.cloud.system.api.domain.request.SaveApiEndpointRequest;
import com.basic.cloud.system.api.domain.response.FindApiEndpointResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API接口详情Client
 *
 * @author vains
 */
@Validated
@RequestMapping("/api-endpoint")
@Tag(name = "API接口生成结构化数据", description = "API接口自动化扫描相关接口")
@FeignClient(name = FeignConstants.SYSTEM_APPLICATION, path = FeignConstants.SYSTEM_CONTEXT_PATH, contextId = "SysApiEndpointClient")
public interface SysApiEndpointClient {

    /**
     * 分页查询API接口列表
     *
     * @param request 分页查询API接口列表入参
     * @return API接口信息
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询API接口列表")
    Result<DataPageResult<FindApiEndpointResponse>> findByPage(@Valid @SpringQueryMap FindApiEndpointPageRequest request);

    /**
     * 查询API接口详情
     *
     * @param id 接口id
     * @return API接口信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询API接口详情")
    Result<FindApiEndpointResponse> endpointDetails(@Parameter(description = "接口id") @PathVariable Long id);

    /**
     * 添加API接口信息
     *
     * @param request API接口信息
     * @return 操作结果
     */
    @PostMapping
    @Operation(summary = "添加API接口信息")
    Result<String> insertApiEndpoint(@RequestBody SaveApiEndpointRequest request);

    /**
     * 修改API接口信息
     *
     * @param request API接口信息
     * @return 操作结果
     */
    @PutMapping
    @Operation(summary = "修改API接口信息")
    Result<String> updateApiEndpoint(@RequestBody SaveApiEndpointRequest request);

    /**
     * 删除API接口信息
     *
     * @param id 接口id
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除API接口信息")
    Result<String> removeById(@Parameter(description = "接口id") @PathVariable Long id);

    /**
     * 查询API接口信息列表
     *
     * @param request 查询API接口信息列表入参
     * @return API接口信息
     */
    @PostMapping("/list")
    @Operation(summary = "查询API接口信息列表")
    Result<List<FindApiEndpointResponse>> findApiEndpoints(@RequestBody FindApiEndpointRequest request);

    /**
     * 根据扫描批次ID查询接口列表
     *
     * @param scanBatchId 扫描批次ID
     * @return 接口列表
     */
    @GetMapping("/batch/{scanBatchId}")
    @Operation(summary = "根据扫描批次ID查询接口列表")
    Result<List<FindApiEndpointResponse>> findByScanBatchId(@Parameter(description = "扫描批次ID") @PathVariable Long scanBatchId);

    /**
     * 扫描系统接口
     *
     * @param applications 应用名称
     * @return 扫描批次ID
     */
    @Operation(summary = "扫描系统接口")
    @PostMapping("/scan/endpoints")
    Result<Long> scanApiEndpoints(@Parameter(description = "应用名称") @RequestBody List<String> applications);

    /**
     * 批量导入接口到权限表
     *
     * @param endpointIds 接口ID列表
     * @return 操作结果
     */
    @PostMapping("/import")
    @Operation(summary = "批量导入接口到权限表")
    Result<String> importToPermissions(@RequestBody List<Long> endpointIds);

    /**
     * 根据扫描批次ID导入接口
     *
     * @param scanBatchId 扫描批次ID
     * @return 操作结果
     */
    @Operation(summary = "根据扫描批次ID导入接口")
    @PostMapping("/import/batch/{scanBatchId}")
    Result<String> importByScanBatchId(@PathVariable Long scanBatchId);

    /**
     * 设置接口状态为忽略
     *
     * @param endpointIds 接口ID列表
     * @return 操作结果
     */
    @PutMapping("/ignore")
    @Operation(summary = "设置接口状态为忽略")
    Result<String> ignoreToPermissions(@RequestBody List<Long> endpointIds);

}
