package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.FindPermissionPageRequest;
import com.basic.cloud.system.api.domain.request.FindPermissionRequest;
import com.basic.cloud.system.api.domain.request.SavePermissionRequest;
import com.basic.cloud.system.api.domain.response.FindPermissionResponse;
import com.basic.cloud.system.domain.SysPermission;
import com.basic.cloud.system.domain.SysRolePermission;
import com.basic.cloud.system.repository.SysPermissionRepository;
import com.basic.cloud.system.repository.SysRolePermissionRepository;
import com.basic.cloud.system.service.SysPermissionService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.core.util.Sequence;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.security.BasicGrantedAuthority;
import com.basic.framework.oauth2.core.enums.PermissionTypeEnum;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * RBAC权限信息Service实现
 *
 * @author vains
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {

    private final Sequence sequence = new Sequence((null));

    private final SysPermissionRepository permissionRepository;

    private final SysRolePermissionRepository rolePermissionRepository;

    private final RedisOperator<Map<String, List<BasicGrantedAuthority>>> redisOperator;

    @Override
    public DataPageResult<FindPermissionResponse> findByPage(FindPermissionPageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(SysPermission::getRank));
        // 分页
        PageRequest pageQuery = PageRequest.of(request.current(), request.size(), sort);

        // 条件构造器
        SpecificationBuilder<SysPermission> builder = new SpecificationBuilder<>();
        builder.like(!ObjectUtils.isEmpty(request.getName()), SysPermission::getName, request.getName());
        builder.like(!ObjectUtils.isEmpty(request.getPermission()), SysPermission::getPermission,
                request.getPermission());
        builder.like(!ObjectUtils.isEmpty(request.getPath()), SysPermission::getPath, request.getPath());
        builder.eq(!ObjectUtils.isEmpty(request.getPermissionType()), SysPermission::getPermissionType,
                request.getPermissionType());

        // 执行查询
        Page<SysPermission> findPageResult = permissionRepository.findAll(builder, pageQuery);
        // 转为响应bean
        List<FindPermissionResponse> permissionResponseList = findPageResult.getContent()
                .stream()
                .map(e -> {
                    FindPermissionResponse permissionResponse = new FindPermissionResponse();
                    BeanUtils.copyProperties(e, permissionResponse);
                    return permissionResponse;
                })
                .toList();

        return DataPageResult.of(findPageResult.getNumber(), findPageResult.getSize(), findPageResult.getTotalElements(), permissionResponseList);
    }

    @Override
    public FindPermissionResponse permissionDetails(Long id) {
        return permissionRepository.findById(id).map(u -> {
            FindPermissionResponse permissionResponse = new FindPermissionResponse();
            BeanUtils.copyProperties(u, permissionResponse);
            return permissionResponse;
        }).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePermission(SavePermissionRequest request) {
        // id是否为空，不为空是修改
        boolean hasId = request.getId() != null;
        // 检查路径是否已存在
        SpecificationBuilder<SysPermission> builder = new SpecificationBuilder<>();
        // 请求路径
        builder.eq(SysPermission::getPath, request.getPath());
        // 请求方式
        if (ObjectUtils.isEmpty(request.getRequestMethod())) {
            builder.isNull(SysPermission::getRequestMethod);
        } else {
            builder.eq(SysPermission::getRequestMethod, request.getRequestMethod());
        }
        // 修改需排除当前数据
        builder.ne(hasId, SysPermission::getId, request.getId());
        boolean exists = permissionRepository.exists(builder);
        if (exists) {
            throw new CloudIllegalArgumentException("路径已存在。");
        }

        // 组装实体信息
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(request, permission);

        // 插入时初始化id
        if (!hasId) {
            permission.setId(sequence.nextId());
            // 初始化默认信息
            permission.setDeleted(Boolean.FALSE);
        } else {
            // 设置插入相关的审计信息
            Optional<SysPermission> permissionOptional = permissionRepository.findById(request.getId());
            if (permissionOptional.isPresent()) {
                SysPermission existsPermission = permissionOptional.get();
                permission.setCreateBy(existsPermission.getCreateBy());
                permission.setCreateName(existsPermission.getCreateName());
                permission.setCreateTime(existsPermission.getCreateTime());
            }
        }

        // 默认设置不删除
        if (permission.getDeleted() == null) {
            permission.setDeleted(Boolean.FALSE);
        }
        permissionRepository.save(permission);

        // 刷新权限缓存
        this.refreshPermissionCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        if (id == null) {
            throw new CloudIllegalArgumentException("权限id不能为空.");
        }
        permissionRepository.deleteById(id);
        // 刷新权限缓存
        this.refreshPermissionCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshPermissionCache() {
        // 查询需要鉴权的接口
        SpecificationBuilder<SysPermission> specificationBuilder = new SpecificationBuilder<>();
        SpecificationBuilder<SysPermission> builder = specificationBuilder
                // 只查询接口权限
                .eq(SysPermission::getPermissionType, PermissionTypeEnum.REST.getValue())
                .eq(SysPermission::getNeedAuthentication, Boolean.TRUE);
        List<SysPermission> permissions = permissionRepository.findAll(builder);

        // 先删除redis中所有权限
        redisOperator.delete(AuthorizeConstants.ALL_PERMISSIONS);

        // 根据path分组后缓存至redis
        if (!ObjectUtils.isEmpty(permissions)) {
            Map<String, List<BasicGrantedAuthority>> permissionPathMap = permissions.stream()
                    // 排除path为空的权限
                    .filter(e -> !ObjectUtils.isEmpty(e.getPath()))
                    .map(e -> {
                        BasicGrantedAuthority authority = new BasicGrantedAuthority();
                        authority.setId(e.getId());
                        authority.setPath(e.getPath());
                        authority.setAuthority(e.getPermission());
                        authority.setPermission(e.getPermission());
                        authority.setRequestMethod(e.getRequestMethod());
                        authority.setPermissionType(e.getPermissionType());
                        authority.setNeedAuthentication(e.getNeedAuthentication());
                        return authority;
                    })
                    .collect(Collectors.groupingBy(BasicGrantedAuthority::getPath));
            redisOperator.set(AuthorizeConstants.ALL_PERMISSIONS, permissionPathMap);
        }
    }

    @Override
    public List<FindPermissionResponse> findPermissions(FindPermissionRequest request) {
        // 排序
        Sort sort = Sort.by(LambdaUtils.extractMethodToProperty(SysPermission::getRank));
        // 条件构造器
        SpecificationBuilder<SysPermission> builder = new SpecificationBuilder<>();
        builder.like(!ObjectUtils.isEmpty(request.getPath()), SysPermission::getPath, request.getPath());
        builder.like(!ObjectUtils.isEmpty(request.getName()), SysPermission::getName, request.getName());
        builder.like(!ObjectUtils.isEmpty(request.getPermission()), SysPermission::getPermission,
                request.getPermission());
        builder.eq(!ObjectUtils.isEmpty(request.getPermissionType()), SysPermission::getPermissionType,
                request.getPermissionType());

        // 执行查询
        List<SysPermission> findPageResult = permissionRepository.findAll(builder, sort);
        // 转为响应bean并返回
        return findPageResult
                .stream()
                .map(e -> {
                    FindPermissionResponse permissionResponse = new FindPermissionResponse();
                    BeanUtils.copyProperties(e, permissionResponse);
                    return permissionResponse;
                })
                .toList();
    }

    @Override
    public List<String> findPermissionIdsByRoleId(Long roleId) {
        List<SysRolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        if (rolePermissions == null || rolePermissions.isEmpty()) {
            return null;
        }
        // 提取所有权限id
        List<Long> permissionIds = rolePermissions.stream()
                .map(SysRolePermission::getPermissionId)
                .toList();

        // 过滤掉有子节点的权限id(ElementPlus Tree组件如果有设置父节点选中，则不管所有子节点是否选中，父节点都选中，这时会让子节点默认全部选中)
        return this.findNonParentPermissions(permissionIds);
    }

    @Override
    public List<String> findNonParentPermissions(List<Long> permissionIds) {
        // 过滤掉有子节点的权限id(ElementPlus Tree组件如果有设置父节点选中，则不管所有子节点是否选中，父节点都选中，这时会让子节点默认全部选中)
        List<SysPermission> permissions = permissionRepository.findAllById(permissionIds);
        // 提取所有父节点id
        Set<Long> parentPermissionIds = permissions.stream()
                .map(SysPermission::getParentId)
                .filter(Objects::nonNull)
                // 去掉根节点标识
                .filter(parentId -> parentId != 0)
                .collect(Collectors.toSet());

        // 过滤掉 parentIds 中的节点（也就是有子节点的父节点）
        return permissionIds.stream()
                .filter(parentId -> !parentPermissionIds.contains(parentId))
                .map(String::valueOf)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdatePermissions(List<SavePermissionRequest> requests) {
        // 判断入参是否存在重复
        Set<String> duplicateChecker = new HashSet<>();
        List<String> duplicates = new ArrayList<>();

        for (SavePermissionRequest request : requests) {
            String key = buildUniqueKey(request.getPath(), request.getRequestMethod());
            if (!duplicateChecker.add(key)) {
                duplicates.add(key);
            }
        }

        if (!duplicates.isEmpty()) {
            throw new CloudServiceException("路径和请求方法组合不能重复: " + String.join(", ", duplicates));
        }

        // 检查是否和已有权限冲突
        for (SavePermissionRequest request : requests) {
            SpecificationBuilder<SysPermission> builder = new SpecificationBuilder<>();
            // 请求方式
            if (ObjectUtils.isEmpty(request.getRequestMethod())) {
                builder.isNull(SysPermission::getRequestMethod);
            } else {
                builder.eq(SysPermission::getRequestMethod, request.getRequestMethod());
            }
            // 请求路径
            builder.eq(SysPermission::getPath, request.getPath());
            // 修改需排除当前数据
            builder.ne(request.getId() != null, SysPermission::getId, request.getId());
            boolean exists = permissionRepository.exists(builder);
            if (exists) {
                duplicates.add(buildUniqueKey(request.getPath(), request.getRequestMethod()));
            }
        }

        if (!duplicates.isEmpty()) {
            throw new CloudServiceException("路径和请求方法组合已存在: " + String.join(", ", duplicates));
        }

        // 批量更新权限
        List<SysPermission> permissionsToSave = requests.stream().map(request -> {
            SysPermission permission = new SysPermission();
            BeanUtils.copyProperties(request, permission);
            // 如果是新增，则设置ID
            if (request.getId() == null) {
                permission.setId(sequence.nextId());
                permission.setDeleted(Boolean.FALSE);
            } else {
                // 如果是修改，则保留原有的创建信息
                Optional<SysPermission> existingPermission = permissionRepository.findById(request.getId());
                existingPermission.ifPresent(existing -> {
                    permission.setCreateBy(existing.getCreateBy());
                    permission.setCreateName(existing.getCreateName());
                    permission.setCreateTime(existing.getCreateTime());
                });
            }
            // 默认设置不删除
            if (permission.getDeleted() == null) {
                permission.setDeleted(Boolean.FALSE);
            }
            return permission;
        }).toList();

        if (!ObjectUtils.isEmpty(permissionsToSave)) {
            permissionRepository.saveAll(permissionsToSave);
        }

        // 刷新权限缓存
        this.refreshPermissionCache();
    }

    /**
     * 构建唯一键，用于检查重复的路径和请求方法组合
     *
     * @param path 请求路径
     * @param requestMethod 请求方法
     * @return 唯一键
     */
    private String buildUniqueKey(String path, String requestMethod) {
        return (path != null ? path : "") + ":" + (requestMethod != null ? requestMethod : "");
    }

}
