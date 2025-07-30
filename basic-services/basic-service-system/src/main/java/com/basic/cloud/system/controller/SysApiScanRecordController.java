package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.SysApiScanRecordClient;
import com.basic.cloud.system.api.domain.request.FindApiScanRecordPageRequest;
import com.basic.cloud.system.api.domain.response.FindApiScanRecordResponse;
import com.basic.cloud.system.service.SysApiScanRecordService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * API扫描记录相关接口
 *
 * @author vains
 */
@RestController
@RequiredArgsConstructor
public class SysApiScanRecordController implements SysApiScanRecordClient {

    private final SysApiScanRecordService scanRecordService;

    @Override
    public Result<DataPageResult<FindApiScanRecordResponse>> findByPage(FindApiScanRecordPageRequest request) {
        DataPageResult<FindApiScanRecordResponse> pageResult = scanRecordService.findByPage(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<FindApiScanRecordResponse> recordDetails(Long id) {
        FindApiScanRecordResponse response = scanRecordService.recordDetails(id);
        return Result.success(response);
    }

    @Override
    public Result<String> removeById(Long id) {
        scanRecordService.removeById(id);
        return Result.success();
    }
}
