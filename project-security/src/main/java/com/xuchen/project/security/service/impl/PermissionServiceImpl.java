package com.xuchen.project.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xuchen.project.model.common.enums.ResponseStatus;
import com.xuchen.project.model.common.exception.ClientException;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.dto.PermissionDto;
import com.xuchen.project.model.security.pojo.Permission;
import com.xuchen.project.security.mapper.PermissionMapper;
import com.xuchen.project.security.service.PermissionService;
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
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;

    /**
     * 新增权限
     *
     * @param permissionDto 新增权限Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<Long> insert(PermissionDto permissionDto) {
        log.info("新增权限：{}", permissionDto);

        // 检查权限名是否已存在
        Permission permission = permissionMapper.selectOne(Wrappers.<Permission>lambdaQuery().eq(Permission::getPermissionName, permissionDto.getPermissionName()));
        if (permission != null) {
            throw new ClientException(ResponseStatus.PERMISSION_NAME_EXISTED_EXCEPTION);
        }

        // 新增权限
        permission = new Permission();
        BeanUtils.copyProperties(permissionDto, permission);
        permission.setStatus(1);
        permissionMapper.insert(permission);

        // 返回执行结果
        return ResponseResult.success(permission.getPermissionId());
    }

    /**
     * 删除权限
     *
     * @param permissionIds 删除权限Id列表
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> delete(List<Long> permissionIds) {
        log.info("删除权限：{}", permissionIds);

        // 删除权限
        permissionMapper.delete(Wrappers.<Permission>lambdaQuery().in(Permission::getPermissionId, permissionIds));

        // 返回执行结果
        return ResponseResult.success();
    }

    /**
     * 修改权限
     *
     * @param permissionDto 修改权限Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<Object> update(PermissionDto permissionDto) {
        log.info("修改权限：{}", permissionDto);

        // 检查权限是否已存在
        Permission permission = permissionMapper.selectById(permissionDto.getPermissionId());
        if (permission == null) {
            throw new ClientException(ResponseStatus.USER_NOT_EXIST_EXCEPTION);
        }

        // 修改权限
        BeanUtils.copyProperties(permissionDto, permission);
        permissionMapper.updateById(permission);

        // 返回执行结果
        return ResponseResult.success();
    }

    /**
     * 查询权限
     *
     * @param permissionDto 查询权限Dto
     * @return 执行结果
     */
    @Override
    public ResponseResult<List<Permission>> select(PermissionDto permissionDto) {
        log.info("查询权限：{}", permissionDto);

        // 查询权限
        List<Permission> permissions = permissionMapper.selectList(Wrappers.<Permission>lambdaQuery().like(Permission::getPermissionName, permissionDto.getPermissionName()).orderByDesc(Permission::getCreateTime));

        // 返回执行结果
        return ResponseResult.success(permissions);
    }
}
