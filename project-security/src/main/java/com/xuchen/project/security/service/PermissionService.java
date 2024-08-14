package com.xuchen.project.security.service;

import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.dto.PermissionDto;
import com.xuchen.project.model.security.pojo.Permission;

import java.util.List;

public interface PermissionService {

    /**
     * 新增权限
     *
     * @param permissionDto 新增权限Dto
     * @return 执行结果
     */
    ResponseResult<Long> insert(PermissionDto permissionDto);

    /**
     * 删除权限
     *
     * @param permissionIds 删除权限Id列表
     * @return 执行结果
     */
    ResponseResult<Object> delete(List<Long> permissionIds);

    /**
     * 修改权限
     *
     * @param permissionDto 修改权限Dto
     * @return 执行结果
     */
    ResponseResult<Object> update(PermissionDto permissionDto);

    /**
     * 查询权限
     *
     * @param permissionDto 查询权限Dto
     * @return 执行结果
     */
    ResponseResult<List<Permission>> select(PermissionDto permissionDto);
}
