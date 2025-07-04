
package com.basic.cloud.system.controller;

import com.basic.cloud.system.api.SysDictTypeClient;
import com.basic.cloud.system.api.domain.request.SysDictTypePageRequest;
import com.basic.cloud.system.api.domain.request.SysDictTypeRequest;
import com.basic.cloud.system.api.domain.response.FindSysDictTypeResponse;
import com.basic.cloud.system.service.SysDictTypeService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典类型Controller
 *
 * @author vains
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class SysDictTypeController implements SysDictTypeClient {

    private final SysDictTypeService sysDictTypeService;

    @Override
    public Result<List<FindSysDictTypeResponse>> listAll() {
        List<FindSysDictTypeResponse> responses = sysDictTypeService.listAll();
        return Result.success(responses);
    }

    @Override
    public Result<DataPageResult<FindSysDictTypeResponse>> pageQuery(SysDictTypePageRequest request) {
        DataPageResult<FindSysDictTypeResponse> responses = sysDictTypeService.pageQuery(request);
        return Result.success(responses);
    }

    @Override
    public Result<FindSysDictTypeResponse> getById(@PathVariable Long id) {
        FindSysDictTypeResponse response = sysDictTypeService.getById(id);
        return Result.success(response);
    }

    @Override
    public Result<FindSysDictTypeResponse> create(@Valid @RequestBody SysDictTypeRequest request) {
        FindSysDictTypeResponse response = sysDictTypeService.create(request);
        return Result.success(response);
    }

    @Override
    public Result<FindSysDictTypeResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody SysDictTypeRequest request) {
        FindSysDictTypeResponse response = sysDictTypeService.update(id, request);
        return Result.success(response);
    }

    @Override
    public Result<String> delete(@PathVariable Long id) {
        sysDictTypeService.delete(id);
        return Result.success();
    }
}
