package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.SysDictTypePageRequest;
import com.basic.cloud.system.api.domain.request.SysDictTypeRequest;
import com.basic.cloud.system.api.domain.response.FindSysDictTypeResponse;
import com.basic.cloud.system.converter.SysDictTypeConverter;
import com.basic.cloud.system.domain.SysDictItem;
import com.basic.cloud.system.domain.SysDictType;
import com.basic.cloud.system.repository.SysDictItemRepository;
import com.basic.cloud.system.repository.SysDictTypeRepository;
import com.basic.cloud.system.service.SysDictTypeService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.core.util.Sequence;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

/**
 * 字典类型Service实现
 *
 * @author vains
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl implements SysDictTypeService {

    private final Sequence sequence = new Sequence((null));

    private final SysDictTypeConverter sysDictTypeConverter;

    private final SysDictTypeRepository sysDictTypeRepository;

    private final SysDictItemRepository sysDictItemRepository;

    @Override
    public List<FindSysDictTypeResponse> listAll() {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(SysDictType::getCreateTime));
        List<SysDictType> dictTypes = sysDictTypeRepository.findAll(sort);
        return sysDictTypeConverter.convertToResponseList(dictTypes);
    }

    @Override
    public FindSysDictTypeResponse getById(Long id) {
        SysDictType dictType = sysDictTypeRepository.findById(id)
                .orElseThrow(() -> new CloudServiceException("字典类型不存在，ID：" + id));
        return sysDictTypeConverter.convertToResponse(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FindSysDictTypeResponse create(SysDictTypeRequest request) {
        // 检查字典类型编码是否已存在
        Optional<SysDictType> existingDictType = sysDictTypeRepository.findByTypeCode(request.getTypeCode());
        if (existingDictType.isPresent()) {
            throw new CloudServiceException("字典类型编码已存在：" + request.getTypeCode());
        }

        SysDictType dictType = sysDictTypeConverter.convertFromRequest(request);
        dictType.setId(sequence.nextId());
        SysDictType savedDictType = sysDictTypeRepository.save(dictType);

        if (log.isDebugEnabled()) {
            log.debug("创建字典类型成功，ID：{}，类型编码：{}", savedDictType.getId(), savedDictType.getTypeCode());
        }

        return sysDictTypeConverter.convertToResponse(savedDictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FindSysDictTypeResponse update(Long id, SysDictTypeRequest request) {
        SysDictType existingDictType = sysDictTypeRepository.findById(id)
                .orElseThrow(() -> new CloudServiceException("字典类型不存在，ID：" + id));

        // 检查字典类型编码是否已被其他记录使用
        Optional<SysDictType> dictTypeByCode = sysDictTypeRepository.findByTypeCode(request.getTypeCode());
        if (dictTypeByCode.isPresent() && !dictTypeByCode.get().getId().equals(id)) {
            throw new CloudServiceException("字典类型编码已存在：" + request.getTypeCode());
        }

        // 更新字典类型信息
        existingDictType.setTypeCode(request.getTypeCode());
        existingDictType.setName(request.getName());
        existingDictType.setDescription(request.getDescription());
        existingDictType.setStatus(request.getStatus());

        SysDictType updatedDictType = sysDictTypeRepository.save(existingDictType);

        if (log.isDebugEnabled()) {
            log.debug("更新字典类型，ID：{}，类型编码：{}", updatedDictType.getId(), updatedDictType.getTypeCode());
        }

        // 更新相关的字典项等
        List<SysDictItem> dictItems = this.sysDictItemRepository.findByTypeCodeOrderBySortOrderAsc(existingDictType.getTypeCode());

        if (dictItems.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("没有找到相关的字典项，类型编码：{}", updatedDictType.getTypeCode());
            }
            return sysDictTypeConverter.convertToResponse(updatedDictType);
        }

        // 如果字典项存在，更新它们的类型编码
        for (SysDictItem item : dictItems) {
            item.setTypeCode(updatedDictType.getTypeCode());
        }

        this.sysDictItemRepository.saveAll(dictItems);

        if (log.isDebugEnabled()) {
            log.debug("更新字典类型相关的字典项，类型编码：{}，数量：{}", updatedDictType.getTypeCode(), dictItems.size());
        }

        return sysDictTypeConverter.convertToResponse(updatedDictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysDictType dictType = sysDictTypeRepository.findById(id)
                .orElseThrow(() -> new CloudServiceException("字典类型不存在，ID：" + id));

        sysDictTypeRepository.deleteById(id);

        if (log.isDebugEnabled()) {
            log.debug("删除字典类型，ID：{}，类型编码：{}", id, dictType.getTypeCode());
        }

        // 删除相关的字典项等
        this.sysDictItemRepository.deleteByTypeCode(dictType.getTypeCode());
        if (log.isDebugEnabled()) {
            log.debug("删除字典类型相关的字典项，类型编码：{}", dictType.getTypeCode());
        }
    }

    @Override
    public FindSysDictTypeResponse getByTypeCode(String typeCode) {
        SysDictType dictType = sysDictTypeRepository.findByTypeCode(typeCode)
                .orElseThrow(() -> new CloudServiceException("字典类型不存在，类型编码：" + typeCode));
        return sysDictTypeConverter.convertToResponse(dictType);
    }

    @Override
    public DataPageResult<FindSysDictTypeResponse> pageQuery(SysDictTypePageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(SysDictType::getCreateTime));
        // 分页
        PageRequest pageQuery = PageRequest.of(request.current(), request.size(), sort);

        // 条件构造器
        SpecificationBuilder<SysDictType> builder = new SpecificationBuilder<>();
        builder.or(or -> {
            if (!ObjectUtils.isEmpty(request.getKeyword())) {
                or.like(SysDictType::getName, request.getKeyword());
                or.like(SysDictType::getTypeCode, request.getKeyword());
            }
        });

        Page<SysDictType> dictTypesPage = this.sysDictTypeRepository.findAll(builder, pageQuery);
        // 转换响应结果
        List<FindSysDictTypeResponse> itemResponses = dictTypesPage.getContent()
                .stream()
                .map(sysDictTypeConverter::convertToResponse)
                .toList();

        return DataPageResult.of(dictTypesPage.getNumber(), dictTypesPage.getSize(), dictTypesPage.getTotalElements(), itemResponses);
    }
}
