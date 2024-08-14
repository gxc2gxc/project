package com.xuchen.project.security.controller;

import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.dto.RoleDto;
import com.xuchen.project.model.security.dto.RolePermissionDto;
import com.xuchen.project.model.security.pojo.Role;
import com.xuchen.project.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security/role")
public class RoleController {

    private final RoleService roleService;

    /**
     * 新增角色
     *
     * @param roleDto 新增角色Dto
     * @return 执行结果
     */
    @PostMapping("/insert")
    public ResponseResult<Long> insert(@RequestBody RoleDto roleDto) {
        return roleService.insert(roleDto);
    }

    /**
     * 删除角色
     *
     * @param roleIds 删除角色Id列表
     * @return 执行结果
     */
    @DeleteMapping("/delete")
    public ResponseResult<Object> delete(@RequestParam("roleIds") List<Long> roleIds) {
        return roleService.delete(roleIds);
    }

    /**
     * 修改角色
     *
     * @param roleDto 修改角色Dto
     * @return 执行结果
     */
    @PutMapping("/update")
    public ResponseResult<Object> update(@RequestBody RoleDto roleDto) {
        return roleService.update(roleDto);
    }

    /**
     * 查询角色
     *
     * @param roleDto 查询角色Dto
     * @return 执行结果
     */
    @GetMapping("/select")
    public ResponseResult<List<Role>> select(@RequestBody RoleDto roleDto) {
        return roleService.select(roleDto);
    }

    /**
     * 为角色绑定权限
     *
     * @param rolePermissionDto 角色与权限的Id
     * @return 执行结果
     */
    @PostMapping("/bindingPermission")
    public ResponseResult<Object> bindingPermission(@RequestBody RolePermissionDto rolePermissionDto) {
        return roleService.bindingPermission(rolePermissionDto);
    }
}
