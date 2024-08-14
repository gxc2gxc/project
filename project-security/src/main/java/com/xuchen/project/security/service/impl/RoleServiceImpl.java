package com.xuchen.project.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.exception.ClientException;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.dto.RoleDto;
import com.xuchen.project.model.security.dto.RolePermissionDto;
import com.xuchen.project.model.security.pojo.Permission;
import com.xuchen.project.model.security.pojo.Role;
import com.xuchen.project.model.security.pojo.RolePermission;
import com.xuchen.project.security.mapper.PermissionMapper;
import com.xuchen.project.security.mapper.RoleMapper;
import com.xuchen.project.security.mapper.RolePermissionMapper;
import com.xuchen.project.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    private final PermissionMapper permissionMapper;

    private final RolePermissionMapper rolePermissionMapper;

    /**
     * 新增角色
     *
     * @param roleDto 新增角色Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<Long> insert(RoleDto roleDto) {
        log.info("新增角色：{}", roleDto);

        // 检查角色名是否已存在
        Role role = roleMapper.selectOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleName, roleDto.getRoleName()));
        if (role != null) {
            throw new ClientException(ResponseStatus.ROLE_NAME_EXISTED_EXCEPTION);
        }

        // 新增角色
        role = new Role();
        BeanUtils.copyProperties(roleDto, role);
        role.setStatus(1);
        roleMapper.insert(role);

        // 返回执行结果
        return ResponseResult.success(role.getRoleId());
    }

    /**
     * 删除角色
     *
     * @param roleIds 删除角色Id列表
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> delete(List<Long> roleIds) {
        log.info("删除角色：{}", roleIds);

        // 删除角色
        roleMapper.delete(Wrappers.<Role>lambdaQuery().in(Role::getRoleId, roleIds));

        // 返回执行结果
        return ResponseResult.success();
    }

    /**
     * 修改角色
     *
     * @param roleDto 修改角色Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> update(RoleDto roleDto) {
        log.info("修改角色：{}", roleDto);

        // 检查角色是否已存在
        Role role = roleMapper.selectById(roleDto.getRoleId());
        if (role == null) {
            throw new ClientException(ResponseStatus.USER_NOT_EXIST_EXCEPTION);
        }

        // 修改角色
        BeanUtils.copyProperties(roleDto, role);
        roleMapper.updateById(role);

        // 返回执行结果
        return ResponseResult.success();
    }

    /**
     * 查询角色
     *
     * @param roleDto 查询角色Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<List<Role>> select(RoleDto roleDto) {
        log.info("查询角色：{}", roleDto);

        // 查询角色
        List<Role> roles = roleMapper.selectList(Wrappers.<Role>lambdaQuery().like(Role::getRoleName, roleDto.getRoleName()).orderByDesc(Role::getCreateTime));

        // 返回执行结果
        return ResponseResult.success(roles);
    }

    /**
     * 为角色绑定权限
     *
     * @param rolePermissionDto 角色与权限的Id
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> bindingPermission(RolePermissionDto rolePermissionDto) {
        log.info("为角色绑定权限：{}", rolePermissionDto);

        // 检查角色是否存在
        Role role = roleMapper.selectById(rolePermissionDto.getRoleId());
        if (role == null) {
            throw new ClientException(ResponseStatus.ROLE_NOT_EXIST_EXCEPTION);
        }

        // 检查权限是否存在
        Permission permission = permissionMapper.selectById(rolePermissionDto.getPermissionId());
        if (permission == null) {
            throw new ClientException(ResponseStatus.PERMISSION_NOT_EXIST_EXCEPTION);
        }

        // 检查绑定关系是否存在
        RolePermission rolePermission = rolePermissionMapper.selectOne(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, rolePermissionDto.getRoleId()).eq(RolePermission::getPermissionId, rolePermissionDto.getPermissionId()));
        if (rolePermission != null) {
            throw new ClientException(ResponseStatus.USER_ROLE_BINDING_EXIST_EXCEPTION);
        }

        // 绑定权限
        rolePermission = new RolePermission();
        BeanUtils.copyProperties(rolePermissionDto, rolePermission);
        rolePermissionMapper.insert(rolePermission);
        return ResponseResult.success();
    }
}
