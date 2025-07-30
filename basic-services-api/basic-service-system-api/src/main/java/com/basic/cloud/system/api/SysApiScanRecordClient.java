package com.basic.cloud.system.api;

import com.basic.cloud.system.api.domain.request.FindApiScanRecordPageRequest;
import com.basic.cloud.system.api.domain.response.FindApiScanRecordResponse;
import com.basic.framework.core.constants.FeignConstants;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * API扫描记录Client
 *
 * @author vains
 */
@RequestMapping("/api-scan")
@Tag(name = "API接口生成结构化数据", description = "API接口自动化扫描相关接口")
@FeignClient(name = FeignConstants.SYSTEM_APPLICATION, path = FeignConstants.SYSTEM_CONTEXT_PATH, contextId = "SysApiScanRecordClient")
public interface SysApiScanRecordClient {

    /**
     * 分页查询API扫描记录列表
     *
     * @param request 分页查询API扫描记录列表入参
     * @return API扫描记录信息
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询API扫描记录列表")
    Result<DataPageResult<FindApiScanRecordResponse>> findByPage(@Valid FindApiScanRecordPageRequest request);

    /**
     * 查询API扫描记录详情
     *
     * @param id 记录id
     * @return API扫描记录信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询API扫描记录详情")
    Result<FindApiScanRecordResponse> recordDetails(@Parameter(description = "记录id") @PathVariable Long id);

    /**
     * 删除API扫描记录信息
     *
     * @param id 记录id
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除API扫描记录信息")
    Result<String> removeById(@Parameter(description = "记录id") @PathVariable Long id);
}
