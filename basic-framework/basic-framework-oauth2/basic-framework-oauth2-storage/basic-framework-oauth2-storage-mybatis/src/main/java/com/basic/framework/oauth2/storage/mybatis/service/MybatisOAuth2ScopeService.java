package com.basic.framework.oauth2.storage.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.domain.ScopePermissionModel;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import com.basic.framework.oauth2.core.constant.AuthorizeConstants;
import com.basic.framework.oauth2.storage.core.domain.model.ScopeWithDescription;
import com.basic.framework.oauth2.storage.core.domain.request.FindScopePageRequest;
import com.basic.framework.oauth2.storage.core.domain.request.ResetScopePermissionRequest;
import com.basic.framework.oauth2.storage.core.domain.request.SaveScopeRequest;
import com.basic.framework.oauth2.storage.core.domain.response.FindScopeResponse;
import com.basic.framework.oauth2.storage.core.service.OAuth2ScopeService;
import com.basic.framework.oauth2.storage.mybatis.domain.MybatisOAuth2Scope;
import com.basic.framework.oauth2.storage.mybatis.domain.MybatisOAuth2ScopePermission;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2ScopeMapper;
import com.basic.framework.oauth2.storage.mybatis.mapper.MybatisOAuth2ScopePermissionMapper;
import com.basic.framework.redis.support.RedisOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 针对表【oauth2_scope(oauth2客户端的scope)】的数据库操作Service实现
 *
 * @author vains
 */
@RequiredArgsConstructor
public class MybatisOAuth2ScopeService implements OAuth2ScopeService {

    private final MybatisOAuth2ScopeMapper baseMapper;

    private final RedisOperator<List<ScopePermissionModel>> redisOperator;

    private final MybatisOAuth2ScopePermissionMapper scopePermissionMapper;

    @Override
    public PageResult<FindScopeResponse> findScopePage(FindScopePageRequest request) {
        // 查询条件
        LambdaQueryWrapper<MybatisOAuth2Scope> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Scope.class)
                .eq(request.getEnabled() != null, MybatisOAuth2Scope::getEnabled, request.getEnabled())
                .like(!ObjectUtils.isEmpty(request.getScope()), MybatisOAuth2Scope::getScope, request.getScope()).or()
                .like(!ObjectUtils.isEmpty(request.getScope()), MybatisOAuth2Scope::getDescription, request.getScope())
                .orderByDesc(MybatisOAuth2Scope::getUpdateTime);

        IPage<MybatisOAuth2Scope> pageQuery = Page.of(request.getCurrent(), request.getSize());

        // 分页查询
        IPage<MybatisOAuth2Scope> selectScopePage = this.baseMapper.selectPage(pageQuery, wrapper);

        // 转换实体为响应bean
        IPage<FindScopeResponse> page = selectScopePage.convert(e -> {
            FindScopeResponse result = new FindScopeResponse();
            BeanUtils.copyProperties(e, result);
            return result;
        });

        // 组装数据返回
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

    @Override
    public List<ScopeWithDescription> findByScopes(Set<String> scopes) {
        if (ObjectUtils.isEmpty(scopes)) {
            return Collections.emptyList();
        }
        // 构建查询条件
        LambdaQueryWrapper<MybatisOAuth2Scope> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Scope.class)
                .eq(MybatisOAuth2Scope::getEnabled, Boolean.TRUE)
                .in(MybatisOAuth2Scope::getScope, scopes);

        // 查询
        List<MybatisOAuth2Scope> selectScopeList = this.baseMapper.selectList(wrapper);

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
        LambdaQueryWrapper<MybatisOAuth2Scope> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Scope.class)
                .eq(MybatisOAuth2Scope::getEnabled, Boolean.TRUE)
                .eq(MybatisOAuth2Scope::getScope, request.getScope());
        List<MybatisOAuth2Scope> selectList = this.baseMapper.selectList(wrapper);
        if (!ObjectUtils.isEmpty(selectList)) {
            throw new CloudIllegalArgumentException("scope [" + request.getScope() + "] 已存在");
        }
        // 组装数据并持久化
        MybatisOAuth2Scope scope = new MybatisOAuth2Scope();
        scope.setScope(request.getScope());
        scope.setDescription(request.getDescription());
        scope.setEnabled(Boolean.TRUE);
        this.baseMapper.insert(scope);
    }

    @Override
    public void updateScope(SaveScopeRequest request) {
        // 检查scope是否已存在
        LambdaQueryWrapper<MybatisOAuth2Scope> wrapper = Wrappers.lambdaQuery(MybatisOAuth2Scope.class)
                .eq(MybatisOAuth2Scope::getEnabled, Boolean.TRUE)
                .eq(MybatisOAuth2Scope::getScope, request.getScope())
                .ne(MybatisOAuth2Scope::getId, request.getId());

        List<MybatisOAuth2Scope> selectList = this.baseMapper.selectList(wrapper);
        if (!ObjectUtils.isEmpty(selectList)) {
            throw new CloudIllegalArgumentException("scope [" + request.getScope() + "] 已存在");
        }
        // 组装数据并持久化
        MybatisOAuth2Scope scope = new MybatisOAuth2Scope();
        BeanUtils.copyProperties(request, scope);
        this.baseMapper.updateById(scope);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetScopePermission(ResetScopePermissionRequest request) {
        LambdaQueryWrapper<MybatisOAuth2ScopePermission> wrapper = Wrappers.lambdaQuery(MybatisOAuth2ScopePermission.class)
                .eq(MybatisOAuth2ScopePermission::getScope, request.getScope());
        // 先移除原有关联关系
        this.scopePermissionMapper.delete(wrapper);
        // 根据入参将数据转移至实体
        List<Long> permissionsId = request.getPermissionsId();
        if (ObjectUtils.isEmpty(permissionsId)) {
            return;
        }
        List<MybatisOAuth2ScopePermission> scopePermissions = new ArrayList<>();
        for (Long permissionId : permissionsId) {
            MybatisOAuth2ScopePermission permission = new MybatisOAuth2ScopePermission();
            permission.setScope(request.getScope());
            permission.setPermissionId(permissionId);
            scopePermissions.add(permission);
        }
        // 保存关联关系
        this.scopePermissionMapper.insert(scopePermissions);

        // 查询所有数据
        List<MybatisOAuth2ScopePermission> scopePermissionList = this.scopePermissionMapper.selectList(null);
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
}




