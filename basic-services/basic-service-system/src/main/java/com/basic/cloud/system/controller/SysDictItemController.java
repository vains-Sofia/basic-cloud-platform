
package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.SysDictItemClient;
import com.basic.cloud.system.api.domain.request.SysDictItemPageRequest;
import com.basic.cloud.system.api.domain.request.SysDictItemRequest;
import com.basic.cloud.system.api.domain.response.FindSysDictItemResponse;
import com.basic.cloud.system.service.SysDictItemService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 字典项Controller
 *
 * @author vains
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class SysDictItemController implements SysDictItemClient {

    private final SysDictItemService sysDictItemService;

    @Override
    public Result<DataPageResult<FindSysDictItemResponse>> pageQuery(SysDictItemPageRequest request) {
        DataPageResult<FindSysDictItemResponse> pageResult = sysDictItemService.pageQuery(request);
        return Result.success(pageResult);
    }

    @Override
    public Result<List<FindSysDictItemResponse>> listByType(@PathVariable String typeCode) {
        List<FindSysDictItemResponse> responses = sysDictItemService.listByType(typeCode);
        return Result.success(responses);
    }

    @Override
    public Result<FindSysDictItemResponse> getById(@PathVariable Long id) {
        FindSysDictItemResponse response = sysDictItemService.getById(id);
        return Result.success(response);
    }

    @Override
    public Result<FindSysDictItemResponse> create(@Valid @RequestBody SysDictItemRequest request) {
        FindSysDictItemResponse response = sysDictItemService.create(request);
        return Result.success(response);
    }

    @Override
    public Result<FindSysDictItemResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody SysDictItemRequest request) {
        FindSysDictItemResponse response = sysDictItemService.update(id, request);
        return Result.success(response);
    }

    @Override
    public Result<String> delete(@PathVariable Long id) {
        sysDictItemService.delete(id);
        return Result.success("删除成功");
    }
}
