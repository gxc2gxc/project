package com.xuchen.project.security.controller;

import com.xuchen.project.model.common.validation.ValidationGroup;
import com.xuchen.project.model.common.vo.ResponseResult;
import com.xuchen.project.model.security.dto.PermissionDto;
import com.xuchen.project.model.security.pojo.Permission;
import com.xuchen.project.security.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security/permission")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 新增权限
     *
     * @param permissionDto 新增权限Dto
     * @return 执行结果
     */
    @PostMapping("/insert")
    public ResponseResult<Long> insert(@RequestBody @Validated({ValidationGroup.Insert.class}) PermissionDto permissionDto) {
        return permissionService.insert(permissionDto);
    }

    /**
     * 删除权限
     *
     * @param permissionIds 删除权限Id列表
     * @return 执行结果
     */
    @DeleteMapping("/delete")
    public ResponseResult<Object> delete(@RequestParam("permissionIds") List<Long> permissionIds) {
        return permissionService.delete(permissionIds);
    }

    /**
     * 修改权限
     *
     * @param permissionDto 修改权限Dto
     * @return 执行结果
     */
    @PutMapping("/update")
    public ResponseResult<Object> update(@RequestBody @Validated({ValidationGroup.Update.class}) PermissionDto permissionDto) {
        return permissionService.update(permissionDto);
    }

    /**
     * 查询权限
     *
     * @param permissionDto 查询权限Dto
     * @return 执行结果
     */
    @GetMapping("/select")
    public ResponseResult<List<Permission>> select(@RequestBody PermissionDto permissionDto) {
        return permissionService.select(permissionDto);
    }
}
