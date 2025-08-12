package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.SysApiEndpointClient;
import com.basic.cloud.system.api.domain.request.FindApiEndpointPageRequest;
import com.basic.cloud.system.api.domain.request.FindApiEndpointRequest;
import com.basic.cloud.system.api.domain.request.SaveApiEndpointRequest;
import com.basic.cloud.system.api.domain.response.FindApiEndpointResponse;
import com.basic.cloud.system.service.SysApiEndpointService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API接口详情相关接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class SysApiEndpointController implements SysApiEndpointClient {

    private final SysApiEndpointService apiEndpointService;

    @Override
    public Result<DataPageResult<FindApiEndpointResponse>> findByPage(FindApiEndpointPageRequest request) {
        DataPageResult<FindApiEndpointResponse> pageResult = apiEndpointService.findByPage(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<FindApiEndpointResponse> endpointDetails(Long id) {
        FindApiEndpointResponse response = apiEndpointService.endpointDetails(id);
        return Result.success(response);
    }

    @Override
    public Result<String> insertApiEndpoint(SaveApiEndpointRequest request) {
        // 置空id，防止插入变修改
        request.setId(null);
        apiEndpointService.saveApiEndpoint(request);
        return Result.success();
    }

    @Override
    public Result<String> updateApiEndpoint(SaveApiEndpointRequest request) {
        apiEndpointService.saveApiEndpoint(request);
        return Result.success();
    }

    @Override
    public Result<String> removeById(Long id) {
        apiEndpointService.removeById(id);
        return Result.success();
    }

    @Override
    public Result<List<FindApiEndpointResponse>> findApiEndpoints(FindApiEndpointRequest request) {
        List<FindApiEndpointResponse> endpoints = apiEndpointService.findApiEndpoints(request);
        return Result.success(endpoints);
    }

    @Override
    public Result<List<FindApiEndpointResponse>> findByScanBatchId(Long scanBatchId) {
        List<FindApiEndpointResponse> endpoints = apiEndpointService.findByScanBatchId(scanBatchId);
        return Result.success(endpoints);
    }

    @Override
    public Result<Long> scanApiEndpoints(List<String> applications) {
        Long scanBatchId = apiEndpointService.scanApiEndpoints(applications);
        return Result.success(scanBatchId);
    }

    @Override
    public Result<String> importToPermissions(List<Long> endpointIds) {
        apiEndpointService.importToPermissions(endpointIds);
        return Result.success();
    }

    @Override
    public Result<String> importByScanBatchId(Long scanBatchId) {
        apiEndpointService.importByScanBatchId(scanBatchId);
        return Result.success();
    }

    @Override
    public Result<String> ignoreToPermissions(List<Long> endpointIds) {
        apiEndpointService.ignoreToPermissions(endpointIds);
        return Result.success();
    }
}
