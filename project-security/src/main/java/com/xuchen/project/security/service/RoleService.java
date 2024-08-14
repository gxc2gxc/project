package com.xuchen.project.security.service;

import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.dto.RoleDto;
import com.xuchen.project.model.security.dto.RolePermissionDto;
import com.xuchen.project.model.security.pojo.Role;

import java.util.List;

public interface RoleService {

    /**
     * 新增角色
     *
     * @param roleDto 新增角色Dto
     * @return 执行结果
     */
    ResponseResult<Long> insert(RoleDto roleDto);

    /**
     * 删除角色
     *
     * @param roleIds 删除角色Id列表
     * @return 执行结果
     */
    ResponseResult<Object> delete(List<Long> roleIds);

    /**
     * 修改角色
     *
     * @param roleDto 修改角色Dto
     * @return 执行结果
     */
    ResponseResult<Object> update(RoleDto roleDto);

    /**
     * 查询角色
     *
     * @param roleDto 查询角色Dto
     * @return 执行结果
     */
    ResponseResult<List<Role>> select(RoleDto roleDto);

    /**
     * 为角色绑定权限
     *
     * @param rolePermissionDto 角色与权限的Id
     * @return 执行结果
     */
    ResponseResult<Object> bindingPermission(RolePermissionDto rolePermissionDto);
}
