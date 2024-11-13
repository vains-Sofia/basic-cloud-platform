package com.basic.cloud.authorization.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.basic.cloud.authorization.server.domain.ScopeWithDescription;
import com.basic.cloud.authorization.server.domain.entity.OAuth2Scope;
import com.basic.cloud.authorization.server.domain.request.FindScopePageRequest;
import com.basic.cloud.authorization.server.domain.request.SaveScopeRequest;
import com.basic.cloud.authorization.server.domain.response.FindScopeResponse;
import com.basic.cloud.authorization.server.mapper.OAuth2ScopeMapper;
import com.basic.cloud.authorization.server.service.OAuth2ScopeService;
import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
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
@Service
public class OAuth2ScopeServiceImpl extends ServiceImpl<OAuth2ScopeMapper, OAuth2Scope>
        implements OAuth2ScopeService {

    @Override
    public PageResult<FindScopeResponse> findScopePage(FindScopePageRequest request) {
        // 查询条件
        LambdaQueryWrapper<OAuth2Scope> wrapper = Wrappers.lambdaQuery(OAuth2Scope.class)
                .eq(request.getEnabled() != null, OAuth2Scope::getEnabled, request.getEnabled())
                .like(!ObjectUtils.isEmpty(request.getScope()), OAuth2Scope::getScope, request.getScope()).or()
                .like(!ObjectUtils.isEmpty(request.getScope()), OAuth2Scope::getDescription, request.getScope())
                .orderByDesc(OAuth2Scope::getUpdateTime);

        IPage<OAuth2Scope> pageQuery = Page.of(request.getCurrent(), request.getSize());

        // 分页查询
        IPage<OAuth2Scope> selectScopePage = this.baseMapper.selectPage(pageQuery, wrapper);

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
        LambdaQueryWrapper<OAuth2Scope> wrapper = Wrappers.lambdaQuery(OAuth2Scope.class)
                .eq(OAuth2Scope::getEnabled, Boolean.TRUE)
                .in(OAuth2Scope::getScope, scopes);

        // 查询
        List<OAuth2Scope> selectScopeList = this.baseMapper.selectList(wrapper);

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
        LambdaQueryWrapper<OAuth2Scope> wrapper = Wrappers.lambdaQuery(OAuth2Scope.class)
                .eq(OAuth2Scope::getEnabled, Boolean.TRUE)
                .eq(OAuth2Scope::getScope, request.getScope());
        List<OAuth2Scope> selectList = this.baseMapper.selectList(wrapper);
        if (!ObjectUtils.isEmpty(selectList)) {
            throw new CloudIllegalArgumentException("scope [" + request.getScope() + "] 已存在");
        }
        // 组装数据并持久化
        OAuth2Scope scope = new OAuth2Scope();
        scope.setScope(request.getScope());
        scope.setDescription(request.getDescription());
        scope.setEnabled(Boolean.TRUE);
        this.save(scope);
    }

    @Override
    public void updateScope(SaveScopeRequest request) {
        // 检查scope是否已存在
        LambdaQueryWrapper<OAuth2Scope> wrapper = Wrappers.lambdaQuery(OAuth2Scope.class)
                .eq(OAuth2Scope::getEnabled, Boolean.TRUE)
                .eq(OAuth2Scope::getScope, request.getScope())
                .ne(OAuth2Scope::getId, request.getId());

        List<OAuth2Scope> selectList = this.baseMapper.selectList(wrapper);
        if (!ObjectUtils.isEmpty(selectList)) {
            throw new CloudIllegalArgumentException("scope [" + request.getScope() + "] 已存在");
        }
        // 组装数据并持久化
        OAuth2Scope scope = new OAuth2Scope();
        BeanUtils.copyProperties(request, scope);
        this.updateById(scope);
    }
}




