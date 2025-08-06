package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.DocsApiClient;
import com.basic.cloud.system.api.domain.request.FindApiEndpointPageRequest;
import com.basic.cloud.system.api.domain.request.FindApiEndpointRequest;
import com.basic.cloud.system.api.domain.request.SaveApiEndpointRequest;
import com.basic.cloud.system.api.domain.response.FindApiEndpointResponse;
import com.basic.cloud.system.api.domain.response.FindSysDictItemResponse;
import com.basic.cloud.system.api.enums.ScanStatusEnum;
import com.basic.cloud.system.converter.ApiPathConverter;
import com.basic.cloud.system.converter.SysApiEndpointConverter;
import com.basic.cloud.system.domain.SysApiEndpoint;
import com.basic.cloud.system.domain.SysApiScanRecord;
import com.basic.cloud.system.domain.SysPermission;
import com.basic.cloud.system.repository.SysApiEndpointRepository;
import com.basic.cloud.system.repository.SysApiScanRecordRepository;
import com.basic.cloud.system.repository.SysPermissionRepository;
import com.basic.cloud.system.service.SysApiEndpointService;
import com.basic.cloud.system.service.SysDictItemService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.core.util.JsonUtils;
import com.basic.framework.core.util.Sequence;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.core.enums.PermissionTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.providers.SpringDocProviders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * API接口详情 Service 实现
 *
 * @author vains
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysApiEndpointServiceImpl implements SysApiEndpointService {

    private final DocsApiClient docsApiClient;

    private final SysDictItemService dictItemService;

    private final SpringDocProviders springDocProviders;

    private final Sequence sequence = new Sequence((null));

    private final SysPermissionRepository permissionRepository;

    private final SysApiEndpointRepository apiEndpointRepository;

    private final SysApiScanRecordRepository apiScanRecordRepository;

    private final SysApiEndpointConverter apiEndpointConverter = new SysApiEndpointConverter();

    @Override
    public DataPageResult<FindApiEndpointResponse> findByPage(FindApiEndpointPageRequest request) {
        // 排序
        Sort sort = Sort.by(LambdaUtils.extractMethodToProperty(SysApiEndpoint::getPath));
        // 分页
        PageRequest pageQuery = PageRequest.of(request.current(), request.size(), sort);

        // 条件构造器
        SpecificationBuilder<SysApiEndpoint> builder = new SpecificationBuilder<>();
        // 如果keyword不为空，则添加模糊查询条件(路径、权限码或标题)
        builder.or(or ->
                or.like(!ObjectUtils.isEmpty(request.getKeyword()), SysApiEndpoint::getPath, request.getKeyword())
                .like(!ObjectUtils.isEmpty(request.getKeyword()), SysApiEndpoint::getPermission, request.getKeyword())
                .like(!ObjectUtils.isEmpty(request.getKeyword()), SysApiEndpoint::getTitle, request.getKeyword())
        );
        builder.eq(!ObjectUtils.isEmpty(request.getRequestMethod()), SysApiEndpoint::getRequestMethod, request.getRequestMethod());
        builder.like(!ObjectUtils.isEmpty(request.getModuleName()), SysApiEndpoint::getModuleName, request.getModuleName());
        builder.eq(request.getScanStatus() != null, SysApiEndpoint::getScanStatus, request.getScanStatus());
        builder.eq(request.getScanBatchId() != null, SysApiEndpoint::getScanBatchId, request.getScanBatchId());
        builder.eq(request.getImported() != null, SysApiEndpoint::getImported, request.getImported());

        // 执行查询
        Page<SysApiEndpoint> findPageResult = apiEndpointRepository.findAll(builder, pageQuery);
        // 转为响应bean
        List<FindApiEndpointResponse> endpointsList = findPageResult.getContent()
                .stream()
                .map(apiEndpointConverter::toResponse)
                .toList();

        return DataPageResult.of(findPageResult.getNumber(), findPageResult.getSize(), findPageResult.getTotalElements(), endpointsList);
    }

    @Override
    public FindApiEndpointResponse endpointDetails(Long id) {
        if (id == null) {
            throw new CloudIllegalArgumentException("接口ID不能为空");
        }

        SysApiEndpoint endpoint = apiEndpointRepository.findById(id)
                .orElseThrow(() -> new CloudServiceException("API接口不存在，ID：" + id));

        return apiEndpointConverter.toResponse(endpoint);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveApiEndpoint(SaveApiEndpointRequest request) {
        // id是否为空，不为空是修改
        boolean hasId = request.getId() != null;

        // 检查同一批次下路径和请求方法的组合是否已存在
        if (!hasId || !apiEndpointRepository.existsById(request.getId())) {
            boolean exists = apiEndpointRepository.existsByScanBatchIdAndPathAndRequestMethod(
                    request.getScanBatchId(), request.getPath(), request.getRequestMethod());
            if (exists) {
                throw new CloudIllegalArgumentException("同一批次下该接口路径和请求方法的组合已存在");
            }
        }

        SysApiEndpoint endpoint;
        if (!hasId) {
            // 新增
            endpoint = apiEndpointConverter.toEntity(request);
            endpoint.setId(sequence.nextId());
            endpoint.setImported(Boolean.FALSE);
        } else {
            // 修改
            endpoint = apiEndpointRepository.findById(request.getId())
                    .orElseThrow(() -> new CloudServiceException("API接口不存在，ID：" + request.getId()));

            // 更新字段
            apiEndpointConverter.updateFromRequest(endpoint, request);
        }

        apiEndpointRepository.save(endpoint);

        if (log.isDebugEnabled()) {
            log.debug("{}API接口成功，ID：{}，路径：{}，方法：{}",
                    hasId ? "更新" : "创建", endpoint.getId(), endpoint.getPath(), endpoint.getRequestMethod());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        if (id == null) {
            throw new CloudIllegalArgumentException("接口ID不能为空");
        }

        SysApiEndpoint endpoint = apiEndpointRepository.findById(id)
                .orElseThrow(() -> new CloudServiceException("API接口不存在，ID：" + id));

        apiEndpointRepository.deleteById(id);

        if (log.isDebugEnabled()) {
            log.debug("删除API接口成功，ID：{}，路径：{}", id, endpoint.getPath());
        }
    }

    @Override
    public List<FindApiEndpointResponse> findApiEndpoints(FindApiEndpointRequest request) {
        // 条件构造器
        SpecificationBuilder<SysApiEndpoint> builder = new SpecificationBuilder<>();
        builder.like(!ObjectUtils.isEmpty(request.getPath()), SysApiEndpoint::getPath, request.getPath());
        builder.eq(!ObjectUtils.isEmpty(request.getRequestMethod()), SysApiEndpoint::getRequestMethod, request.getRequestMethod());
        builder.eq(request.getScanBatchId() != null, SysApiEndpoint::getScanBatchId, request.getScanBatchId());
        builder.eq(request.getScanStatus() != null, SysApiEndpoint::getScanStatus, request.getScanStatus());

        // 执行查询
        List<SysApiEndpoint> findResult = apiEndpointRepository.findAll(builder);
        // 转为响应bean
        return findResult.stream()
                .map(apiEndpointConverter::toResponse)
                .toList();
    }

    @Override
    public List<FindApiEndpointResponse> findByScanBatchId(Long scanBatchId) {
        if (scanBatchId == null) {
            throw new CloudIllegalArgumentException("扫描批次ID不能为空");
        }

        List<SysApiEndpoint> endpoints = apiEndpointRepository.findByScanBatchId(scanBatchId);
        return endpoints.stream()
                .map(apiEndpointConverter::toResponse)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long scanApiEndpoints(List<String> applications) {
        List<FindSysDictItemResponse> dictItems;
        String dictModuleName = "MODULE";
        if (ObjectUtils.isEmpty(applications)) {
            // 如果没有传入应用名称，则查询所有字典项
            dictItems = dictItemService.listByType(dictModuleName);
        } else {
            // 如果传入了应用名称，则根据应用名称过滤字典项
            List<FindSysDictItemResponse> responses = dictItemService.listByType(dictModuleName);
            // 根据传入的应用名称过滤字典项
            dictItems = responses.stream().filter(response -> applications.contains(response.getItemCode()))
                    .toList();
        }

        if (ObjectUtils.isEmpty(dictItems)) {
            throw new CloudServiceException("请先在字典管理中添加应用名称，应用名称类型为：" + dictModuleName);
        }

        long batchId = sequence.nextId();
        try {
            // 新增权限列表
            List<SysApiEndpoint> apiEndpoints = new ArrayList<>();
            // 扫描每个应用的API接口.
            for (FindSysDictItemResponse dictItem : dictItems) {
                String application = dictItem.getItemCode();
                if (log.isDebugEnabled()) {
                    log.debug("开始扫描应用：{}，批次ID：{}", application, batchId);
                }
                byte[] bytes = docsApiClient.getOpenApi(application);
                if (bytes == null) {
                    throw new CloudIllegalArgumentException("应用[" + application + "]的OpenAPI文档不存在.");
                }
                ObjectMapper objectMapper = springDocProviders.jsonMapper();
                OpenAPI openApi;
                try {
                    openApi = objectMapper.readValue(bytes, OpenAPI.class);
                } catch (Exception e) {
                    throw new CloudIllegalArgumentException("应用[" + application + "]的OpenAPI文档解析失败: " + e.getMessage());
                }
                // 数据库现有数据
                List<SysPermission> permissions = permissionRepository.findByModuleName(application);
                // 构建已有权限的唯一键集合，格式为 path|method
                Set<String> existingKeys = permissions.stream()
                        .map(p -> p.getPath() + "|" + p.getRequestMethod())
                        .collect(Collectors.toSet());

                // 遍历OpenAPI文档中的路径
                openApi.getPaths().forEach((path, pathItem) -> {
                    Map<String, Operation> ops = new LinkedHashMap<>();
                    if (pathItem.getGet() != null) {
                        ops.put("GET", pathItem.getGet());
                    }
                    if (pathItem.getPost() != null) {
                        ops.put("POST", pathItem.getPost());
                    }
                    if (pathItem.getPut() != null) {
                        ops.put("PUT", pathItem.getPut());
                    }
                    if (pathItem.getDelete() != null) {
                        ops.put("DELETE", pathItem.getDelete());
                    }
                    if (pathItem.getPatch() != null) {
                        ops.put("PATCH", pathItem.getPatch());
                    }
                    ops.forEach((method, operation) -> {
                        // 路径处理为通配符路径
                        String wildcardPath = ApiPathConverter.toWildcardPath(path);

                        // 组装api-endpoint对象
                        SysApiEndpoint endpoint = new SysApiEndpoint();
                        endpoint.setId(sequence.nextId());
                        endpoint.setPath(wildcardPath);
                        endpoint.setRequestMethod(method);
                        endpoint.setTitle(operation.getSummary());
                        endpoint.setPermission(ApiPathConverter.toPermissionCode(path));
                        endpoint.setModuleName(application);
                        ScanStatusEnum status;
                        // 组装唯一键
                        String key = wildcardPath + "|" + method;
                        // 如果没有summary
                        if (ObjectUtils.isEmpty(operation.getSummary())) {
                            status = ScanStatusEnum.MISSING;
                        } else if (existingKeys.contains(key)) {
                            // 如果请求路径与请求方式的组合已存在
                            status = ScanStatusEnum.EXISTING;
                            permissions.stream()
                                    .filter(e -> {
                                        String existingKey = e.getPath() + "|" + e.getRequestMethod();
                                        return existingKey.equals(key);
                                    })
                                    .findFirst()
                                    .ifPresent(existingPermission -> {
                                        // 设置已存在的权限ID
                                        endpoint.setExistingPermissionId(existingPermission.getId());
                                    });
                        } else {
                            status = ScanStatusEnum.NEW;
                        }
                        endpoint.setScanStatus(status);
                        endpoint.setScanBatchId(batchId);
                        apiEndpoints.add(endpoint);
                    });
                });
            }

            String applicationName = dictItems.stream().map(FindSysDictItemResponse::getItemName).collect(Collectors.joining(", "));

            // 保存扫描结果
            if (!apiEndpoints.isEmpty()) {
                apiEndpointRepository.saveAll(apiEndpoints);
                if (log.isDebugEnabled()) {
                    log.debug("API接口扫描完成，应用：{}，批次ID：{}，扫描到接口数量：{}", applicationName, batchId, apiEndpoints.size());
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("API接口扫描完成，应用：{}，批次ID：{}，未扫描到任何接口", applicationName, batchId);
                }
            }
            // 统计新发现的接口数量
            long newCount = apiEndpoints.stream().filter(e -> e.getScanStatus() == ScanStatusEnum.NEW).count();
            // 统计缺少注解的接口数量
            long missingCount = apiEndpoints.stream().filter(e -> e.getScanStatus() == ScanStatusEnum.MISSING).count();
            // 统计已存在的接口数量
            long existCount = apiEndpoints.stream().filter(e -> e.getScanStatus() == ScanStatusEnum.EXISTING).count();

            // 生成日志记录
            SysApiScanRecord scanRecord = new SysApiScanRecord();
            scanRecord.setId(batchId);
            scanRecord.setScanTime(LocalDateTime.now());
            scanRecord.setTotalCount(apiEndpoints.size());
            scanRecord.setNewCount((int) newCount);
            scanRecord.setMissingDescCount((int) missingCount);
            scanRecord.setExistCount((int) existCount);

            String scanResult =
                    """
                            应用：%s，批次ID：%d，扫描到接口数量：%d，新发现接口数量：%d，缺少注解接口数量：%d，已存在接口数量：%d
                            """.formatted(applicationName, batchId, apiEndpoints.size(), newCount, missingCount, existCount);
            scanRecord.setScanResult(scanResult);
            apiScanRecordRepository.save(scanRecord);

            if (log.isDebugEnabled()) {
                log.debug("API接口扫描完成，应用：{}，批次ID：{}", applicationName, batchId);
            }

            return batchId;
        } catch (Exception e) {
            log.error("API接口扫描失败，应用：{}", JsonUtils.toJson(applications), e);
            throw new CloudServiceException("API接口扫描失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importToPermissions(List<Long> endpointIds) {
        if (ObjectUtils.isEmpty(endpointIds)) {
            throw new CloudIllegalArgumentException("接口ID列表不能为空");
        }

        // 检查接口ID列表是否为空
        List<SysApiEndpoint> endpoints = apiEndpointRepository.findAllById(endpointIds);
        if (endpoints.size() != endpointIds.size()) {
            throw new CloudServiceException("部分接口不存在");
        }

        // 过滤出未导入的接口
        List<SysApiEndpoint> unimportedEndpoints = endpoints.stream()
                .filter(endpoint -> !Boolean.TRUE.equals(endpoint.getImported()))
                .toList();

        if (unimportedEndpoints.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("所有接口都已导入，无需重复操作");
            }
            return;
        }

        // 批量查询已存在的权限，避免N+1查询
        Set<String> pathSet = unimportedEndpoints.stream()
                .map(SysApiEndpoint::getPath)
                .collect(Collectors.toSet());

        Set<String> methodSet = unimportedEndpoints.stream()
                .map(SysApiEndpoint::getRequestMethod)
                .collect(Collectors.toSet());

        // 构建查询条件
        SpecificationBuilder<SysPermission> builder = new SpecificationBuilder<>();
        builder.in(SysPermission::getPath, new ArrayList<>(pathSet));
        builder.in(SysPermission::getRequestMethod, new ArrayList<>(methodSet));

        // 查询已存在的权限
        List<SysPermission> existingPermissions = permissionRepository.findAll(builder);

        // 构建已存在权限的快速查找映射
        Set<String> existingPermissionKeys = existingPermissions.stream()
                .map(permission -> permission.getPath() + "|" + permission.getRequestMethod())
                .collect(Collectors.toSet());

        LocalDateTime now = LocalDateTime.now();

        // 导入的权限列表
        List<SysPermission> permissionsToImport = new ArrayList<>();
        // 导入的列表
        List<SysApiEndpoint> endpointsToImport = new ArrayList<>();
        // 处理未导入的接口
        for (SysApiEndpoint endpoint : endpoints) {
            String permissionKey = endpoint.getPath() + "|" + endpoint.getRequestMethod();

            // 如果权限已存在，跳过创建
            if (existingPermissionKeys.contains(permissionKey)) {
                // 设置导入状态
                endpoint.setImportTime(now);
                endpoint.setImported(Boolean.TRUE);
                // 可以选择更新endpoint状态为已存在
                endpoint.setScanStatus(ScanStatusEnum.EXISTING);
                continue;
            }


            // 创建新权限
            SysPermission permission = new SysPermission();
            // 设置接口信息
            permission.setId(sequence.nextId());
            permission.setPath(endpoint.getPath());
            permission.setPermission(endpoint.getPermission());
            permission.setTitle(endpoint.getTitle());
            permission.setRequestMethod(endpoint.getRequestMethod());
            permission.setModuleName(endpoint.getModuleName());

            // 设置其它默认属性
            permission.setName(endpoint.getTitle());
            permission.setPermissionType(PermissionTypeEnum.REST);
            permission.setNeedAuthentication(Boolean.FALSE);
            permission.setRank(99);
            permission.setParentId(0L);
            permission.setDeleted(Boolean.FALSE);

            permissionsToImport.add(permission);

            endpoint.setExistingPermissionId(permission.getId());
            endpoint.setImported(Boolean.TRUE);
            endpoint.setImportTime(now);
            endpointsToImport.add(endpoint);
        }

        if (!endpointsToImport.isEmpty()) {
            apiEndpointRepository.saveAll(endpointsToImport);

            if (log.isDebugEnabled()) {
                log.debug("修改接口扫描记录完成，导入数量：{}", endpointsToImport.size());
            }
        }

        if (!permissionsToImport.isEmpty()) {
            permissionRepository.saveAll(permissionsToImport);

            if (log.isDebugEnabled()) {
                log.debug("批量导入API接口到权限表完成，导入数量：{}", permissionsToImport.size());
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("没有新的API接口需要导入到权限表");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importByScanBatchId(Long scanBatchId) {
        Assert.notNull(scanBatchId, "扫描批次ID不能为空");
        // 查询批次下的所有接口
        List<SysApiEndpoint> endpoints = apiEndpointRepository.findByScanBatchId(scanBatchId);
        if (endpoints.isEmpty()) {
            throw new CloudServiceException("扫描批次ID不存在或没有接口数据，ID：" + scanBatchId);
        }
        // 过滤出未导入的接口
        List<SysApiEndpoint> unimportedEndpoints = endpoints.stream()
                .filter(endpoint -> !endpoint.getImported() && !ObjectUtils.isEmpty(endpoint.getTitle()))
                .toList();

        if (unimportedEndpoints.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("扫描批次ID：{}下的所有接口已导入，无需重复操作", scanBatchId);
            }
            return;
        }

        List<Long> unimportedEndpointIds = unimportedEndpoints.stream()
                .map(SysApiEndpoint::getId)
                .toList();
        // 批量导入接口到权限表
        importToPermissions(unimportedEndpointIds);

    }

    @Override
    public void ignoreToPermissions(List<Long> endpointIds) {
        // 检查接口ID列表是否为空
        if (ObjectUtils.isEmpty(endpointIds)) {
            throw new CloudIllegalArgumentException("接口ID列表不能为空");
        }

        // 查询接口信息
        List<SysApiEndpoint> endpoints = apiEndpointRepository.findAllById(endpointIds);
        if (endpoints.size() != endpointIds.size()) {
            throw new CloudServiceException("部分接口不存在");
        }

        // 过滤出未导入的接口
        List<SysApiEndpoint> unimportedEndpoints = endpoints.stream()
                .filter(endpoint -> !Boolean.TRUE.equals(endpoint.getImported()) && endpoint.getScanStatus() != ScanStatusEnum.IGNORE)
                .toList();

        if (unimportedEndpoints.isEmpty()) {
            throw new CloudServiceException("所有接口都已导入，无需忽略操作");
        }

        // 更新接口状态为已忽略
        for (SysApiEndpoint endpoint : unimportedEndpoints) {
            endpoint.setScanStatus(ScanStatusEnum.IGNORE);
        }

        // 保存更新后的接口信息
        apiEndpointRepository.saveAll(unimportedEndpoints);
    }
}