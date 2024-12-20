package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.FindPermissionPageRequest;
import com.basic.cloud.system.api.domain.request.SavePermissionRequest;
import com.basic.cloud.system.api.domain.response.FindPermissionResponse;
import com.basic.cloud.system.domain.SysPermission;
import com.basic.cloud.system.repository.SysPermissionRepository;
import com.basic.cloud.system.service.SysPermissionService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.PermissionModel;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import com.basic.framework.core.util.Sequence;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RBAC权限信息Service实现
 *
 * @author YuJx
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {

    private final Sequence sequence = new Sequence((null));

    private final SysPermissionRepository permissionRepository;

    private final RedisOperator<Map<String, List<PermissionModel>>> redisOperator;

    @Override
    public DataPageResult<FindPermissionResponse> findByPage(FindPermissionPageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(SysPermission::getUpdateTime));
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
        List<FindPermissionResponse> authorizationList = findPageResult.getContent()
                .stream()
                .map(e -> {
                    FindPermissionResponse permissionResponse = new FindPermissionResponse();
                    BeanUtils.copyProperties(e, permissionResponse);
                    return permissionResponse;
                })
                .toList();

        return DataPageResult.of(findPageResult.getNumber(), findPageResult.getSize(), findPageResult.getTotalElements(), authorizationList);
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

        // 插入时初始化id与密码
        if (!hasId) {
            permission.setId(sequence.nextId());
            // 初始化默认信息
            permission.setDeleted(Boolean.FALSE);
        } else {
            // 设置插入相关的审计信息
            Optional<SysPermission> permissionOptional = permissionRepository.findById(request.getId());
            if (permissionOptional.isPresent()) {
                SysPermission  existsPermission = permissionOptional.get();
                permission.setCreateBy(existsPermission.getCreateBy());
                permission.setCreateName(existsPermission.getCreateName());
                permission.setCreateTime(existsPermission.getCreateTime());
            }
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
    public void refreshPermissionCache() {
        // 查询需要鉴权的接口
        SpecificationBuilder<SysPermission> specificationBuilder = new SpecificationBuilder<>();
        SpecificationBuilder<SysPermission> builder = specificationBuilder
                // 只查询接口权限
                .eq(SysPermission::getPermissionType, 1)
                .eq(SysPermission::getNeedAuthentication, Boolean.TRUE);
        List<SysPermission> permissions = permissionRepository.findAll(builder);

        // 根据path分组后缓存至redis
        if (!ObjectUtils.isEmpty(permissions)) {
            Map<String, List<PermissionModel>> permissionPathMap = permissions.stream()
                    // 排除path为空的权限
                    .filter(e -> !ObjectUtils.isEmpty(e.getPath()))
                    .map(e -> {
                        PermissionModel authority = new PermissionModel();
                        authority.setId(e.getId());
                        authority.setPath(e.getPath());
                        authority.setPermission(e.getPermission());
                        authority.setRequestMethod(e.getRequestMethod());
                        authority.setNeedAuthentication(e.getNeedAuthentication());
                        return authority;
                    })
                    .collect(Collectors.groupingBy(PermissionModel::getPath));
            redisOperator.set(AuthorizeConstants.ALL_PERMISSIONS, permissionPathMap);
        }
    }

}
