package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.FindApiScanRecordPageRequest;
import com.basic.cloud.system.api.domain.response.FindApiScanRecordResponse;
import com.basic.cloud.system.converter.SysApiScanRecordConverter;
import com.basic.cloud.system.domain.SysApiScanRecord;
import com.basic.cloud.system.repository.SysApiScanRecordRepository;
import com.basic.cloud.system.service.SysApiScanRecordService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * API扫描记录 Service 实现
 *
 * @author vains
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysApiScanRecordServiceImpl implements SysApiScanRecordService {

    private final SysApiScanRecordRepository apiScanRecordRepository;

    private final SysApiScanRecordConverter apiScanRecordConverter = new SysApiScanRecordConverter();

    @Override
    public DataPageResult<FindApiScanRecordResponse> findByPage(FindApiScanRecordPageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(SysApiScanRecord::getScanTime));
        // 分页
        PageRequest pageQuery = PageRequest.of(request.current(), request.size(), sort);

        // 条件构造器
        SpecificationBuilder<SysApiScanRecord> builder = new SpecificationBuilder<>();
        builder.between(request.getStartTime() != null && request.getEndTime() != null,
                SysApiScanRecord::getScanTime, request.getStartTime(), request.getEndTime());

        // 执行查询
        Page<SysApiScanRecord> findPageResult = apiScanRecordRepository.findAll(builder, pageQuery);
        // 转为响应bean
        List<FindApiScanRecordResponse> recordsList = findPageResult.getContent()
                .stream()
                .map(apiScanRecordConverter::toResponse)
                .toList();

        return DataPageResult.of(findPageResult.getNumber(), findPageResult.getSize(), findPageResult.getTotalElements(), recordsList);
    }

    @Override
    public FindApiScanRecordResponse recordDetails(Long id) {
        if (id == null) {
            throw new CloudIllegalArgumentException("扫描记录ID不能为空");
        }

        SysApiScanRecord record = apiScanRecordRepository.findById(id)
                .orElseThrow(() -> new CloudServiceException("API扫描记录不存在，ID：" + id));

        return apiScanRecordConverter.toResponse(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        if (id == null) {
            throw new CloudIllegalArgumentException("扫描记录ID不能为空");
        }

        SysApiScanRecord record = apiScanRecordRepository.findById(id)
                .orElseThrow(() -> new CloudServiceException("API扫描记录不存在，ID：" + id));

        apiScanRecordRepository.deleteById(id);

        if (log.isDebugEnabled()) {
            log.debug("删除API扫描记录成功，ID：{}，扫描时间：{}", id, record.getScanTime());
        }
    }
}