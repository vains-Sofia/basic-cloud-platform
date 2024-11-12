package com.basic.cloud.authorization.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.basic.cloud.authorization.server.domain.entity.OAuth2Scope;
import com.basic.cloud.authorization.server.domain.request.FindScopePageRequest;
import com.basic.cloud.authorization.server.domain.request.FindScopeResult;
import com.basic.cloud.authorization.server.mapper.OAuth2ScopeMapper;
import com.basic.cloud.authorization.server.service.OAuth2ScopeService;
import com.basic.framework.core.domain.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 针对表【oauth2_scope(oauth2客户端的scope)】的数据库操作Service实现
 *
 * @author vains
 */
@Service
public class OAuth2ScopeServiceImpl extends ServiceImpl<OAuth2ScopeMapper, OAuth2Scope>
        implements OAuth2ScopeService {

    @Override
    public PageResult<FindScopeResult> findScopePage(FindScopePageRequest request) {
        // 查询条件
        LambdaQueryWrapper<OAuth2Scope> wrapper = Wrappers.lambdaQuery(OAuth2Scope.class)
                .eq(request.getEnabled() != null, OAuth2Scope::getEnabled, request.getEnabled())
                .like(ObjectUtils.isEmpty(request.getName()), OAuth2Scope::getName, request.getName()).or()
                .like(ObjectUtils.isEmpty(request.getName()), OAuth2Scope::getDescription, request.getName());

        IPage<OAuth2Scope> pageQuery = Page.of(request.getCurrent(), request.getSize());

        // 分页查询
        IPage<OAuth2Scope> selectScopePage = this.baseMapper.selectPage(pageQuery, wrapper);

        // 转换实体为响应bean
        IPage<FindScopeResult> page = selectScopePage.convert(e -> {
            FindScopeResult result = new FindScopeResult();
            BeanUtils.copyProperties(e, result);
            return result;
        });

        // 组装数据返回
        return PageResult.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }
}




