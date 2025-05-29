package com.basic.framework.oauth2.storage.service.impl;

import com.basic.cloud.system.api.SysPermissionClient;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.Result;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import com.basic.framework.core.exception.CloudServiceException;
import com.basic.framework.core.util.Sequence;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.core.domain.security.ScopePermissionModel;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2Scope;
import com.basic.framework.oauth2.storage.domain.entity.JpaOAuth2ScopePermission;
import com.basic.framework.oauth2.storage.domain.model.ScopeWithDescription;
import com.basic.framework.oauth2.storage.domain.request.FindScopePageRequest;
import com.basic.framework.oauth2.storage.domain.request.ResetScopePermissionRequest;
import com.basic.framework.oauth2.storage.domain.request.SaveScopeRequest;
import com.basic.framework.oauth2.storage.domain.response.FindScopeResponse;
import com.basic.framework.oauth2.storage.repository.OAuth2ScopePermissionRepository;
import com.basic.framework.oauth2.storage.repository.OAuth2ScopeRepository;
import com.basic.framework.oauth2.storage.service.OAuth2ScopeService;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 针对表【oauth2_scope(oauth2客户端的scope)】的数据库操作Service实现
 *
 * @author vains
 */
@RequiredArgsConstructor
public class OAuth2ScopeServiceImpl implements OAuth2ScopeService {

    private final OAuth2ScopeRepository scopeRepository;

    private final SysPermissionClient sysPermissionClient;

    private final Sequence sequence = new Sequence(null);

    private final RedisOperator<List<ScopePermissionModel>> redisOperator;

    private final OAuth2ScopePermissionRepository scopePermissionRepository;

    @Override
    public PageResult<FindScopeResponse> findScopePage(FindScopePageRequest request) {
        // 查询条件
        SpecificationBuilder<JpaOAuth2Scope> builder = new SpecificationBuilder<>();
        builder.or(or -> or
                        .like(!ObjectUtils.isEmpty(request.getName()), JpaOAuth2Scope::getName, request.getName())
                        .like(!ObjectUtils.isEmpty(request.getName()), JpaOAuth2Scope::getDescription, request.getName())
                )
                .like(!ObjectUtils.isEmpty(request.getScope()), JpaOAuth2Scope::getScope, request.getScope())
                .eq(request.getEnabled() != null, JpaOAuth2Scope::getEnabled, request.getEnabled());

        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(JpaOAuth2Scope::getCreateTime));
        // 分页
        PageRequest pageQuery = PageRequest.of(request.current(), request.size(), sort);

        // 分页查询
        Page<JpaOAuth2Scope> selectScopePage = this.scopeRepository.findAll(builder, pageQuery);

        // 转换实体为响应bean
        List<FindScopeResponse> responseList = selectScopePage.getContent()
                .stream()
                .map(e -> {
                    FindScopeResponse result = new FindScopeResponse();
                    BeanUtils.copyProperties(e, result);
                    return result;
                }).toList();

        // 组装数据返回
        return DataPageResult.of(selectScopePage.getNumber(), selectScopePage.getSize(), selectScopePage.getTotalElements(), responseList);
    }

    @Override
    public List<FindScopeResponse> findScopeAll() {
        // 查询条件
        SpecificationBuilder<JpaOAuth2Scope> builder = new SpecificationBuilder<>();
        builder.eq(JpaOAuth2Scope::getEnabled, Boolean.TRUE);

        // 分页查询
        List<JpaOAuth2Scope> selectScopeList = this.scopeRepository.findAll(builder);

        // 转换实体为响应bean
        return selectScopeList
                .stream()
                .map(e -> {
                    FindScopeResponse result = new FindScopeResponse();
                    BeanUtils.copyProperties(e, result);
                    return result;
                }).toList();
    }

    @Override
    public List<ScopeWithDescription> findByScopes(Set<String> scopes) {
        if (ObjectUtils.isEmpty(scopes)) {
            return Collections.emptyList();
        }
        // 构建查询条件
        SpecificationBuilder<JpaOAuth2Scope> builder = new SpecificationBuilder<>();
        builder.in(JpaOAuth2Scope::getScope, scopes)
                .eq(JpaOAuth2Scope::getEnabled, Boolean.TRUE);

        // 查询
        List<JpaOAuth2Scope> selectScopeList = this.scopeRepository.findAll(builder);

        // 转换后返回
        List<ScopeWithDescription> withDescriptionList = selectScopeList
                .stream()
                .map(e -> new ScopeWithDescription(e.getScope(), e.getDescription()))
                .toList();
        return new ArrayList<>(withDescriptionList);
    }

    @Override
    public void saveScope(SaveScopeRequest request) {
        // 检查scope是否已存在
        Optional<JpaOAuth2Scope> byScope = scopeRepository.findByScopeAndEnabled(request.getScope(), Boolean.TRUE);
        if (byScope.isPresent()) {
            throw new CloudIllegalArgumentException("scope [" + request.getScope() + "] 已存在");
        }
        // 组装数据并持久化
        JpaOAuth2Scope scope = new JpaOAuth2Scope();
        scope.setId(sequence.nextId());
        scope.setName(request.getName());
        scope.setScope(request.getScope());
        scope.setDescription(request.getDescription());
        scope.setEnabled(Boolean.TRUE);
        this.scopeRepository.save(scope);
    }

    @Override
    public void updateScope(SaveScopeRequest request) {
        Optional<JpaOAuth2Scope> existsScopeOptional = this.scopeRepository.findById(request.getId());
        if (existsScopeOptional.isEmpty()) {
            throw new CloudIllegalArgumentException("scope [" + request.getId() + "] 不存在.");
        }
        // 检查scope是否已存在
        SpecificationBuilder<JpaOAuth2Scope> builder = new SpecificationBuilder<>();
        builder.eq(JpaOAuth2Scope::getEnabled, Boolean.TRUE)
                .eq(JpaOAuth2Scope::getScope, request.getScope())
                .ne(JpaOAuth2Scope::getId, request.getId());

        List<JpaOAuth2Scope> selectList = this.scopeRepository.findAll(builder);
        if (!ObjectUtils.isEmpty(selectList)) {
            throw new CloudIllegalArgumentException("scope [" + request.getScope() + "] 已存在");
        }
        // 组装数据并持久化
        JpaOAuth2Scope scope = new JpaOAuth2Scope();
        BeanUtils.copyProperties(request, scope);

        // 不修改内容
        JpaOAuth2Scope existsScope = existsScopeOptional.get();
        scope.setCreateBy(existsScope.getCreateBy());
        scope.setCreateName(existsScope.getCreateName());
        scope.setCreateTime(existsScope.getCreateTime());
        this.scopeRepository.save(scope);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetScopePermission(ResetScopePermissionRequest request) {
        // 先移除原有关联关系
        this.scopePermissionRepository.deleteByScope(request.getScope());
        // 根据入参将数据转移至实体
        List<Long> permissionsId = request.getPermissionsId();
        if (ObjectUtils.isEmpty(permissionsId)) {
            // 删除缓存
            redisOperator.delete(AuthorizeConstants.SCOPE_PERMISSION_KEY);
            return;
        }
        List<JpaOAuth2ScopePermission> scopePermissions = new ArrayList<>();
        for (Long permissionId : permissionsId) {
            JpaOAuth2ScopePermission permission = new JpaOAuth2ScopePermission();
            permission.setId(sequence.nextId());
            permission.setScope(request.getScope());
            permission.setPermissionId(permissionId);
            scopePermissions.add(permission);
        }
        // 保存关联关系
        this.scopePermissionRepository.saveAll(scopePermissions);

        // 查询所有数据
        List<JpaOAuth2ScopePermission> scopePermissionList = this.scopePermissionRepository.findAll();
        List<ScopePermissionModel> permissionModelList = scopePermissionList.stream()
                .map(e -> {
                    ScopePermissionModel model = new ScopePermissionModel();
                    BeanUtils.copyProperties(e, model);
                    return model;
                }).toList();

        // 删除缓存
        redisOperator.delete(AuthorizeConstants.SCOPE_PERMISSION_KEY);
        // 刷新缓存
        redisOperator.set(AuthorizeConstants.SCOPE_PERMISSION_KEY, new ArrayList<>(permissionModelList));
    }

    @Override
    public void removeScopeById(Long id) {
        if (id == null) {
            throw new CloudIllegalArgumentException("scope id不能为空.");
        }
        Optional<JpaOAuth2Scope> existsOptional = this.scopeRepository.findById(id);
        if (existsOptional.isPresent()) {
            // 删除scope数据
            this.scopeRepository.deleteById(id);
            // 删除scope关联的权限数据
            JpaOAuth2Scope exists = existsOptional.get();
            this.scopePermissionRepository.deleteByScope(exists.getScope());
        }
    }

    @Override
    public List<Long> findPermissionIdsByScope(String scope) {
        List<JpaOAuth2ScopePermission> scopePermissions = this.scopePermissionRepository.findByScope(scope);
        if (scopePermissions == null || scopePermissions.isEmpty()) {
            return null;
        }
        // 提取所有权限id
        List<Long> permissionIds = scopePermissions.stream()
                .map(JpaOAuth2ScopePermission::getPermissionId)
                .toList();

        // 过滤掉有子节点的权限id(ElementPlus Tree组件如果有设置父节点选中，则不管所有子节点是否选中，父节点都选中，这时会让子节点默认全部选中)
        Result<List<Long>> result = sysPermissionClient.findNonParentPermissions(permissionIds);
        if (result == null) {
            throw new CloudServiceException("查询权限失败，服务不可用");
        }
        if (!Objects.equals(result.getCode(), HttpStatus.OK.value())) {
            throw new CloudServiceException(result.getMessage());
        }
        return result.getData();
    }
}




