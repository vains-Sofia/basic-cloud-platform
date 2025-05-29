package com.basic.cloud.system.service.impl;

import com.basic.cloud.system.api.domain.request.FindRolePageRequest;
import com.basic.cloud.system.api.domain.request.FindRoleRequest;
import com.basic.cloud.system.api.domain.request.SaveRoleRequest;
import com.basic.cloud.system.api.domain.request.UpdateRolePermissionsRequest;
import com.basic.cloud.system.api.domain.response.FindRoleResponse;
import com.basic.cloud.system.domain.SysRole;
import com.basic.cloud.system.domain.SysRolePermission;
import com.basic.cloud.system.domain.SysUserRole;
import com.basic.cloud.system.repository.SysRolePermissionRepository;
import com.basic.cloud.system.repository.SysRoleRepository;
import com.basic.cloud.system.repository.SysUserRoleRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

/**
 * RBAC角色 Service 实现
 *
 * @author vains
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleRepository roleRepository;

    private final SysUserRoleRepository userRoleRepository;

    private final Sequence sequence = new Sequence((null));

    private final SysRolePermissionRepository rolePermissionRepository;

    @Override
    public DataPageResult<FindRoleResponse> findByPage(FindRolePageRequest request) {
        // 排序
        Sort sort = Sort.by(Sort.Direction.DESC, LambdaUtils.extractMethodToProperty(SysRole::getCreateTime));
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
        List<FindRoleResponse> rolesList = findPageResult.getContent()
                .stream()
                .map(e -> {
                    FindRoleResponse roleResponse = new FindRoleResponse();
                    BeanUtils.copyProperties(e, roleResponse);
                    return roleResponse;
                })
                .toList();

        return DataPageResult.of(findPageResult.getNumber(), findPageResult.getSize(), findPageResult.getTotalElements(), rolesList);
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
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        if (id == null) {
            throw new CloudIllegalArgumentException("角色id不能为空.");
        }
        roleRepository.findById(id).ifPresent(u -> {
            // 如果角色存在，检查是否有用户使用
            List<SysUserRole> userRoles = userRoleRepository.findByRoleId(id);
            if (!ObjectUtils.isEmpty(userRoles)) {
                throw new CloudIllegalArgumentException("角色已被用户使用，不能删除.");
            }
            // 如果角色存在，删除角色
            roleRepository.deleteById(id);
            // 删除角色权限
            rolePermissionRepository.deleteByRoleId(id);
        });

    }

    @Override
    public List<FindRoleResponse> findRoles(FindRoleRequest request) {
        // 条件构造器
        SpecificationBuilder<SysRole> builder = new SpecificationBuilder<>();
        builder.like(!ObjectUtils.isEmpty(request.getName()), SysRole::getName, request.getName());
        builder.like(!ObjectUtils.isEmpty(request.getCode()), SysRole::getCode, request.getCode());
        builder.like(!ObjectUtils.isEmpty(request.getDescription()), SysRole::getDescription, request.getDescription());

        // 执行查询
        List<SysRole> findResult = roleRepository.findAll(builder);
        // 转为响应bean
        return findResult.stream()
                .map(e -> {
                    FindRoleResponse roleResponse = new FindRoleResponse();
                    BeanUtils.copyProperties(e, roleResponse);
                    return roleResponse;
                })
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRolePermissions(UpdateRolePermissionsRequest request) {
        boolean exists = roleRepository.existsById(request.getRoleId());
        if (!exists) {
            throw new CloudIllegalArgumentException("角色不存在.");
        }
        // 删除原有权限
        rolePermissionRepository.deleteByRoleId(request.getRoleId());

        // 插入新权限
        List<SysRolePermission> rolePermissions = request.getPermissionIds().stream().map(id -> {
            SysRolePermission rolePermission = new SysRolePermission();
            rolePermission.setId(sequence.nextId());
            rolePermission.setRoleId(request.getRoleId());
            rolePermission.setPermissionId(id);
            return rolePermission;
        }).toList();
        if (ObjectUtils.isEmpty(rolePermissions)) {
            return;
        }
        rolePermissionRepository.saveAll(rolePermissions);
    }

}
