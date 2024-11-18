package com.basic.framework.oauth2.storage.jpa.service;

import com.basic.framework.core.domain.PageResult;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import com.basic.framework.core.util.Sequence;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import com.basic.framework.oauth2.storage.core.domain.model.ScopeWithDescription;
import com.basic.framework.oauth2.storage.core.domain.request.FindScopePageRequest;
import com.basic.framework.oauth2.storage.core.domain.request.SaveScopeRequest;
import com.basic.framework.oauth2.storage.core.domain.response.FindScopeResponse;
import com.basic.framework.oauth2.storage.core.service.OAuth2ScopeService;
import com.basic.framework.oauth2.storage.jpa.domain.JpaOAuth2Scope;
import com.basic.framework.oauth2.storage.jpa.repository.OAuth2ScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 针对表【oauth2_scope(oauth2客户端的scope)】的数据库操作Service实现
 *
 * @author vains
 */
@RequiredArgsConstructor
public class JpaOAuth2ScopeService implements OAuth2ScopeService {

    private final OAuth2ScopeRepository scopeRepository;

    private final Sequence sequence = new Sequence(null);

    @Override
    public PageResult<FindScopeResponse> findScopePage(FindScopePageRequest request) {
        // 查询条件
        SpecificationBuilder<JpaOAuth2Scope> builder = new SpecificationBuilder<>();
        builder.eq(request.getEnabled() != null, JpaOAuth2Scope::getEnabled, request.getEnabled())
                .like(!ObjectUtils.isEmpty(request.getScope()), JpaOAuth2Scope::getScope, request.getScope())
                .like(!ObjectUtils.isEmpty(request.getScope()), JpaOAuth2Scope::getDescription, request.getScope());

        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(JpaOAuth2Scope::getUpdateTime));
        // 分页
        PageRequest pageQuery = PageRequest.of(request.getCurrent().intValue(), request.getSize().intValue(), sort);

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
        return PageResult.of((long) selectScopePage.getNumber(), (long) selectScopePage.getSize(), selectScopePage.getTotalElements(), responseList);
    }

    @Override
    public List<ScopeWithDescription> findByScopes(Set<String> scopes) {
        if (ObjectUtils.isEmpty(scopes)) {
            return Collections.emptyList();
        }
        // 构建查询条件
        SpecificationBuilder<JpaOAuth2Scope> builder = new SpecificationBuilder<>();
        builder.eq(JpaOAuth2Scope::getEnabled, Boolean.TRUE)
                .in(JpaOAuth2Scope::getScope, scopes);

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
        scope.setScope(request.getScope());
        scope.setDescription(request.getDescription());
        scope.setEnabled(Boolean.TRUE);
        this.scopeRepository.save(scope);
    }

    @Override
    public void updateScope(SaveScopeRequest request) {
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
        this.scopeRepository.save(scope);
    }
}




