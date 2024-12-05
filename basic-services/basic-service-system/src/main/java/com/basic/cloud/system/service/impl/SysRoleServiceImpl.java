package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.FindRolePageRequest;
import com.basic.cloud.system.api.domain.request.SaveRoleRequest;
import com.basic.cloud.system.api.domain.response.FindRoleResponse;
import com.basic.cloud.system.domain.SysRole;
import com.basic.cloud.system.repository.SysRoleRepository;
import com.basic.cloud.system.service.SysRoleService;
import com.basic.framework.core.domain.DataPageResult;
import com.basic.framework.core.exception.CloudIllegalArgumentException;
import com.basic.framework.core.util.Sequence;
import com.basic.framework.data.jpa.lambda.LambdaUtils;
import com.basic.framework.data.jpa.specification.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

/**
 * RBAC角色 Service 实现
 *
 * @author YuJx
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleRepository roleRepository;

    private final Sequence sequence = new Sequence((null));

    @Override
    public DataPageResult<FindRoleResponse> findByPage(FindRolePageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(SysRole::getUpdateTime));
        // 分页
        PageRequest pageQuery = PageRequest.of(request.current(), request.size(), sort);

        // 条件构造器
        SpecificationBuilder<SysRole> builder = new SpecificationBuilder<>();
        builder.like(!ObjectUtils.isEmpty(request.getCode()), SysRole::getCode, request.getCode());
        builder.like(!ObjectUtils.isEmpty(request.getName()), SysRole::getName, request.getName());
        builder.like(!ObjectUtils.isEmpty(request.getDescription()), SysRole::getDescription, request.getDescription());

        // 执行查询
        Page<SysRole> findPageResult = roleRepository.findAll(builder, pageQuery);
        // 转为响应bean
        List<FindRoleResponse> authorizationList = findPageResult.getContent()
                .stream()
                .map(e -> {
                    FindRoleResponse roleResponse = new FindRoleResponse();
                    BeanUtils.copyProperties(e, roleResponse);
                    return roleResponse;
                })
                .toList();

        return DataPageResult.of(findPageResult.getNumber(), findPageResult.getSize(), findPageResult.getTotalElements(), authorizationList);
    }

    @Override
    public FindRoleResponse roleDetails(Long id) {
        return roleRepository.findById(id).map(u -> {
            FindRoleResponse roleResponse = new FindRoleResponse();
            BeanUtils.copyProperties(u, roleResponse);
            return roleResponse;
        }).orElse(null);
    }

    @Override
    public void saveRole(SaveRoleRequest request) {
        // id是否为空，不为空是修改
        boolean hasId = request.getId() != null;
        // 检查路径是否已存在
        SpecificationBuilder<SysRole> builder = new SpecificationBuilder<>();
        // 路径，请求方式
        builder.eq(SysRole::getCode, request.getCode());
        // 修改需排除当前数据
        builder.ne(hasId, SysRole::getId, request.getId());
        boolean exists = roleRepository.exists(builder);
        if (exists) {
            throw new CloudIllegalArgumentException("角色代码已存在。");
        }
        // 组装实体信息
        SysRole role = new SysRole();

        // 插入时初始化id与密码
        if (!hasId) {
            BeanUtils.copyProperties(request, role);
            role.setId(sequence.nextId());
            // 初始化默认信息
            role.setDeleted(Boolean.FALSE);
        } else {
            // 设置插入相关的审计信息
            Optional<SysRole> roleOptional = roleRepository.findById(request.getId());
            if (roleOptional.isPresent()) {
                // 转移数据至实体
                SysRole existsRole = roleOptional.get();
                role.setId(existsRole.getId());
                role.setCode(request.getCode());
                role.setName(request.getName());
                role.setDescription(request.getDescription());
                role.setDeleted(existsRole.getDeleted());
                role.setCreateBy(existsRole.getCreateBy());
                role.setCreateName(existsRole.getCreateName());
                role.setCreateTime(existsRole.getCreateTime());
            }
        }

        roleRepository.save(role);
    }

    @Override
    public void removeById(Long id) {
        if (id == null) {
            throw new CloudIllegalArgumentException("角色id不能为空.");
        }
        roleRepository.deleteById(id);
    }

}
