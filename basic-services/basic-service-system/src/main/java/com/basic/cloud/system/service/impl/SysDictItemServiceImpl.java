package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.SysDictItemPageRequest;
import com.basic.cloud.system.api.domain.request.SysDictItemRequest;
import com.basic.cloud.system.api.domain.response.FindSysDictItemResponse;
import com.basic.cloud.system.api.enums.StatusEnum;
import com.basic.cloud.system.converter.SysDictItemConverter;
import com.basic.cloud.system.domain.SysDictItem;
import com.basic.cloud.system.domain.SysDictType;
import com.basic.cloud.system.repository.SysDictItemRepository;
import com.basic.cloud.system.repository.SysDictTypeRepository;
import com.basic.cloud.system.service.SysDictItemService;
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
 * 字典项Service实现
 *
 * @author vains
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictItemServiceImpl implements SysDictItemService {

    private final Sequence sequence = new Sequence((null));

    private final SysDictItemConverter sysDictItemConverter;

    private final SysDictItemRepository sysDictItemRepository;

    private final SysDictTypeRepository sysDictTypeRepository;

    @Override
    public List<FindSysDictItemResponse> listAll() {
        List<SysDictItem> dictItems = sysDictItemRepository.findAll();
        return sysDictItemConverter.convertToResponseList(dictItems);
    }

    @Override
    public List<FindSysDictItemResponse> listByType(String typeCode) {
        // 验证字典类型是否存在
        SysDictType dictType = sysDictTypeRepository.findByTypeCode(typeCode)
                .orElseThrow(() -> new CloudServiceException("字典类型不存在，类型编码：" + typeCode));

        List<SysDictItem> dictItems = sysDictItemRepository.findByTypeCodeAndStatusOrderBySortOrderAsc(dictType.getTypeCode(), StatusEnum.ENABLE);
        return sysDictItemConverter.convertToResponseList(dictItems);
    }

    @Override
    public FindSysDictItemResponse getById(Long id) {
        SysDictItem dictItem = sysDictItemRepository.findById(id)
                .orElseThrow(() -> new CloudServiceException("字典项不存在，ID：" + id));
        return sysDictItemConverter.convertToResponse(dictItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FindSysDictItemResponse create(SysDictItemRequest request) {
        // 验证字典类型是否存在
        SysDictType dictType = sysDictTypeRepository.findByTypeCode(request.getTypeCode())
                .orElseThrow(() -> new CloudServiceException("字典类型不存在，类型编码：" + request.getTypeCode()));

        // 检查同一字典类型下是否存在相同的字典项键
        List<SysDictItem> existingItems = sysDictItemRepository.findByTypeCodeOrderBySortOrderAsc(dictType.getTypeCode());
        boolean keyExists = existingItems.stream()
                .anyMatch(item -> item.getItemCode().equals(request.getItemCode()));

        if (keyExists) {
            throw new CloudServiceException("字典项在类型 [ " + dictType.getTypeCode() + " ] 中已存在：" + request.getItemCode());
        }

        SysDictItem dictItem = sysDictItemConverter.convertFromRequest(request);
        dictItem.setId(sequence.nextId());
        SysDictItem savedDictItem = sysDictItemRepository.save(dictItem);

        if (log.isDebugEnabled()) {
            log.debug("创建字典项成功，ID：{}，字典类型：{}，字典项键：{}",
                    savedDictItem.getId(), savedDictItem.getTypeCode(), savedDictItem.getItemCode());
        }

        return sysDictItemConverter.convertToResponse(savedDictItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FindSysDictItemResponse update(Long id, SysDictItemRequest request) {
        SysDictItem existingDictItem = sysDictItemRepository.findById(id)
                .orElseThrow(() -> new CloudServiceException("字典项不存在，ID：" + id));

        // 验证字典类型是否存在
        Optional<SysDictType> dictType = sysDictTypeRepository.findByTypeCode(request.getTypeCode());
        if (dictType.isEmpty()) {
            throw new CloudServiceException("字典类型不存在，类型编码：" + request.getTypeCode());
        }

        // 检查同一字典类型下是否存在相同的字典项键（排除当前记录）
        List<SysDictItem> existingItems = sysDictItemRepository.findByTypeCodeOrderBySortOrderAsc(request.getTypeCode());
        boolean keyExists = existingItems.stream()
                .anyMatch(item -> item.getItemCode().equals(request.getItemCode()) && !item.getId().equals(id));

        if (keyExists) {
            throw new CloudServiceException("字典项键已存在：" + request.getItemCode());
        }

        // 更新字典项信息
        existingDictItem.setTypeCode(request.getTypeCode());
        existingDictItem.setItemCode(request.getItemCode());
        existingDictItem.setItemName(request.getItemName());
        existingDictItem.setSortOrder(request.getSortOrder());
        existingDictItem.setStatus(request.getStatus());
        existingDictItem.setI18nJson(request.getI18nJson());

        SysDictItem updatedDictItem = sysDictItemRepository.save(existingDictItem);

        if (log.isDebugEnabled()) {
            log.debug("更新字典项成功，ID：{}，字典类型：{}，字典项键：{}",
                    updatedDictItem.getId(), updatedDictItem.getTypeCode(), updatedDictItem.getItemCode());
        }

        return sysDictItemConverter.convertToResponse(updatedDictItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysDictItem dictItem = sysDictItemRepository.findById(id)
                .orElseThrow(() -> new CloudServiceException("字典项不存在，ID：" + id));

        sysDictItemRepository.deleteById(id);

        if (log.isDebugEnabled()) {
            log.debug("删除字典项成功，ID：{}，字典项键：{}", id, dictItem.getItemCode());
        }
    }

    @Override
    public FindSysDictItemResponse getByTypeCodeAndKey(String typeCode, String itemKey) {
        // 验证字典类型是否存在
        SysDictType dictType = sysDictTypeRepository.findByTypeCode(typeCode)
                .orElseThrow(() -> new CloudServiceException("字典类型不存在，类型编码：" + typeCode));

        List<SysDictItem> dictItems = sysDictItemRepository.findByTypeCodeOrderBySortOrderAsc(dictType.getTypeCode());
        SysDictItem dictItem = dictItems.stream()
                .filter(item -> item.getItemCode().equals(itemKey))
                .findFirst()
                .orElseThrow(() -> new CloudServiceException("字典项不存在，类型编码：" + typeCode + "，键：" + itemKey));

        return sysDictItemConverter.convertToResponse(dictItem);
    }

    @Override
    public DataPageResult<FindSysDictItemResponse> pageQuery(SysDictItemPageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.ASC, LambdaUtils.extractMethodToProperty(SysDictItem::getSortOrder));
        // 分页
        PageRequest pageQuery = PageRequest.of(request.current(), request.size(), sort);

        // 条件构造器
        SpecificationBuilder<SysDictItem> builder = new SpecificationBuilder<>();
        builder.eq(!ObjectUtils.isEmpty(request.getStatus()), SysDictItem::getStatus, request.getStatus());
        builder.eq(!ObjectUtils.isEmpty(request.getTypeCode()), SysDictItem::getTypeCode, request.getTypeCode());
        builder.or(or -> {
            if (!ObjectUtils.isEmpty(request.getKeyword())) {
                or.like(SysDictItem::getItemCode, request.getKeyword());
                or.like(SysDictItem::getItemName, request.getKeyword());
            }
        });

        // 分页查询
        Page<SysDictItem> dictItemPage = sysDictItemRepository.findAll(builder, pageQuery);

        // 转换响应结果
        List<FindSysDictItemResponse> itemResponses = dictItemPage.getContent()
                .stream()
                .map(sysDictItemConverter::convertToResponse)
                .toList();

        return DataPageResult.of(dictItemPage.getNumber(), dictItemPage.getSize(), dictItemPage.getTotalElements(), itemResponses);
    }
}
